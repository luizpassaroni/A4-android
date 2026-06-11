package com.trigodourado.app.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.trigodourado.app.R;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.databinding.ItemPedidoAtivoBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PedidoAtivoAdapter extends ListAdapter<Pedido, PedidoAtivoAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Pedido> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Pedido>() {
                @Override
                public boolean areItemsTheSame(@NonNull Pedido antigo, @NonNull Pedido novo) {
                    return antigo.getIdPedido() == novo.getIdPedido();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Pedido antigo, @NonNull Pedido novo) {
                    return Objects.equals(antigo.getStatus(), novo.getStatus())
                            && Objects.equals(antigo.getDataPedido(), novo.getDataPedido())
                            && Double.compare(antigo.getTotal(), novo.getTotal()) == 0;
                }
            };

    public interface Listener { void avancar(Pedido pedido); }
    private final Listener listener;
    public PedidoAtivoAdapter(Listener listener) { super(DIFF_CALLBACK); this.listener = listener; }
    public void atualizar(List<Pedido> novos) {
        submitList(novos == null ? Collections.emptyList() : new ArrayList<>(novos));
    }
    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPedidoAtivoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }
    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPedidoAtivoBinding binding;
        private ViewHolder(ItemPedidoAtivoBinding binding) { super(binding.getRoot()); this.binding = binding; }
        private void bind(Pedido pedido, Listener listener) {
            binding.pedidoAtivoTitulo.setText(binding.getRoot().getContext()
                    .getString(R.string.pedido_numero, pedido.getIdPedido()));
            binding.pedidoAtivoStatus.setText(pedido.getStatus().name());
            binding.avancarStatus.setOnClickListener(v -> listener.avancar(pedido));
        }
    }
}
