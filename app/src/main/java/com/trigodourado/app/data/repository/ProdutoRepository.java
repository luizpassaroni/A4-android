package com.trigodourado.app.data.repository;

import androidx.lifecycle.LiveData;

import com.trigodourado.app.data.local.ProdutoDao;
import com.trigodourado.app.data.model.Produto;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ProdutoRepository {
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private final ProdutoDao produtoDao;

    public ProdutoRepository(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
        IO_EXECUTOR.execute(() -> produtoDao.inserirTodos(Arrays.asList(
                new Produto(1, 1, "Pão francês", "Unidade fresca da fornada", 1.25, "", true, ""),
                new Produto(2, 2, "Croissant", "Massa folhada amanteigada", 8.50, "", true, ""),
                new Produto(3, 3, "Bolo de chocolate", "Fatia com cobertura", 12.00, "", true, "")
        )));
    }

    public LiveData<List<Produto>> listarAtivos() { return produtoDao.listarAtivos(); }
    public LiveData<List<Produto>> listarTodos() { return produtoDao.listarTodos(); }
    public void atualizarAtivo(int idProduto, boolean ativo) {
        IO_EXECUTOR.execute(() -> produtoDao.atualizarAtivo(idProduto, ativo));
    }
}
