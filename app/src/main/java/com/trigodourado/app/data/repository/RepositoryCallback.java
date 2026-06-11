package com.trigodourado.app.data.repository;

public interface RepositoryCallback<T> {
    void onSuccess(T resultado);
    void onError(String mensagem);
}
