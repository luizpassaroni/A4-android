package com.trigodourado.app.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.FormaPagamento;
import com.trigodourado.app.data.model.Produto;
import com.trigodourado.app.data.repository.CartRepository;

public final class CartViewModel extends ViewModel {
    private final CartRepository repository = CartRepository.getInstance();

    public LiveData<CartState> getEstado() {
        return repository.getEstado();
    }

    public void adicionarProduto(Produto produto) { repository.adicionarProduto(produto); }
    public void incrementarQuantidade(int idProduto) { repository.incrementarQuantidade(idProduto); }
    public void decrementarQuantidade(int idProduto) { repository.decrementarQuantidade(idProduto); }
    public void removerItem(int idProduto) { repository.removerItem(idProduto); }
    public void selecionarEndereco(EnderecoEntrega endereco) { repository.selecionarEndereco(endereco); }
    public void definirFormaPagamento(FormaPagamento forma) { repository.definirFormaPagamento(forma); }
    public void definirObservacao(String observacao) { repository.definirObservacao(observacao); }
}
