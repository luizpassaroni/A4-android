package com.trigodourado.app.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.trigodourado.app.R;
import com.trigodourado.app.data.model.Produto;
import com.trigodourado.app.databinding.ItemProdutoBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ProdutoViewHolder> {
    public interface OnProdutoClickListener {
        void onAdicionarAoCarrinho(Produto produto);
    }

    private final List<Produto> produtos = new ArrayList<>();
    private final OnProdutoClickListener listener;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public ProdutosAdapter(OnProdutoClickListener listener) {
        this.listener = listener;
    }

    public void atualizarProdutos(List<Produto> novosProdutos) {
        List<Produto> listaAtualizada = new ArrayList<>();
        if (novosProdutos != null) {
            listaAtualizada.addAll(novosProdutos);
        }
        DiffUtil.DiffResult resultado = DiffUtil.calculateDiff(
                new ProdutoDiffCallback(new ArrayList<>(produtos), listaAtualizada));
        produtos.clear();
        produtos.addAll(listaAtualizada);
        resultado.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProdutoBinding binding = ItemProdutoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProdutoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        holder.bind(produtos.get(position));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    final class ProdutoViewHolder extends RecyclerView.ViewHolder {
        private final ItemProdutoBinding binding;

        ProdutoViewHolder(ItemProdutoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Produto produto) {
            binding.nomeProduto.setText(produto.getNome());
            binding.descricaoProduto.setText(produto.getDescricao());
            binding.precoProduto.setText(moeda.format(produto.getPreco()));
            binding.imagemProduto.setImageResource(R.mipmap.ic_launcher);
            binding.adicionarProduto.setEnabled(produto.isAtivo());
            binding.adicionarProduto.setOnClickListener(v -> listener.onAdicionarAoCarrinho(produto));
        }
    }
}
