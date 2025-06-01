package br.udesc.ddm.condominioapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvAppName = findViewById(R.id.tvAppName);
        TextView tvDescricao = findViewById(R.id.tvDescricao);
        TextView tvCreditos = findViewById(R.id.tvCreditos);
        ImageView logo = findViewById(R.id.ivLogo);
        tvAppName.setText(getString(R.string.app_name) + " " + getVersionName());
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        logo.post(() -> logo.startAnimation(fade));
        logo.post(() -> logo.startAnimation(bounce));
        tvAppName.post(() -> tvAppName.startAnimation(slide));
        tvDescricao.post(() -> tvDescricao.startAnimation(slide));
        tvCreditos.post(() -> tvCreditos.startAnimation(slide));
    }

    private String getVersionName() {
        try {
            PackageInfo p = getPackageManager().getPackageInfo(getPackageName(), 0);
            return "v" + p.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "v-?";
        }
    }
}
