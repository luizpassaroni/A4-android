package com.trigodourado.app.ui.history;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.trigodourado.app.R;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.model.StatusPedido;
import com.trigodourado.app.databinding.ItemPedidoHistoricoBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class PedidoAdapter extends ListAdapter<Pedido, PedidoAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Pedido> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Pedido>() {
                @Override
                public boolean areItemsTheSame(@NonNull Pedido antigo, @NonNull Pedido novo) {
                    return antigo.getIdPedido() == novo.getIdPedido();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Pedido antigo, @NonNull Pedido novo) {
                    return Objects.equals(antigo.getDataPedido(), novo.getDataPedido())
                            && Objects.equals(antigo.getStatus(), novo.getStatus())
                            && Double.compare(antigo.getTotal(), novo.getTotal()) == 0;
                }
            };

    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public PedidoAdapter() {
        super(DIFF_CALLBACK);
    }

    public void atualizar(List<Pedido> novos) {
        submitList(novos == null ? Collections.emptyList() : new ArrayList<>(novos));
    }

    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPedidoHistoricoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), moeda);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPedidoHistoricoBinding binding;
        private ViewHolder(ItemPedidoHistoricoBinding binding) { super(binding.getRoot()); this.binding = binding; }
        private void bind(Pedido pedido, NumberFormat moeda) {
            binding.idPedido.setText(binding.getRoot().getContext()
                    .getString(R.string.pedido_numero, pedido.getIdPedido()));
            binding.dataPedido.setText(pedido.getDataPedido());
            binding.totalPedido.setText(moeda.format(pedido.getTotal()));
            binding.statusPedido.setText(pedido.getStatus().name());
            binding.statusPedido.setChipBackgroundColor(ColorStateList.valueOf(cor(pedido.getStatus())));
        }
        private int cor(StatusPedido status) {
            if (status == StatusPedido.ENTREGUE) return Color.rgb(46, 125, 50);
            if (status == StatusPedido.EM_PREPARO) return Color.rgb(239, 108, 0);
            if (status == StatusPedido.SAINDO_PARA_ENTREGA) return Color.rgb(21, 101, 192);
            if (status == StatusPedido.CANCELADO) return Color.rgb(183, 28, 28);
            return Color.rgb(118, 83, 0);
        }
    }
}
