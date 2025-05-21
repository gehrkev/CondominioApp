package br.udesc.ddm.condominioapp.ui.condominio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.utils.NumberFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CondominioFormActivity extends AppCompatActivity {

    private CondominioViewModel viewModel;

    private TextView tvTitulo;
    private TextInputLayout tilNome;
    private TextInputLayout tilEndereco;
    private TextInputLayout tilTaxaMensal;
    private TextInputLayout tilFatorMetragem;
    private TextInputLayout tilValorGaragem;
    private TextInputEditText etNome;
    private TextInputEditText etEndereco;
    private TextInputEditText etTaxaMensal;
    private TextInputEditText etFatorMetragem;
    private TextInputEditText etValorGaragem;
    private Button btnSalvar;
    private Button btnCancelar;
    private ProgressBar progressBar;

    private Condominio condominioAtual;
    private boolean modoEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condominio_form);

        viewModel = new ViewModelProvider(this).get(CondominioViewModel.class);

        initViews();

        setupListeners();

        if (getIntent().hasExtra("condominio")) {
            modoEdicao = true;
            condominioAtual = getIntent().getParcelableExtra("condominio");
            preencherCampos(condominioAtual);
            tvTitulo.setText("Editar Condomínio");
        } else {
            modoEdicao = false;
            condominioAtual = new Condominio();
            tvTitulo.setText("Cadastrar Condomínio");
        }

        observeViewModel();
    }

    private void initViews() {
        tvTitulo = findViewById(R.id.tvTitulo);
        tilNome = findViewById(R.id.tilNome);
        tilEndereco = findViewById(R.id.tilEndereco);
        tilTaxaMensal = findViewById(R.id.tilTaxaMensal);
        tilFatorMetragem = findViewById(R.id.tilFatorMetragem);
        tilValorGaragem = findViewById(R.id.tilValorGaragem);
        etNome = findViewById(R.id.etNome);
        etEndereco = findViewById(R.id.etEndereco);
        etTaxaMensal = findViewById(R.id.etTaxaMensal);
        etFatorMetragem = findViewById(R.id.etFatorMetragem);
        etValorGaragem = findViewById(R.id.etValorGaragem);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnSalvar.setOnClickListener(v -> {
            if (validarCampos()) {
                salvarCondominio();
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

    private void preencherCampos(Condominio condominio) {
        etNome.setText(condominio.getNome());
        etEndereco.setText(condominio.getEndereco());
        etTaxaMensal.setText(String.valueOf(condominio.getTaxaMensalCondominio()));
        etFatorMetragem.setText(String.valueOf(condominio.getFatorMultiplicadorDeMetragem()));
        etValorGaragem.setText(String.valueOf(condominio.getValorVagaGaragem()));
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

        String endereco = etEndereco.getText().toString().trim();
        if (endereco.isEmpty()) {
            tilEndereco.setError("Endereço é obrigatório");
            isValid = false;
        } else {
            tilEndereco.setError(null);
        }

        return isValid;
    }

    private void salvarCondominio() {
        String nome = etNome.getText().toString().trim();
        String endereco = etEndereco.getText().toString().trim();
        double taxaMensal = NumberFormatter.parseDouble(etTaxaMensal.getText().toString());
        double fatorMetragem = NumberFormatter.parseDouble(etFatorMetragem.getText().toString());
        double valorGaragem = NumberFormatter.parseDouble(etValorGaragem.getText().toString());

        condominioAtual.setNome(nome);
        condominioAtual.setEndereco(endereco);
        condominioAtual.setTaxaMensalCondominio(taxaMensal);
        condominioAtual.setFatorMultiplicadorDeMetragem(fatorMetragem);
        condominioAtual.setValorVagaGaragem(valorGaragem);

        viewModel.salvarCondominio(condominioAtual);

        Toast.makeText(this, modoEdicao ? "Condomínio atualizado com sucesso!" : "Condomínio cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}