package com.trigodourado.app.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    private static final String BASE_URL = "https://viacep.com.br/";
    private static final RetrofitClient INSTANCE = new RetrofitClient();

    private final Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        return INSTANCE;
    }

    public ViaCepService getViaCepService() {
        return retrofit.create(ViaCepService.class);
    }
}
