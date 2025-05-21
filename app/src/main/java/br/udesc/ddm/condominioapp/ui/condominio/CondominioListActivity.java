package br.udesc.ddm.condominioapp.ui.condominio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.ui.bloco.BlocoListActivity;

public class CondominioListActivity extends AppCompatActivity implements CondominioAdapter.CondominioListener {

    private CondominioViewModel viewModel;
    private RecyclerView rvCondominios;
    private CondominioAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvEmpty;
    private Button btnNovoCondominio;
    private Button btnVoltar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condominio_list);

        viewModel = new ViewModelProvider(this).get(CondominioViewModel.class);

        initViews();

        setupRecyclerView();
        setupListeners();

        observeViewModel();

        viewModel.carregarCondominios();
    }

    private void initViews() {
        rvCondominios = findViewById(R.id.rvCondominios);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnNovoCondominio = findViewById(R.id.btnNovoCondominio);
        btnVoltar = findViewById(R.id.btnVoltar);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new CondominioAdapter(this, this);
        rvCondominios.setLayoutManager(new LinearLayoutManager(this));
        rvCondominios.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.carregarCondominios();
        });

        btnNovoCondominio.setOnClickListener(v -> {
            Intent intent = new Intent(this, CondominioFormActivity.class);
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getCondominios().observe(this, condominios -> {
            adapter.updateList(condominios);

            if (condominios == null || condominios.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvCondominios.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvCondominios.setVisibility(View.VISIBLE);
            }

            swipeRefresh.setRefreshing(false);
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getMensagemErro().observe(this, mensagem -> {
            if (mensagem != null && !mensagem.isEmpty()) {
                Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.carregarCondominios();
    }


    @Override
    public void onVerBlocosClick(Condominio condominio) {
        Intent intent = new Intent(this, BlocoListActivity.class);
        intent.putExtra("condominio_id", condominio.getId());
        intent.putExtra("condominio_nome", condominio.getNome());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Condominio condominio) {
        Intent intent = new Intent(this, CondominioFormActivity.class);
        intent.putExtra("condominio", condominio);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Condominio condominio) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Deseja realmente excluir o condomínio \"" + condominio.getNome() + "\"?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    viewModel.removerCondominio(condominio);
                })
                .setNegativeButton("Não", null)
                .show();
    }
}