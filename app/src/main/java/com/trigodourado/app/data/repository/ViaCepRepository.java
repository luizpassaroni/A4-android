package com.trigodourado.app.data.repository;

import androidx.annotation.NonNull;

import com.trigodourado.app.data.api.RetrofitClient;
import com.trigodourado.app.data.api.ViaCepResponse;
import com.trigodourado.app.data.api.ViaCepService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class ViaCepRepository {
    private static final ViaCepRepository INSTANCE = new ViaCepRepository();
    private final ViaCepService service = RetrofitClient.getInstance().getViaCepService();

    private ViaCepRepository() {
    }

    public static ViaCepRepository getInstance() {
        return INSTANCE;
    }

    public void buscarCep(String cep, RepositoryCallback<ViaCepResponse> callback) {
        service.buscarCep(cep).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ViaCepResponse> call,
                                   @NonNull Response<ViaCepResponse> response) {
                ViaCepResponse endereco = response.body();
                if (response.isSuccessful() && endereco != null) {
                    callback.onSuccess(endereco);
                } else {
                    callback.onError("Não foi possível consultar o CEP.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ViaCepResponse> call, @NonNull Throwable throwable) {
                callback.onError("Falha de conexão ao consultar o CEP.");
            }
        });
    }
}
