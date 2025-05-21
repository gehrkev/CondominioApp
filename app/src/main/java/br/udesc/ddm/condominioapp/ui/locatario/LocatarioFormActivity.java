package br.udesc.ddm.condominioapp.ui.locatario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Locatario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LocatarioFormActivity extends AppCompatActivity {

    private LocatarioViewModel viewModel;

    private TextView tvTitulo;
    private TextInputLayout tilNome;
    private TextInputLayout tilCpf;
    private TextInputLayout tilTelefone;
    private TextInputLayout tilEmail;
    private TextInputEditText etNome;
    private TextInputEditText etCpf;
    private TextInputEditText etTelefone;
    private TextInputEditText etEmail;
    private TextView tvApartamento;
    private Button btnSalvar;
    private Button btnCancelar;
    private ProgressBar progressBar;

    private Locatario locatarioAtual;
    private boolean modoEdicao = false;
    private boolean modoVisualizacao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatario_form);

        viewModel = new ViewModelProvider(this).get(LocatarioViewModel.class);

        initViews();

        if (getIntent().hasExtra("locatario")) {
            locatarioAtual = getIntent().getParcelableExtra("locatario");

            if (getIntent().getBooleanExtra("modo_visualizacao", false)) {
                modoVisualizacao = true;
                modoEdicao = false;
                tvTitulo.setText("Detalhes do Locatário");
                desabilitarEdicao();
            } else {
                modoVisualizacao = false;
                modoEdicao = true;
                tvTitulo.setText("Editar Locatário");
            }

            preencherCampos(locatarioAtual);
        } else {
            modoVisualizacao = false;
            modoEdicao = false;
            locatarioAtual = new Locatario();
            tvTitulo.setText("Cadastrar Locatário");
            tvApartamento.setText("Apartamento associado: Nenhum");
        }

        setupListeners();

        observeViewModel();
    }

    private void initViews() {
        tvTitulo = findViewById(R.id.tvTitulo);
        tilNome = findViewById(R.id.tilNome);
        tilCpf = findViewById(R.id.tilCpf);
        tilTelefone = findViewById(R.id.tilTelefone);
        tilEmail = findViewById(R.id.tilEmail);
        etNome = findViewById(R.id.etNome);
        etCpf = findViewById(R.id.etCpf);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        tvApartamento = findViewById(R.id.tvApartamento);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnSalvar.setOnClickListener(v -> {
            if (validarCampos()) {
                salvarLocatario();
            }
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

            if (!modoVisualizacao) {
                btnSalvar.setEnabled(!isLoading);
                btnCancelar.setEnabled(!isLoading);
            }
        });

        viewModel.getMensagemErro().observe(this, mensagem -> {
            if (mensagem != null && !mensagem.isEmpty()) {
                Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void preencherCampos(Locatario locatario) {
        etNome.setText(locatario.getNome());
        etCpf.setText(locatario.getCpf());
        etTelefone.setText(locatario.getTelefone());
        etEmail.setText(locatario.getEmail());

        if (locatario.temApartamento()) {
            tvApartamento.setText("Apartamento associado: Sim (ID: " + locatario.getApartamentoId() + ")");
        } else {
            tvApartamento.setText("Apartamento associado: Nenhum");
        }
    }

    private void desabilitarEdicao() {
        etNome.setEnabled(false);
        etCpf.setEnabled(false);
        etTelefone.setEnabled(false);
        etEmail.setEnabled(false);
        btnSalvar.setVisibility(View.GONE);
        btnCancelar.setText("Voltar");
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

        String cpf = etCpf.getText().toString().trim();
        if (cpf.isEmpty()) {
            tilCpf.setError("CPF é obrigatório");
            isValid = false;
        } else {
            tilCpf.setError(null);
        }

        String telefone = etTelefone.getText().toString().trim();
        if (telefone.isEmpty()) {
            tilTelefone.setError("Telefone é obrigatório");
            isValid = false;
        } else {
            tilTelefone.setError(null);
        }

        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            tilEmail.setError("E-mail é obrigatório");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        return isValid;
    }

    private void salvarLocatario() {
        String nome = etNome.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        locatarioAtual.setNome(nome);
        locatarioAtual.setCpf(cpf);
        locatarioAtual.setTelefone(telefone);
        locatarioAtual.setEmail(email);

        viewModel.salvarLocatario(locatarioAtual);

        Toast.makeText(this, modoEdicao ? "Locatário atualizado com sucesso!" : "Locatário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}