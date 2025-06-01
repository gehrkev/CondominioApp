package br.udesc.ddm.condominioapp.ui.about; // Sugestão de pacote

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.udesc.ddm.condominioapp.R;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvAppName = view.findViewById(R.id.tvAppName);
        TextView tvDescricao = view.findViewById(R.id.tvDescricao);
        TextView tvCreditos = view.findViewById(R.id.tvCreditos);
        ImageView logo = view.findViewById(R.id.ivLogo);

        if (tvAppName != null) {
            tvAppName.setText(getString(R.string.app_name) + " " + getVersionName());
        }

        Animation fade = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
        Animation bounce = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce);

        if (logo != null) {
            logo.post(() -> {
                logo.startAnimation(fade);
                logo.startAnimation(bounce);
            });
        }
        if (tvAppName != null) {
            tvAppName.post(() -> tvAppName.startAnimation(slide));
        }
        if (tvDescricao != null) {
            tvDescricao.post(() -> tvDescricao.startAnimation(slide));
        }
        if (tvCreditos != null) {
            tvCreditos.post(() -> tvCreditos.startAnimation(slide));
        }
    }

    private String getVersionName() {
        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            return "v" + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "v-?";
        }
    }
}