package br.udesc.ddm.condominioapp.ui.apartamento;

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
import br.udesc.ddm.condominioapp.data.model.Apartamento;
import br.udesc.ddm.condominioapp.data.model.Locatario;
import br.udesc.ddm.condominioapp.ui.locatario.LocatarioListActivity;
import br.udesc.ddm.condominioapp.ui.locatario.LocatarioViewModel;

import java.util.List;

public class ApartamentoListActivity extends AppCompatActivity implements ApartamentoAdapter.ApartamentoListener {

    private ApartamentoViewModel viewModel;
    private LocatarioViewModel locatarioViewModel;
    private RecyclerView rvApartamentos;
    private ApartamentoAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvTitulo;
    private TextView tvBloco;
    private TextView tvEmpty;
    private Button btnNovoApartamento;
    private Button btnVoltar;
    private ProgressBar progressBar;

    private long blocoId;
    private String blocoNome;
    private long condominioId;
    private String condominioNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartamento_list);

        if (getIntent().hasExtra("bloco_id") && getIntent().hasExtra("bloco_nome") &&
                getIntent().hasExtra("condominio_id") && getIntent().hasExtra("condominio_nome")) {
            blocoId = getIntent().getLongExtra("bloco_id", -1);
            blocoNome = getIntent().getStringExtra("bloco_nome");
            condominioId = getIntent().getLongExtra("condominio_id", -1);
            condominioNome = getIntent().getStringExtra("condominio_nome");
        } else {
            Toast.makeText(this, "Erro: Bloco não informado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(ApartamentoViewModel.class);
        locatarioViewModel = new ViewModelProvider(this).get(LocatarioViewModel.class);

        initViews();

        setupRecyclerView();
        setupListeners();

        observeViewModel();

        viewModel.setBlocoAtual(blocoId, condominioId);

        viewModel.carregarLocatariosDisponiveis();
    }

    private void initViews() {
        rvApartamentos = findViewById(R.id.rvApartamentos);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvBloco = findViewById(R.id.tvBloco);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnNovoApartamento = findViewById(R.id.btnNovoApartamento);
        btnVoltar = findViewById(R.id.btnVoltar);
        progressBar = findViewById(R.id.progressBar);

        tvBloco.setText("Bloco: " + blocoNome + " | Condomínio: " + condominioNome);
    }

    private void setupRecyclerView() {
        adapter = new ApartamentoAdapter(this, this);
        rvApartamentos.setLayoutManager(new LinearLayoutManager(this));
        rvApartamentos.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.carregarApartamentos();
            viewModel.carregarLocatariosDisponiveis();
        });

        btnNovoApartamento.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApartamentoFormActivity.class);
            intent.putExtra("bloco_id", blocoId);
            intent.putExtra("bloco_nome", blocoNome);
            intent.putExtra("condominio_id", condominioId);
            intent.putExtra("condominio_nome", condominioNome);
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getApartamentos().observe(this, apartamentos -> {
            adapter.updateList(apartamentos);

            if (apartamentos == null || apartamentos.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvApartamentos.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvApartamentos.setVisibility(View.VISIBLE);
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

        viewModel.getLocatariosDisponiveis().observe(this, locatarios -> {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.carregarApartamentos();
        viewModel.carregarLocatariosDisponiveis();
    }

    @Override
    public void onLocatarioInfoClick(Apartamento apartamento) {
        if (apartamento.getLocatario() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Informações do Locatário");
            builder.setMessage(apartamento.getLocatario().toString());
            builder.setPositiveButton("OK", null);
            builder.show();
        }
    }

    @Override
    public void onEditClick(Apartamento apartamento) {
        Intent intent = new Intent(this, ApartamentoFormActivity.class);
        intent.putExtra("apartamento", apartamento);
        intent.putExtra("bloco_id", blocoId);
        intent.putExtra("bloco_nome", blocoNome);
        intent.putExtra("condominio_id", condominioId);
        intent.putExtra("condominio_nome", condominioNome);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Apartamento apartamento) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Deseja realmente excluir o apartamento \"" + apartamento.getNumero() + "\"?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    viewModel.removerApartamento(apartamento);
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onAssociarLocatarioClick(Apartamento apartamento) {
        List<Locatario> locatarios = viewModel.getLocatariosDisponiveis().getValue();

        if (locatarios == null || locatarios.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Nenhum locatário disponível")
                    .setMessage("Não há locatários disponíveis para associar. Deseja cadastrar um novo?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        Intent intent = new Intent(this, LocatarioListActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Não", null)
                    .show();
            return;
        }

        String[] nomes = new String[locatarios.size()];
        for (int i = 0; i < locatarios.size(); i++) {
            nomes[i] = locatarios.get(i).getNome();
        }

        new AlertDialog.Builder(this)
                .setTitle("Selecione um locatário")
                .setItems(nomes, (dialog, which) -> {
                    viewModel.associarLocatario(apartamento, locatarios.get(which));
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDesassociarLocatarioClick(Apartamento apartamento) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar remoção")
                .setMessage("Deseja realmente remover o locatário \"" +
                        apartamento.getLocatario().getNome() + "\" deste apartamento?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    viewModel.desassociarLocatario(apartamento);
                })
                .setNegativeButton("Não", null)
                .show();
    }
}