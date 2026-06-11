package com.trigodourado.app.ui.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trigodourado.app.data.local.AppDatabase;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.model.Produto;
import com.trigodourado.app.data.repository.PedidoRepository;
import com.trigodourado.app.data.repository.ProdutoRepository;

import java.util.List;

public final class AdminViewModel extends AndroidViewModel {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        pedidoRepository = new PedidoRepository(database.pedidoDao());
        produtoRepository = new ProdutoRepository(database.produtoDao());
    }

    public LiveData<List<Pedido>> getPedidosAtivos() { return pedidoRepository.listarAtivos(); }
    public LiveData<List<Produto>> getProdutos() { return produtoRepository.listarTodos(); }
    public void avancarStatus(Pedido pedido) { pedidoRepository.atualizarStatus(pedido); }
    public void atualizarAtivo(Produto produto, boolean ativo) {
        produtoRepository.atualizarAtivo(produto.getIdProduto(), ativo);
    }
}
