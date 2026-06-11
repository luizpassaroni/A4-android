package com.trigodourado.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.model.CartItemUI;
import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.FormaPagamento;
import com.trigodourado.app.data.model.PedidoFinalizado;
import com.trigodourado.app.data.model.Produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class CartRepository {
    private static final CartRepository INSTANCE = new CartRepository();
    private static final Map<String, Double> TAXAS_POR_BAIRRO = criarTaxas();

    private final MutableLiveData<CartState> estado =
            new MutableLiveData<>(criarEstadoInicial());
    private EnderecoEntrega enderecoSelecionado;
    private FormaPagamento formaPagamento = FormaPagamento.PIX;
    private String observacao = "";

    private CartRepository() {
    }

    public static CartRepository getInstance() {
        return INSTANCE;
    }

    public MutableLiveData<CartState> getEstado() {
        return estado;
    }

    public void adicionarProduto(Produto produto) {
        if (produto == null || !produto.isAtivo()) {
            return;
        }
        List<CartItemUI> itens = copiarItens();
        int indice = localizarProduto(itens, produto.getIdProduto());
        if (indice >= 0) {
            CartItemUI atual = itens.get(indice);
            itens.set(indice, atual.comQuantidade(atual.getQuantidade() + 1));
        } else {
            itens.add(new CartItemUI(produto.getIdProduto(), produto.getIdProduto(),
                    produto.getNome(), 1, produto.getPreco(), produto.getPreco(),
                    produto.getImagem()));
        }
        publicar(itens, false, null, null);
    }

    public void incrementarQuantidade(int idProduto) {
        alterarQuantidade(idProduto, 1);
    }

    public void decrementarQuantidade(int idProduto) {
        alterarQuantidade(idProduto, -1);
    }

    public void removerItem(int idProduto) {
        List<CartItemUI> itens = copiarItens();
        int indice = localizarProduto(itens, idProduto);
        if (indice >= 0) {
            itens.remove(indice);
            publicar(itens, false, null, null);
        }
    }

    public void selecionarEndereco(EnderecoEntrega endereco) {
        enderecoSelecionado = endereco;
        publicar(copiarItens(), false, null, null);
    }

    public void definirFormaPagamento(FormaPagamento formaPagamento) {
        if (formaPagamento != null) {
            this.formaPagamento = formaPagamento;
        }
    }

    public void definirObservacao(String observacao) {
        this.observacao = observacao == null ? "" : observacao;
    }

    public void limparCarrinho() {
        enderecoSelecionado = null;
        formaPagamento = FormaPagamento.PIX;
        observacao = "";
        publicar(Collections.emptyList(), false, null, null);
    }

    private void alterarQuantidade(int idProduto, int diferenca) {
        List<CartItemUI> itens = copiarItens();
        int indice = localizarProduto(itens, idProduto);
        if (indice < 0) {
            return;
        }
        CartItemUI atual = itens.get(indice);
        int quantidade = atual.getQuantidade() + diferenca;
        if (quantidade <= 0) {
            itens.remove(indice);
        } else {
            itens.set(indice, atual.comQuantidade(quantidade));
        }
        publicar(itens, false, null, null);
    }

    private void publicar(List<CartItemUI> itens, boolean fechando, String erro,
                          PedidoFinalizado finalizado) {
        estado.setValue(criarEstado(itens, fechando, erro, finalizado));
    }

    private List<CartItemUI> copiarItens() {
        CartState atual = estado.getValue();
        return atual == null ? new ArrayList<>() : new ArrayList<>(atual.getItens());
    }

    private int localizarProduto(List<CartItemUI> itens, int idProduto) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getIdProduto() == idProduto) {
                return i;
            }
        }
        return -1;
    }

    private CartState criarEstado(List<CartItemUI> itens, boolean fechando, String erro,
                                  PedidoFinalizado finalizado) {
        double subtotal = 0;
        for (CartItemUI item : itens) {
            subtotal += item.getSubtotal();
        }
        boolean areaAtendida = enderecoSelecionado != null && calcularTaxaEntrega() != null;
        double taxaEntrega = itens.isEmpty() || !areaAtendida ? 0 : calcularTaxaEntrega();
        return new CartState(itens, subtotal, taxaEntrega, subtotal + taxaEntrega,
                fechando, erro, finalizado, enderecoSelecionado, areaAtendida);
    }

    private Double calcularTaxaEntrega() {
        if (enderecoSelecionado == null || enderecoSelecionado.getBairro() == null) {
            return null;
        }
        return TAXAS_POR_BAIRRO.get(
                enderecoSelecionado.getBairro().trim().toLowerCase(Locale.ROOT));
    }

    private static CartState criarEstadoInicial() {
        return new CartState(Collections.emptyList(), 0, 0, 0, false, null, null, null, false);
    }

    private static Map<String, Double> criarTaxas() {
        return Map.of("copacabana", 5.0, "ipanema", 9.0, "leblon", 10.0, "botafogo", 7.0, "flamengo", 7.0, "tijuca", 12.0);
    }
}
