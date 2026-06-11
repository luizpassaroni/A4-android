package com.trigodourado.app.ui.menu;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.trigodourado.app.data.local.AppDatabase;
import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.model.CategoriaProduto;
import com.trigodourado.app.data.model.Produto;
import com.trigodourado.app.data.repository.CartRepository;
import com.trigodourado.app.data.repository.ProdutoRepository;

import java.util.Arrays;
import java.util.List;

public final class CardapioViewModel extends AndroidViewModel {
    private final CartRepository cartRepository = CartRepository.getInstance();
    private final MediatorLiveData<CardapioState> estadoProdutos =
            new MediatorLiveData<>(CardapioState.carregando());
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable observarProdutos;
    private final MutableLiveData<List<CategoriaProduto>> categorias =
            new MutableLiveData<>(Arrays.asList(
                    new CategoriaProduto(0, "Todos", "Todos os produtos disponíveis", "", true),
                    new CategoriaProduto(1, "Pães Tradicionais", "Pães frescos da fornada", "", true),
                    new CategoriaProduto(2, "Pães Doces", "Massas doces e folhadas", "", true),
                    new CategoriaProduto(3, "Bebidas", "Bebidas e acompanhamentos", "", true)
            ));

    public CardapioViewModel(@NonNull Application application) {
        super(application);
        ProdutoRepository repository =
                new ProdutoRepository(AppDatabase.getInstance(application).produtoDao());
        observarProdutos = () -> estadoProdutos.addSource(repository.listarAtivos(),
                produtos -> estadoProdutos.setValue(CardapioState.sucesso(produtos)));
        handler.postDelayed(observarProdutos, 1500);
    }

    public LiveData<CartState> getEstadoCarrinho() { return cartRepository.getEstado(); }
    public LiveData<CardapioState> getEstadoProdutos() { return estadoProdutos; }
    public LiveData<List<CategoriaProduto>> getCategorias() { return categorias; }
    public void adicionarAoCarrinho(Produto produto) { cartRepository.adicionarProduto(produto); }

    @Override protected void onCleared() { handler.removeCallbacks(observarProdutos); }
}
