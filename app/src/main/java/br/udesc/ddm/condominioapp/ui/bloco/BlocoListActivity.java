package br.udesc.ddm.condominioapp.ui.bloco;

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
import br.udesc.ddm.condominioapp.data.model.Bloco;
import br.udesc.ddm.condominioapp.ui.apartamento.ApartamentoListActivity;

public class BlocoListActivity extends AppCompatActivity implements BlocoAdapter.BlocoListener {

    private BlocoViewModel viewModel;
    private RecyclerView rvBlocos;
    private BlocoAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvTitulo;
    private TextView tvCondominio;
    private TextView tvEmpty;
    private Button btnNovoBloco;
    private Button btnVoltar;
    private ProgressBar progressBar;

    private long condominioId;
    private String condominioNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloco_list);

        if (getIntent().hasExtra("condominio_id") && getIntent().hasExtra("condominio_nome")) {
            condominioId = getIntent().getLongExtra("condominio_id", -1);
            condominioNome = getIntent().getStringExtra("condominio_nome");
        } else {
            Toast.makeText(this, "Erro: Condomínio não informado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(BlocoViewModel.class);

        initViews();

        setupRecyclerView();
        setupListeners();

        observeViewModel();

        viewModel.setCondominioAtual(condominioId);
    }

    private void initViews() {
        rvBlocos = findViewById(R.id.rvBlocos);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvCondominio = findViewById(R.id.tvCondominio);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnNovoBloco = findViewById(R.id.btnNovoBloco);
        btnVoltar = findViewById(R.id.btnVoltar);
        progressBar = findViewById(R.id.progressBar);

        tvCondominio.setText("Condomínio: " + condominioNome);
    }

    private void setupRecyclerView() {
        adapter = new BlocoAdapter(this, this);
        rvBlocos.setLayoutManager(new LinearLayoutManager(this));
        rvBlocos.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.carregarBlocos();
        });

        btnNovoBloco.setOnClickListener(v -> {
            Intent intent = new Intent(this, BlocoFormActivity.class);
            intent.putExtra("condominio_id", condominioId);
            intent.putExtra("condominio_nome", condominioNome);
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getBlocos().observe(this, blocos -> {
            adapter.updateList(blocos);

            if (blocos == null || blocos.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvBlocos.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvBlocos.setVisibility(View.VISIBLE);
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
        viewModel.carregarBlocos();
    }


    @Override
    public void onVerApartamentosClick(Bloco bloco) {
        Intent intent = new Intent(this, ApartamentoListActivity.class);
        intent.putExtra("bloco_id", bloco.getId());
        intent.putExtra("bloco_nome", bloco.getNome());
        intent.putExtra("condominio_id", condominioId);
        intent.putExtra("condominio_nome", condominioNome);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Bloco bloco) {
        Intent intent = new Intent(this, BlocoFormActivity.class);
        intent.putExtra("bloco", bloco);
        intent.putExtra("condominio_id", condominioId);
        intent.putExtra("condominio_nome", condominioNome);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Bloco bloco) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Deseja realmente excluir o bloco \"" + bloco.getNome() +
                        "\"?\nTodos os apartamentos deste bloco também serão excluídos.")
                .setPositiveButton("Sim", (dialog, which) -> {
                    viewModel.removerBloco(bloco);
                })
                .setNegativeButton("Não", null)
                .show();
    }
}