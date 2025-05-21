package br.udesc.ddm.condominioapp.ui.locatario;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.udesc.ddm.condominioapp.data.model.Locatario;
import br.udesc.ddm.condominioapp.data.repository.LocatarioRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocatarioViewModel extends AndroidViewModel {
    private final LocatarioRepository repository;
    private final ExecutorService executorService;

    private final MutableLiveData<List<Locatario>> locatarios = new MutableLiveData<>();
    private final MutableLiveData<Locatario> locatarioSelecionado = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    public LocatarioViewModel(@NonNull Application application) {
        super(application);
        repository = new LocatarioRepository(application);
        executorService = Executors.newFixedThreadPool(4);

        carregarLocatarios();
    }

    public void carregarLocatarios() {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                List<Locatario> lista = repository.listarTodos();
                locatarios.postValue(lista);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar locatários: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void carregarLocatariosSemApartamento() {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                List<Locatario> lista = repository.listarSemApartamento();
                locatarios.postValue(lista);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar locatários: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void salvarLocatario(Locatario locatario) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                if (locatario.getId() > 0) {
                    repository.atualizar(locatario);
                } else {
                    repository.inserir(locatario);
                }
                carregarLocatarios();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao salvar locatário: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void removerLocatario(Locatario locatario) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                repository.remover(locatario.getId());
                carregarLocatarios();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao remover locatário: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void limparSelecao() {
        locatarioSelecionado.setValue(null);
    }

    public void buscarLocatarioPorId(long id) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                Locatario locatario = repository.buscarPorId(id);
                locatarioSelecionado.postValue(locatario);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao buscar locatário: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void associarApartamento(Locatario locatario, long apartamentoId) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                repository.associarApartamento(locatario.getId(), apartamentoId);
                locatario.setApartamentoId(apartamentoId);
                locatarioSelecionado.postValue(locatario);
                carregarLocatarios();
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao associar apartamento: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void desassociarApartamento(Locatario locatario) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                repository.desassociarApartamento(locatario.getId());
                locatario.setApartamentoId(-1);
                locatarioSelecionado.postValue(locatario);
                carregarLocatarios();
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao desassociar apartamento: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Locatario>> getLocatarios() {
        return locatarios;
    }

    public LiveData<Locatario> getLocatarioSelecionado() {
        return locatarioSelecionado;
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