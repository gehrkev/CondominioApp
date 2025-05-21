package br.udesc.ddm.condominioapp.ui.bloco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Bloco;
import br.udesc.ddm.condominioapp.utils.NumberFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BlocoFormActivity extends AppCompatActivity {

    private BlocoViewModel viewModel;

    private TextView tvTitulo;
    private TextView tvCondominio;
    private TextInputLayout tilNome;
    private TextInputLayout tilAndares;
    private TextInputLayout tilVagasGaragem;
    private TextInputEditText etNome;
    private TextInputEditText etAndares;
    private TextInputEditText etVagasGaragem;
    private Button btnSalvar;
    private Button btnCancelar;
    private ProgressBar progressBar;

    private Bloco blocoAtual;
    private boolean modoEdicao = false;
    private long condominioId;
    private String condominioNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloco_form);

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

        setupListeners();

        viewModel.setCondominioAtual(condominioId);

        if (getIntent().hasExtra("bloco")) {
            modoEdicao = true;
            blocoAtual = getIntent().getParcelableExtra("bloco");
            preencherCampos(blocoAtual);
            tvTitulo.setText("Editar Bloco");
        } else {
            modoEdicao = false;
            blocoAtual = new Bloco();
            blocoAtual.setCondominioId(condominioId);
            tvTitulo.setText("Cadastrar Bloco");
        }

        observeViewModel();
    }

    private void initViews() {
        tvTitulo = findViewById(R.id.tvTitulo);
        tvCondominio = findViewById(R.id.tvCondominio);
        tilNome = findViewById(R.id.tilNome);
        tilAndares = findViewById(R.id.tilAndares);
        tilVagasGaragem = findViewById(R.id.tilVagasGaragem);
        etNome = findViewById(R.id.etNome);
        etAndares = findViewById(R.id.etAndares);
        etVagasGaragem = findViewById(R.id.etVagasGaragem);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        progressBar = findViewById(R.id.progressBar);

        tvCondominio.setText("Condomínio: " + condominioNome);
    }

    private void setupListeners() {
        btnSalvar.setOnClickListener(v -> {
            if (validarCampos()) {
                salvarBloco();
            }
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSalvar.setEnabled(!isLoading);
            btnCancelar.setEnabled(!isLoading);
        });

        viewModel.getMensagemErro().observe(this, mensagem -> {
            if (mensagem != null && !mensagem.isEmpty()) {
                Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void preencherCampos(Bloco bloco) {
        etNome.setText(bloco.getNome());
        etAndares.setText(String.valueOf(bloco.getAndares()));
        etVagasGaragem.setText(String.valueOf(bloco.getVagasDeGaragem()));
    }

    private boolean validarCampos() {
        boolean isValid = true;

        String nome = etNome.getText().toString().trim();
        if (nome.isEmpty()) {
            tilNome.setError("Nome é obrigatório");
            isValid = false;
        } else {
            tilNome.setError(null);
        }

        String andaresStr = etAndares.getText().toString().trim();
        if (andaresStr.isEmpty()) {
            tilAndares.setError("Número de andares é obrigatório");
            isValid = false;
        } else {
            int andares = NumberFormatter.parseInt(andaresStr);
            if (andares <= 0) {
                tilAndares.setError("Número de andares deve ser maior que zero");
                isValid = false;
            } else {
                tilAndares.setError(null);
            }
        }

        String vagasStr = etVagasGaragem.getText().toString().trim();
        if (vagasStr.isEmpty()) {
            tilVagasGaragem.setError("Número de vagas é obrigatório");
            isValid = false;
        } else {
            tilVagasGaragem.setError(null);
        }

        return isValid;
    }

    private void salvarBloco() {
        String nome = etNome.getText().toString().trim();
        int andares = NumberFormatter.parseInt(etAndares.getText().toString());
        int vagasGaragem = NumberFormatter.parseInt(etVagasGaragem.getText().toString());

        blocoAtual.setNome(nome);
        blocoAtual.setAndares(andares);
        blocoAtual.setVagasDeGaragem(vagasGaragem);

        viewModel.salvarBloco(blocoAtual);

        Toast.makeText(this, modoEdicao ? "Bloco atualizado com sucesso!" : "Bloco cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}