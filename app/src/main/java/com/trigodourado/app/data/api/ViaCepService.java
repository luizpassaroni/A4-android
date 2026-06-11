package com.trigodourado.app.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {
    @GET("ws/{cep}/json/")
    Call<ViaCepResponse> buscarCep(@Path("cep") String cep);
}
