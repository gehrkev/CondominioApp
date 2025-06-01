package br.udesc.ddm.condominioapp.ui.locatario;

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
import br.udesc.ddm.condominioapp.data.model.Locatario;

public class LocatarioFragment extends Fragment {

    private LocatarioViewModel viewModel;
    private LocatarioAdapter   adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_locatario_list, container, false);

        Button btnNovoLocatario = root.findViewById(R.id.btnNovoLocatario);
        btnNovoLocatario.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), LocatarioFormActivity.class));
        });

        View btnVoltar = root.findViewById(R.id.btnVoltar);
        if (btnVoltar != null) btnVoltar.setVisibility(View.GONE);

        RecyclerView rv          = root.findViewById(R.id.rvLocatarios);
        SwipeRefreshLayout swipe = root.findViewById(R.id.swipeRefresh);
        View emptyView           = root.findViewById(R.id.tvEmpty);
        View progress            = root.findViewById(R.id.progressBar);

        LocatarioAdapter.LocatarioListener listener = new LocatarioAdapter.LocatarioListener() {
            @Override
            public void onItemClick(Locatario l) {
                Intent i = new Intent(requireContext(), LocatarioFormActivity.class);
                i.putExtra("locatario", l);
                i.putExtra("modo_visualizacao", true);
                startActivity(i);
            }
            @Override
            public void onEditClick(Locatario l) {
                Intent i = new Intent(requireContext(), LocatarioFormActivity.class);
                i.putExtra("locatario", l);
                startActivity(i);
            }
            @Override
            public void onDeleteClick(Locatario l) {
                viewModel.removerLocatario(l);
            }
        };

        adapter = new LocatarioAdapter(requireContext(), listener);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);


        viewModel = new ViewModelProvider(this).get(LocatarioViewModel.class);

        viewModel.getLocatarios().observe(getViewLifecycleOwner(), lista -> {
            adapter.updateList(lista);
            emptyView.setVisibility(lista == null || lista.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(),
                l -> { progress.setVisibility(l ? View.VISIBLE : View.GONE);
                    swipe.setRefreshing(l); });

        viewModel.getMensagemErro().observe(getViewLifecycleOwner(),
                m -> { if (m != null && !m.isEmpty())
                    Snackbar.make(root, m, Snackbar.LENGTH_LONG).show(); });

        swipe.setOnRefreshListener(viewModel::carregarLocatarios);

        viewModel.carregarLocatarios();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.carregarLocatarios();
    }
}
