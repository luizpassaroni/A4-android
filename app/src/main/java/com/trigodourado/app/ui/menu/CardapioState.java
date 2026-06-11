package com.trigodourado.app.ui.menu;

import com.trigodourado.app.data.model.Produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CardapioState {
    private final boolean carregando;
    private final List<Produto> produtos;

    private CardapioState(boolean carregando, List<Produto> produtos) {
        this.carregando = carregando;
        this.produtos = Collections.unmodifiableList(new ArrayList<>(produtos));
    }

    public static CardapioState carregando() {
        return new CardapioState(true, Collections.emptyList());
    }

    public static CardapioState sucesso(List<Produto> produtos) {
        return new CardapioState(false, produtos);
    }

    public boolean isCarregando() { return carregando; }
    public List<Produto> getProdutos() { return produtos; }
}
