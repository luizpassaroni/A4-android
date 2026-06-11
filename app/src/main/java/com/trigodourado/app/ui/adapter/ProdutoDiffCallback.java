package com.trigodourado.app.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.trigodourado.app.data.model.Produto;

import java.util.List;
import java.util.Objects;

public final class ProdutoDiffCallback extends DiffUtil.Callback {
    private final List<Produto> produtosAntigos;
    private final List<Produto> produtosNovos;

    public ProdutoDiffCallback(List<Produto> produtosAntigos, List<Produto> produtosNovos) {
        this.produtosAntigos = produtosAntigos;
        this.produtosNovos = produtosNovos;
    }

    @Override
    public int getOldListSize() {
        return produtosAntigos.size();
    }

    @Override
    public int getNewListSize() {
        return produtosNovos.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return produtosAntigos.get(oldItemPosition).getIdProduto()
                == produtosNovos.get(newItemPosition).getIdProduto();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Produto antigo = produtosAntigos.get(oldItemPosition);
        Produto novo = produtosNovos.get(newItemPosition);
        return antigo.getIdCategoria() == novo.getIdCategoria()
                && Double.compare(antigo.getPreco(), novo.getPreco()) == 0
                && antigo.isAtivo() == novo.isAtivo()
                && Objects.equals(antigo.getNome(), novo.getNome())
                && Objects.equals(antigo.getDescricao(), novo.getDescricao())
                && Objects.equals(antigo.getImagem(), novo.getImagem())
                && Objects.equals(antigo.getDataCadastro(), novo.getDataCadastro());
    }

    @NonNull
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return produtosNovos.get(newItemPosition);
    }
}
