package br.udesc.ddm.condominioapp.ui.apartamento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.model.Apartamento;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.utils.NumberFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ApartamentoFormActivity extends AppCompatActivity {

    private ApartamentoViewModel viewModel;

    private TextView tvTitulo;
    private TextView tvBloco;
    private TextInputLayout tilNumero;
    private TextInputLayout tilMetragem;
    private TextInputLayout tilVagasGaragem;
    private TextInputEditText etNumero;
    private TextInputEditText etMetragem;
    private TextInputEditText etVagasGaragem;
    private TextView tvValorAluguel;
    private Button btnSalvar;
    private Button btnCancelar;
    private ProgressBar progressBar;

    private Apartamento apartamentoAtual;
    private boolean modoEdicao = false;
    private long blocoId;
    private String blocoNome;
    private long condominioId;
    private String condominioNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartamento_form);

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

        initViews();

        setupListeners();

        viewModel.setBlocoAtual(blocoId, condominioId);

        if (getIntent().hasExtra("apartamento")) {
            modoEdicao = true;
            apartamentoAtual = getIntent().getParcelableExtra("apartamento");
            preencherCampos(apartamentoAtual);
            tvTitulo.setText("Editar Apartamento");
        } else {
            modoEdicao = false;
            apartamentoAtual = new Apartamento();
            apartamentoAtual.setBlocoId(blocoId);
            tvTitulo.setText("Cadastrar Apartamento");
        }

        observeViewModel();
    }

    private void initViews() {
        tvTitulo = findViewById(R.id.tvTitulo);
        tvBloco = findViewById(R.id.tvBloco);
        tilNumero = findViewById(R.id.tilNumero);
        tilMetragem = findViewById(R.id.tilMetragem);
        tilVagasGaragem = findViewById(R.id.tilVagasGaragem);
        etNumero = findViewById(R.id.etNumero);
        etMetragem = findViewById(R.id.etMetragem);
        etVagasGaragem = findViewById(R.id.etVagasGaragem);
        tvValorAluguel = findViewById(R.id.tvValorAluguel);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        progressBar = findViewById(R.id.progressBar);

        tvBloco.setText("Bloco: " + blocoNome + " | Condomínio: " + condominioNome);
    }

    private void setupListeners() {
        btnSalvar.setOnClickListener(v -> {
            if (validarCampos()) {
                salvarApartamento();
            }
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });

        TextInputEditText[] campos = {etMetragem, etVagasGaragem};
        for (TextInputEditText campo : campos) {
            campo.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    calcularAluguel();
                }
            });
        }
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

        viewModel.getCondominioAtual().observe(this, condominio -> {
            if (condominio != null) {
                calcularAluguel();
            }
        });
    }

    private void preencherCampos(Apartamento apartamento) {
        etNumero.setText(apartamento.getNumero());
        etMetragem.setText(String.valueOf(apartamento.getMetragem()));
        etVagasGaragem.setText(String.valueOf(apartamento.getVagasDeGaragem()));

        atualizarValorAluguel(apartamento.getValorAluguel());
    }

    private boolean validarCampos() {
        boolean isValid = true;

        String numero = etNumero.getText().toString().trim();
        if (numero.isEmpty()) {
            tilNumero.setError("Número é obrigatório");
            isValid = false;
        } else {
            tilNumero.setError(null);
        }

        String metragemStr = etMetragem.getText().toString().trim();
        if (metragemStr.isEmpty()) {
            tilMetragem.setError("Metragem é obrigatória");
            isValid = false;
        } else {
            double metragem = NumberFormatter.parseDouble(metragemStr);
            if (metragem <= 0) {
                tilMetragem.setError("Metragem deve ser maior que zero");
                isValid = false;
            } else {
                tilMetragem.setError(null);
            }
        }

        return isValid;
    }

    private void calcularAluguel() {
        Condominio condominio = viewModel.getCondominioAtual().getValue();
        if (condominio == null) {
            return;
        }

        double metragem = NumberFormatter.parseDouble(etMetragem.getText().toString());
        int vagasGaragem = NumberFormatter.parseInt(etVagasGaragem.getText().toString());

        double taxaMensal = condominio.getTaxaMensalCondominio();
        double valorMetro = condominio.getFatorMultiplicadorDeMetragem();
        double valorGaragem = condominio.getValorVagaGaragem();

        double valorAluguel = taxaMensal + (metragem * valorMetro) + (vagasGaragem * valorGaragem);

        atualizarValorAluguel(valorAluguel);
    }

    private void atualizarValorAluguel(double valor) {
        tvValorAluguel.setText("Valor do Aluguel: " + NumberFormatter.formatCurrency(valor));
    }

    private void salvarApartamento() {
        String numero = etNumero.getText().toString().trim();
        double metragem = NumberFormatter.parseDouble(etMetragem.getText().toString());
        int vagasGaragem = NumberFormatter.parseInt(etVagasGaragem.getText().toString());

        apartamentoAtual.setNumero(numero);
        apartamentoAtual.setMetragem(metragem);
        apartamentoAtual.setVagasDeGaragem(vagasGaragem);

        Condominio condominio = viewModel.getCondominioAtual().getValue();
        if (condominio != null) {
            double valorAluguel = apartamentoAtual.calcularAluguel(condominio);
            apartamentoAtual.setValorAluguel(valorAluguel);
        }

        viewModel.salvarApartamento(apartamentoAtual);

        Toast.makeText(this, modoEdicao ? "Apartamento atualizado com sucesso!" : "Apartamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}