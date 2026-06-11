package com.trigodourado.app.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trigodourado.app.data.model.Usuario;
import com.trigodourado.app.data.repository.AuthRepository;
import com.trigodourado.app.data.repository.RepositoryCallback;

public final class AuthViewModel extends ViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<AuthState> estado = new MutableLiveData<>(AuthState.inicial());

    public AuthViewModel() {
        repository = AuthRepository.getInstance();
    }

    public LiveData<AuthState> getEstado() {
        return estado;
    }

    public void login(String email, String senha) {
        estado.setValue(AuthState.carregando());
        repository.login(email, senha, callback());
    }

    public void cadastrar(Usuario usuario) {
        estado.setValue(AuthState.carregando());
        repository.cadastrar(usuario, callback());
    }

    private RepositoryCallback<Usuario> callback() {
        return new RepositoryCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario resultado) {
                estado.postValue(AuthState.sucesso(resultado));
            }

            @Override
            public void onError(String mensagem) {
                estado.postValue(AuthState.erro(mensagem));
            }
        };
    }
}
