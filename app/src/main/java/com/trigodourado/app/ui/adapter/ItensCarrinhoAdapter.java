package com.trigodourado.app.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.trigodourado.app.R;
import com.trigodourado.app.data.model.ItemPedido;
import com.trigodourado.app.databinding.ItemCarrinhoBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class ItensCarrinhoAdapter
        extends ListAdapter<ItemPedido, ItensCarrinhoAdapter.ItemCarrinhoViewHolder> {
    private static final DiffUtil.ItemCallback<ItemPedido> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ItemPedido>() {
                @Override
                public boolean areItemsTheSame(@NonNull ItemPedido antigo, @NonNull ItemPedido novo) {
                    return antigo.getIdProduto() == novo.getIdProduto();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ItemPedido antigo, @NonNull ItemPedido novo) {
                    return antigo.getQuantidade() == novo.getQuantidade()
                            && Double.compare(antigo.getPrecoUnitario(), novo.getPrecoUnitario()) == 0
                            && Double.compare(antigo.getSubtotal(), novo.getSubtotal()) == 0;
                }
            };

    public interface AcoesCarrinho {
        void incrementar(int idProduto);
        void decrementar(int idProduto);
        void remover(int idProduto);
    }

    private final AcoesCarrinho acoes;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public ItensCarrinhoAdapter(AcoesCarrinho acoes) {
        super(DIFF_CALLBACK);
        this.acoes = acoes;
    }

    public void atualizarItens(List<ItemPedido> novosItens) {
        submitList(novosItens == null
                ? Collections.emptyList()
                : new ArrayList<>(novosItens));
    }

    @NonNull
    @Override
    public ItemCarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarrinhoBinding binding = ItemCarrinhoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemCarrinhoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCarrinhoViewHolder holder, int position) {
        holder.bind(getItem(position), acoes, moeda);
    }

    public static final class ItemCarrinhoViewHolder extends RecyclerView.ViewHolder {
        private final ItemCarrinhoBinding binding;

        private ItemCarrinhoViewHolder(ItemCarrinhoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(ItemPedido item, AcoesCarrinho acoes, NumberFormat moeda) {
            binding.nomeProdutoCarrinho.setText(binding.getRoot().getContext()
                    .getString(R.string.produto_numero, item.getIdProduto()));
            binding.quantidadeProduto.setText(String.valueOf(item.getQuantidade()));
            binding.subtotalItem.setText(moeda.format(item.getSubtotal()));
            binding.incrementarQuantidade.setOnClickListener(v -> acoes.incrementar(item.getIdProduto()));
            binding.decrementarQuantidade.setOnClickListener(v -> acoes.decrementar(item.getIdProduto()));
            binding.removerItem.setOnClickListener(v -> acoes.remover(item.getIdProduto()));
        }
    }
}
