package br.udesc.ddm.condominioapp.ui.locatario;

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
import br.udesc.ddm.condominioapp.data.model.Locatario;

public class LocatarioListActivity extends AppCompatActivity implements LocatarioAdapter.LocatarioListener {

    private LocatarioViewModel viewModel;
    private RecyclerView rvLocatarios;
    private LocatarioAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvEmpty;
    private Button btnNovoLocatario;
    private Button btnVoltar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatario_list);

        viewModel = new ViewModelProvider(this).get(LocatarioViewModel.class);

        initViews();

        setupRecyclerView();
        setupListeners();

        observeViewModel();

        viewModel.carregarLocatarios();
    }

    private void initViews() {
        rvLocatarios = findViewById(R.id.rvLocatarios);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnNovoLocatario = findViewById(R.id.btnNovoLocatario);
        btnVoltar = findViewById(R.id.btnVoltar);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new LocatarioAdapter(this, this);
        rvLocatarios.setLayoutManager(new LinearLayoutManager(this));
        rvLocatarios.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.carregarLocatarios();
        });

        btnNovoLocatario.setOnClickListener(v -> {
            Intent intent = new Intent(this, LocatarioFormActivity.class);
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getLocatarios().observe(this, locatarios -> {
            adapter.updateList(locatarios);

            if (locatarios == null || locatarios.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvLocatarios.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvLocatarios.setVisibility(View.VISIBLE);
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
        viewModel.carregarLocatarios();
    }


    @Override
    public void onItemClick(Locatario locatario) {
        Intent intent = new Intent(this, LocatarioFormActivity.class);
        intent.putExtra("locatario", locatario);
        intent.putExtra("modo_visualizacao", true);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Locatario locatario) {
        Intent intent = new Intent(this, LocatarioFormActivity.class);
        intent.putExtra("locatario", locatario);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Locatario locatario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar exclusão");

        if (locatario.temApartamento()) {
            builder.setMessage("O locatário \"" + locatario.getNome() +
                    "\" está associado a um apartamento. Deseja realmente excluí-lo?");
        } else {
            builder.setMessage("Deseja realmente excluir o locatário \"" + locatario.getNome() + "\"?");
        }

        builder.setPositiveButton("Sim", (dialog, which) -> {
            viewModel.removerLocatario(locatario);
        });
        builder.setNegativeButton("Não", null);
        builder.show();
    }
}