package com.trigodourado.app.ui.address;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.trigodourado.app.data.api.ViaCepResponse;
import com.trigodourado.app.data.local.AppDatabase;
import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.Usuario;
import com.trigodourado.app.data.repository.AuthRepository;
import com.trigodourado.app.data.repository.EnderecoRepository;
import com.trigodourado.app.data.repository.RepositoryCallback;
import com.trigodourado.app.data.repository.ViaCepRepository;

import java.util.Collections;
import java.util.List;

public final class EnderecoViewModel extends AndroidViewModel {
    private final AuthRepository authRepository = AuthRepository.getInstance();
    private final EnderecoRepository repository;
    private final ViaCepRepository viaCepRepository = ViaCepRepository.getInstance();
    private final MediatorLiveData<EnderecoState> estado =
            new MediatorLiveData<>(EnderecoState.inicial());
    private final MutableLiveData<ViaCepState> estadoCep =
            new MutableLiveData<>(ViaCepState.inicial());
    private LiveData<List<EnderecoEntrega>> fonteEnderecos;
    private String ultimoCepSolicitado = "";
    private volatile String mensagemPendente;

    public EnderecoViewModel(@NonNull Application application) {
        super(application);
        repository = new EnderecoRepository(AppDatabase.getInstance(application).enderecoDao());
    }

    public LiveData<EnderecoState> getEstado() {
        return estado;
    }

    public LiveData<ViaCepState> getEstadoCep() {
        return estadoCep;
    }

    public void buscarCep(String cep) {
        String cepNumerico = cep == null ? "" : cep.replaceAll("\\D", "");
        if (cepNumerico.length() != 8) {
            estadoCep.setValue(ViaCepState.erro("CEP inválido."));
            return;
        }
        ultimoCepSolicitado = cepNumerico;
        estadoCep.setValue(ViaCepState.carregando());
        viaCepRepository.buscarCep(cepNumerico, new RepositoryCallback<ViaCepResponse>() {
            @Override
            public void onSuccess(ViaCepResponse resultado) {
                if (!cepNumerico.equals(ultimoCepSolicitado)) {
                    return;
                }
                if (resultado.isErro()) {
                    estadoCep.postValue(ViaCepState.erro("CEP inexistente"));
                    return;
                }
                if (!"Rio de Janeiro".equalsIgnoreCase(resultado.getLocalidade())
                        || !"RJ".equalsIgnoreCase(resultado.getUf())) {
                    estadoCep.postValue(ViaCepState.erro("Área fora da zona de entrega"));
                    return;
                }
                estadoCep.postValue(ViaCepState.sucesso(resultado));
            }

            @Override
            public void onError(String mensagem) {
                if (cepNumerico.equals(ultimoCepSolicitado)) {
                    estadoCep.postValue(ViaCepState.erro(mensagem));
                }
            }
        });
    }

    public void limparBuscaCep() {
        ultimoCepSolicitado = "";
        estadoCep.setValue(ViaCepState.inicial());
    }

    public void listar() {
        Usuario usuario = authRepository.getUsuarioAutenticado();
        if (usuario == null) {
            estado.setValue(EnderecoState.erro(Collections.emptyList(),
                    "Faça login para consultar seus endereços."));
            return;
        }
        if (fonteEnderecos != null) {
            estado.removeSource(fonteEnderecos);
        }
        estado.setValue(EnderecoState.carregando(enderecosAtuais()));
        fonteEnderecos = repository.listar(usuario.getIdUsuario());
        estado.addSource(fonteEnderecos, enderecos -> {
            String mensagem = mensagemPendente;
            mensagemPendente = null;
            estado.setValue(EnderecoState.sucesso(
                    enderecos == null ? Collections.emptyList() : enderecos, mensagem));
        });
    }

    public void cadastrar(EnderecoEntrega endereco) {
        Usuario usuario = authRepository.getUsuarioAutenticado();
        if (usuario == null) {
            estado.setValue(EnderecoState.erro(enderecosAtuais(),
                    "Faça login para cadastrar um endereço."));
            return;
        }
        estado.setValue(EnderecoState.carregando(enderecosAtuais()));
        repository.cadastrar(usuario.getIdUsuario(), endereco, new RepositoryCallback<EnderecoEntrega>() {
            @Override
            public void onSuccess(EnderecoEntrega resultado) {
                mensagemPendente = "Endereço cadastrado.";
            }

            @Override
            public void onError(String mensagem) {
                estado.postValue(EnderecoState.erro(enderecosAtuais(), mensagem));
            }
        });
    }

    public void remover(EnderecoEntrega endereco) {
        repository.remover(endereco, new RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void resultado) {
                mensagemPendente = "Endereço removido.";
            }

            @Override
            public void onError(String mensagem) {
                estado.postValue(EnderecoState.erro(enderecosAtuais(), mensagem));
            }
        });
    }

    private List<EnderecoEntrega> enderecosAtuais() {
        EnderecoState atual = estado.getValue();
        return atual == null ? Collections.emptyList() : atual.getEnderecos();
    }
}
