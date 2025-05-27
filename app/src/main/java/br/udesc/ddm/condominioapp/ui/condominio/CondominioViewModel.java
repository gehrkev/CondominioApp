package br.udesc.ddm.condominioapp.ui.condominio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.udesc.ddm.condominioapp.data.api.RetrofitClient;
import br.udesc.ddm.condominioapp.data.model.CepResponse;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.data.repository.CondominioRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CondominioViewModel extends AndroidViewModel {
    private final CondominioRepository repository;
    private final ExecutorService executorService;

    private final MutableLiveData<List<Condominio>> condominios = new MutableLiveData<>();
    private final MutableLiveData<Condominio> condominioSelecionado = new MutableLiveData<>();
    private final MutableLiveData<CepResponse> cepConsultado = new MutableLiveData<>();
    private final MutableLiveData<String> cepErro = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    public CondominioViewModel(@NonNull Application application) {
        super(application);
        repository = new CondominioRepository(application);
        executorService = Executors.newFixedThreadPool(4);

        carregarCondominios();
    }

    public void carregarCondominios() {
        isLoading.postValue(true);
        executorService.execute(() -> {
            try {
                List<Condominio> lista = repository.listarTodos();
                condominios.postValue(lista);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar condomínios: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void consultarCep(String cep) {
        cepErro.setValue(null);
        cepConsultado.setValue(null);

        Call<CepResponse> call = RetrofitClient.getCepApiService().consultarCep(cep);
        call.enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CepResponse cepResponse = response.body();

                    if (cepResponse.isErro()) {
                        cepErro.postValue("CEP não encontrado");
                    } else {
                        cepConsultado.postValue(cepResponse);
                    }
                } else {
                    cepErro.postValue("Erro ao consultar CEP");
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                cepErro.postValue("Erro de conexão. Verifique sua internet.");
            }
        });
    }

    public void selecionarCondominio(Condominio condominio) {
        condominioSelecionado.setValue(condominio);
    }

    public void salvarCondominio(Condominio condominio) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                if (condominio.getId() > 0) {
                    repository.atualizar(condominio);
                } else {
                    repository.inserir(condominio);
                }
                carregarCondominios();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao salvar condomínio: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void removerCondominio(Condominio condominio) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                repository.remover(condominio.getId());
                carregarCondominios();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao remover condomínio: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void limparSelecao() {
        condominioSelecionado.setValue(null);
    }

    public void buscarCondominioPorId(long id) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                Condominio condominio = repository.buscarPorId(id);
                condominioSelecionado.postValue(condominio);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao buscar condomínio: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Condominio>> getCondominios() {
        return condominios;
    }

    public LiveData<Condominio> getCondominioSelecionado() {
        return condominioSelecionado;
    }

    public LiveData<CepResponse> getCepConsultado() {
        return cepConsultado;
    }

    public LiveData<String> getCepErro() {
        return cepErro;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMensagemErro() {
        return mensagemErro;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}