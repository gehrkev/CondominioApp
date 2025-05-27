package br.udesc.ddm.condominioapp.ui.apartamento;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.udesc.ddm.condominioapp.data.model.Apartamento;
import br.udesc.ddm.condominioapp.data.model.Condominio;
import br.udesc.ddm.condominioapp.data.model.Locatario;
import br.udesc.ddm.condominioapp.data.repository.ApartamentoRepository;
import br.udesc.ddm.condominioapp.data.repository.CondominioRepository;
import br.udesc.ddm.condominioapp.data.repository.LocatarioRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApartamentoViewModel extends AndroidViewModel {
    private final ApartamentoRepository repository;
    private final LocatarioRepository locatarioRepository;
    private final CondominioRepository condominioRepository;
    private final ExecutorService executorService;

    private final MutableLiveData<List<Apartamento>> apartamentos = new MutableLiveData<>();
    private final MutableLiveData<Apartamento> apartamentoSelecionado = new MutableLiveData<>();
    private final MutableLiveData<List<Locatario>> locatariosDisponiveis = new MutableLiveData<>();
    private final MutableLiveData<Condominio> condominioAtual = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    private long blocoIdAtual = -1;

    public ApartamentoViewModel(@NonNull Application application) {
        super(application);
        repository = new ApartamentoRepository(application);
        locatarioRepository = new LocatarioRepository(application);
        condominioRepository = new CondominioRepository(application);
        executorService = Executors.newFixedThreadPool(4);
    }

    public void setBlocoAtual(long blocoId, long condominioId) {
        if (blocoId != blocoIdAtual) {
            blocoIdAtual = blocoId;
            carregarApartamentos();
            if (condominioId > 0) {
                carregarCondominio(condominioId);
            }
        }
    }

    private void carregarCondominio(long condominioId) {
        executorService.execute(() -> {
            try {
                Condominio condominio = condominioRepository.buscarPorId(condominioId);
                condominioAtual.postValue(condominio);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar condomínio: " + e.getMessage());
            }
        });
    }

    public void carregarApartamentos() {
        if (blocoIdAtual <= 0) {
            apartamentos.postValue(null);  
            return;
        }

        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                List<Apartamento> lista = repository.listarPorBloco(blocoIdAtual);
                apartamentos.postValue(lista);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar apartamentos: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void carregarLocatariosDisponiveis() {
        executorService.execute(() -> {
            try {
                List<Locatario> lista = locatarioRepository.listarSemApartamento();
                locatariosDisponiveis.postValue(lista);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar locatários: " + e.getMessage());
            }
        });
    }

    public void selecionarApartamento(Apartamento apartamento) {
        apartamentoSelecionado.setValue(apartamento);
    }

    public void salvarApartamento(Apartamento apartamento) {
        if (blocoIdAtual <= 0) {
            mensagemErro.postValue("Nenhum bloco selecionado");  
            return;
        }

        apartamento.setBlocoId(blocoIdAtual);

        Condominio condominio = condominioAtual.getValue();
        if (condominio != null) {
            double valorAluguel = apartamento.calcularAluguel(condominio);
            apartamento.setValorAluguel(valorAluguel);
        }

        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                if (apartamento.getId() > 0) {
                    repository.atualizar(apartamento);
                } else {
                    repository.inserir(apartamento);
                }
                carregarApartamentos();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao salvar apartamento: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void associarLocatario(Apartamento apartamento, Locatario locatario) {
        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                locatario.setApartamentoId(apartamento.getId());
                locatarioRepository.atualizar(locatario);

                apartamento.setLocatario(locatario);
                apartamentoSelecionado.postValue(apartamento);

                carregarApartamentos();
                carregarLocatariosDisponiveis();

                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao associar locatário: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void desassociarLocatario(Apartamento apartamento) {
        if (apartamento.getLocatario() == null) {
            return;
        }

        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                locatarioRepository.desassociarApartamento(apartamento.getLocatario().getId());

                apartamento.setLocatario(null);
                apartamentoSelecionado.postValue(apartamento);

                carregarApartamentos();
                carregarLocatariosDisponiveis();

                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao desassociar locatário: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void removerApartamento(Apartamento apartamento) {
        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                repository.remover(apartamento.getId());
                carregarApartamentos();
                carregarLocatariosDisponiveis();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao remover apartamento: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void limparSelecao() {
        apartamentoSelecionado.setValue(null);
    }

    public void buscarApartamentoPorId(long id) {
        isLoading.postValue(true);  
        executorService.execute(() -> {
            try {
                Apartamento apartamento = repository.buscarPorId(id);
                apartamentoSelecionado.postValue(apartamento);
                isLoading.postValue(false);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao buscar apartamento: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Apartamento>> getApartamentos() {
        return apartamentos;
    }

    public LiveData<Apartamento> getApartamentoSelecionado() {
        return apartamentoSelecionado;
    }

    public LiveData<List<Locatario>> getLocatariosDisponiveis() {
        return locatariosDisponiveis;
    }

    public LiveData<Condominio> getCondominioAtual() {
        return condominioAtual;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMensagemErro() {
        return mensagemErro;
    }

    public long getBlocoIdAtual() {
        return blocoIdAtual;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}