package br.udesc.ddm.condominioapp.ui.condominio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.ui.bloco.BlocoListActivity;

public class CondominioFragment extends Fragment {

    private CondominioViewModel viewModel;
    private CondominioAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_condominio_list, container, false);

        Button btnNovoCondominio = root.findViewById(R.id.btnNovoCondominio);
        btnNovoCondominio.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CondominioFormActivity.class));
        });

        View btnVoltar = root.findViewById(R.id.btnVoltar);
        if (btnVoltar != null) btnVoltar.setVisibility(View.GONE);

        // views
        RecyclerView rv = root.findViewById(R.id.rvCondominios);
        SwipeRefreshLayout swipe = root.findViewById(R.id.swipeRefresh);
        View emptyView = root.findViewById(R.id.tvEmpty);
        View progress = root.findViewById(R.id.progressBar);

        CondominioAdapter.CondominioListener listener = new CondominioAdapter.CondominioListener() {

            @Override
            public void onVerBlocosClick(Condominio c) {
                Intent i = new Intent(requireContext(), BlocoListActivity.class);
                i.putExtra("condominio_id", c.getId());
                i.putExtra("condominio_nome", c.getNome());
                startActivity(i);
            }

            @Override
            public void onEditClick(Condominio c) {
                Intent i = new Intent(requireContext(), CondominioFormActivity.class);
                i.putExtra("condominio", c);
                startActivity(i);
            }

            @Override
            public void onDeleteClick(Condominio c) {
                viewModel.removerCondominio(c);
            }
        };
        adapter = new CondominioAdapter(requireContext(), listener);


        adapter = new CondominioAdapter(requireContext(), listener);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(CondominioViewModel.class);

        viewModel.getCondominios().observe(getViewLifecycleOwner(), lista -> {
            adapter.updateList(lista);
            emptyView.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
        });
        viewModel.getIsLoading().observe(getViewLifecycleOwner(),
                l -> {
                    progress.setVisibility(l ? View.VISIBLE : View.GONE);
                    swipe.setRefreshing(l);
                });
        viewModel.getMensagemErro().observe(getViewLifecycleOwner(),
                m -> {
                    if (m != null) Snackbar.make(root, m, Snackbar.LENGTH_LONG).show();
                });

        swipe.setOnRefreshListener(viewModel::carregarCondominios);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.carregarCondominios();
    }

}
