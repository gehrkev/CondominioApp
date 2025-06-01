package br.udesc.ddm.condominioapp.ui.condominio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.api.CepResponse;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.utils.CepUtils;
import br.udesc.ddm.condominioapp.utils.NumberFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CondominioFormActivity extends AppCompatActivity {

    private CondominioViewModel viewModel;

    private TextView tvTitulo;
    private TextInputLayout tilNome;
    private TextInputLayout tilCep;
    private TextInputLayout tilLogradouro;
    private TextInputLayout tilComplemento;
    private TextInputLayout tilBairro;
    private TextInputLayout tilLocalidade;
    private TextInputLayout tilUf;
    private TextInputLayout tilTaxaMensal;
    private TextInputLayout tilFatorMetragem;
    private TextInputLayout tilValorGaragem;
    private TextInputEditText etNome;
    private TextInputEditText etCep;
    private TextInputEditText etLogradouro;
    private TextInputEditText etComplemento;
    private TextInputEditText etBairro;
    private TextInputEditText etLocalidade;
    private TextInputEditText etUf;
    private TextInputEditText etTaxaMensal;
    private TextInputEditText etFatorMetragem;
    private TextInputEditText etValorGaragem;
    private Button btnSalvar;
    private Button btnCancelar;
    private ProgressBar progressBar;
    private ProgressBar progressBarCep;

    private Condominio condominioAtual;
    private boolean modoEdicao = false;
    private boolean consultandoCep = false;

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
        tilCep = findViewById(R.id.tilCep);
        tilLogradouro = findViewById(R.id.tilLogradouro);
        tilComplemento = findViewById(R.id.tilComplemento);
        tilBairro = findViewById(R.id.tilBairro);
        tilLocalidade = findViewById(R.id.tilLocalidade);
        tilUf = findViewById(R.id.tilUf);
        tilTaxaMensal = findViewById(R.id.tilTaxaMensal);
        tilFatorMetragem = findViewById(R.id.tilFatorMetragem);
        tilValorGaragem = findViewById(R.id.tilValorGaragem);
        etNome = findViewById(R.id.etNome);
        etCep = findViewById(R.id.etCep);
        etLogradouro = findViewById(R.id.etLogradouro);
        etComplemento = findViewById(R.id.etComplemento);
        etBairro = findViewById(R.id.etBairro);
        etLocalidade = findViewById(R.id.etLocalidade);
        etUf = findViewById(R.id.etUf);
        etTaxaMensal = findViewById(R.id.etTaxaMensal);
        etFatorMetragem = findViewById(R.id.etFatorMetragem);
        etValorGaragem = findViewById(R.id.etValorGaragem);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        progressBar = findViewById(R.id.progressBar);
        progressBarCep = findViewById(R.id.progressBarCep);
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

        // Autocomplete do CEP
        etCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String cep = s.toString();
                String cepLimpo = CepUtils.limparCep(cep);

                if (cepLimpo.length() == 8 && !CepUtils.isFormatted(cep)) {
                    String cepFormatado = CepUtils.formatarCep(cepLimpo);
                    etCep.removeTextChangedListener(this);
                    etCep.setText(cepFormatado);
                    etCep.setSelection(cepFormatado.length());
                    etCep.addTextChangedListener(this);
                }

                if (cepLimpo.length() == 8 && !consultandoCep) {
                    String cepAtual = condominioAtual.getCep();
                    if (!modoEdicao || !cepLimpo.equals(CepUtils.limparCep(cepAtual))) {
                        consultarCep(cepLimpo);
                    }
                }

                if (tilCep.getError() != null) {
                    tilCep.setError(null);
                    tilCep.setHelperText("Digite o CEP para preenchimento automático");
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSalvar.setEnabled(!isLoading && !consultandoCep);
            btnCancelar.setEnabled(!isLoading);
        });

        viewModel.getMensagemErro().observe(this, mensagem -> {
            if (mensagem != null && !mensagem.isEmpty()) {
                Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getCepConsultado().observe(this, cepResponse -> {
            if (cepResponse != null && cepResponse.isValid()) {
                preencherEnderecoComCep(cepResponse);
            }
            consultandoCep = false;
            progressBarCep.setVisibility(View.GONE);
            btnSalvar.setEnabled(!viewModel.getIsLoading().getValue());
        });

        viewModel.getCepErro().observe(this, erro -> {
            if (erro != null && !erro.isEmpty()) {
                tilCep.setError(erro);
                tilCep.setHelperText(null);
            }
            consultandoCep = false;
            progressBarCep.setVisibility(View.GONE);
            btnSalvar.setEnabled(!viewModel.getIsLoading().getValue());
        });
    }

    private void consultarCep(String cep) {
        consultandoCep = true;
        progressBarCep.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(false);
        tilCep.setError(null);
        tilCep.setHelperText("Consultando CEP...");

        viewModel.consultarCep(cep);
    }

    private void preencherEnderecoComCep(CepResponse cepResponse) {
        if (cepResponse.getLogradouro() != null && !cepResponse.getLogradouro().trim().isEmpty()) {
            etLogradouro.setText(cepResponse.getLogradouro());
        }

        if (cepResponse.getComplemento() != null && !cepResponse.getComplemento().trim().isEmpty()) {
            etComplemento.setText(cepResponse.getComplemento());
        }

        if (cepResponse.getBairro() != null && !cepResponse.getBairro().trim().isEmpty()) {
            etBairro.setText(cepResponse.getBairro());
        }

        if (cepResponse.getLocalidade() != null && !cepResponse.getLocalidade().trim().isEmpty()) {
            etLocalidade.setText(cepResponse.getLocalidade());
        }

        if (cepResponse.getUf() != null && !cepResponse.getUf().trim().isEmpty()) {
            etUf.setText(cepResponse.getUf().toUpperCase());
        }

        tilCep.setHelperText("✓ Endereço preenchido automaticamente");

        tilCep.postDelayed(() -> {
            tilCep.setHelperText("Digite o CEP para preenchimento automático");
        }, 3000);
    }

    private void preencherCampos(Condominio condominio) {
        etNome.setText(condominio.getNome());
        etCep.setText(condominio.getCep());
        etLogradouro.setText(condominio.getLogradouro());
        etComplemento.setText(condominio.getComplemento());
        etBairro.setText(condominio.getBairro());
        etLocalidade.setText(condominio.getLocalidade());
        etUf.setText(condominio.getUf());
        etTaxaMensal.setText(formatarComDuasCasas(condominio.getTaxaMensalCondominio()));
        etFatorMetragem.setText(formatarComDuasCasas(condominio.getFatorMultiplicadorDeMetragem()));
        etValorGaragem.setText(formatarComDuasCasas(condominio.getValorVagaGaragem()));
    }

    private boolean validarCampos() {
        boolean isValid = true;

        String nome = etNome.getText().toString().trim();
        if (nome.isEmpty()) {
            tilNome.setError("Nome é obrigatório!");
            isValid = false;
        } else {
            tilNome.setError(null);
        }

        String cep = etCep.getText().toString().trim();
        if (!cep.isEmpty() && !CepUtils.isValidCep(cep)) {
            tilCep.setError("CEP deve ter 8 dígitos!");
            isValid = false;
        } else {
            tilCep.setError(null);
        }

        String uf = etUf.getText().toString().trim();
        if ((!uf.isEmpty() && uf.length() != 2) || uf.isEmpty()) {
            tilUf.setError("UF deve ter 2 caracteres!");
            isValid = false;
        } else {
            tilUf.setError(null);
        }

        String cidade = etLocalidade.getText().toString().trim();
        if (cidade.isEmpty()){
            tilLocalidade.setError("Cidade é obrigatória!");
            isValid = false;
        } else {
            tilLocalidade.setError(null);
        }

        String logradouro = etLogradouro.getText().toString().trim();
        if(logradouro.isEmpty()){
            tilLogradouro.setError(("Logradouro é obrigatório!"));
            isValid = false;
        } else {
            tilLogradouro.setError(null);
        }

        return isValid;
    }

    private void salvarCondominio() {
        String nome = etNome.getText().toString().trim();
        String cep = CepUtils.limparCep(etCep.getText().toString());
        String logradouro = etLogradouro.getText().toString().trim();
        String complemento = etComplemento.getText().toString().trim();
        String bairro = etBairro.getText().toString().trim();
        String localidade = etLocalidade.getText().toString().trim();
        String uf = etUf.getText().toString().trim().toUpperCase();
        double taxaMensal = NumberFormatter.parseDouble(etTaxaMensal.getText().toString());
        double fatorMetragem = NumberFormatter.parseDouble(etFatorMetragem.getText().toString());
        double valorGaragem = NumberFormatter.parseDouble(etValorGaragem.getText().toString());

        condominioAtual.setNome(nome);
        condominioAtual.setCep(cep);
        condominioAtual.setLogradouro(logradouro);
        condominioAtual.setComplemento(complemento);
        condominioAtual.setBairro(bairro);
        condominioAtual.setLocalidade(localidade);
        condominioAtual.setUf(uf);
        condominioAtual.setTaxaMensalCondominio(taxaMensal);
        condominioAtual.setFatorMultiplicadorDeMetragem(fatorMetragem);
        condominioAtual.setValorVagaGaragem(valorGaragem);

        viewModel.salvarCondominio(condominioAtual);

        finish();
    }

    public static String formatarComDuasCasas(double valor) {
        return String.format("%.2f", valor);
    }
}