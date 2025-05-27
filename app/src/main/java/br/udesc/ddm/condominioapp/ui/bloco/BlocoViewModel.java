package br.udesc.ddm.condominioapp.ui.bloco;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.udesc.ddm.condominioapp.data.model.Bloco;
import br.udesc.ddm.condominioapp.data.repository.BlocoRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlocoViewModel extends AndroidViewModel {
    private final BlocoRepository repository;
    private final ExecutorService executorService;

    private final MutableLiveData<List<Bloco>> blocos = new MutableLiveData<>();
    private final MutableLiveData<Bloco> blocoSelecionado = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    private long condominioIdAtual = -1;

    public BlocoViewModel(@NonNull Application application) {
        super(application);
        repository = new BlocoRepository(application);
        executorService = Executors.newFixedThreadPool(4);
    }

    public void setCondominioAtual(long condominioId) {
        if (condominioId != condominioIdAtual) {
            condominioIdAtual = condominioId;
            carregarBlocos();
        }
    }

    public void carregarBlocos() {
        if (condominioIdAtual <= 0) {
            blocos.postValue(null);  
            return;
        }

        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                List<Bloco> lista = repository.listarPorCondominio(condominioIdAtual);
                blocos.postValue(lista);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar blocos: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void selecionarBloco(Bloco bloco) {
        blocoSelecionado.setValue(bloco);
    }

    public void salvarBloco(Bloco bloco) {
        if (condominioIdAtual <= 0) {
            mensagemErro.postValue("Nenhum condomínio selecionado");  
            return;
        }

        bloco.setCondominioId(condominioIdAtual);

        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                if (bloco.getId() > 0) {
                    repository.atualizar(bloco);
                } else {
                    repository.inserir(bloco);
                }
                carregarBlocos();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao salvar bloco: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void removerBloco(Bloco bloco) {
        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                repository.remover(bloco.getId());
                carregarBlocos();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao remover bloco: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void limparSelecao() {
        blocoSelecionado.setValue(null);
    }

    public void buscarBlocoPorId(long id) {
        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                Bloco bloco = repository.buscarPorId(id);
                blocoSelecionado.postValue(bloco);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao buscar bloco: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Bloco>> getBlocos() {
        return blocos;
    }

    public LiveData<Bloco> getBlocoSelecionado() {
        return blocoSelecionado;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMensagemErro() {
        return mensagemErro;
    }

    public long getCondominioIdAtual() {
        return condominioIdAtual;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}