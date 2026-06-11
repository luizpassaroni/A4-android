package com.trigodourado.app.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.trigodourado.app.data.model.Produto;
import com.trigodourado.app.databinding.ItemEstoqueBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class EstoqueAdapter extends ListAdapter<Produto, EstoqueAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Produto> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Produto>() {
                @Override
                public boolean areItemsTheSame(@NonNull Produto antigo, @NonNull Produto novo) {
                    return antigo.getIdProduto() == novo.getIdProduto();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Produto antigo, @NonNull Produto novo) {
                    return Objects.equals(antigo.getNome(), novo.getNome())
                            && antigo.isAtivo() == novo.isAtivo();
                }
            };

    public interface Listener { void alterar(Produto produto, boolean ativo); }
    private final Listener listener;
    public EstoqueAdapter(Listener listener) { super(DIFF_CALLBACK); this.listener = listener; }
    public void atualizar(List<Produto> novos) {
        submitList(novos == null ? Collections.emptyList() : new ArrayList<>(novos));
    }
    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemEstoqueBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }
    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemEstoqueBinding binding;
        private ViewHolder(ItemEstoqueBinding binding) { super(binding.getRoot()); this.binding = binding; }
        private void bind(Produto produto, Listener listener) {
            binding.nomeProdutoEstoque.setText(produto.getNome());
            binding.produtoAtivo.setOnCheckedChangeListener(null);
            binding.produtoAtivo.setChecked(produto.isAtivo());
            binding.produtoAtivo.setOnCheckedChangeListener((button, checked) -> listener.alterar(produto, checked));
        }
    }
}
