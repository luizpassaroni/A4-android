package com.trigodourado.app.ui.cart;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trigodourado.app.databinding.ActivityCarrinhoBinding;
import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.model.PedidoFinalizado;
import com.trigodourado.app.ui.adapter.ItensCarrinhoAdapter;
import com.trigodourado.app.ui.checkout.CheckoutBottomSheet;
import com.trigodourado.app.util.WindowInsetsUtil;

import java.text.NumberFormat;
import java.util.Locale;

public final class CarrinhoActivity extends AppCompatActivity {
    private ActivityCarrinhoBinding binding;
    private CartViewModel viewModel;
    private ItensCarrinhoAdapter adapter;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private PedidoFinalizado ultimoPedidoNotificado;
    private String ultimoErroNotificado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarrinhoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowInsetsUtil.aplicarSafeArea(this, binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        configurarLista();
        binding.fecharPedido.setOnClickListener(v -> {
            if (getSupportFragmentManager().findFragmentByTag(CheckoutBottomSheet.TAG) == null) {
                new CheckoutBottomSheet().show(getSupportFragmentManager(), CheckoutBottomSheet.TAG);
            }
        });
        viewModel.getEstado().observe(this, this::renderizar);
    }

    private void configurarLista() {
        adapter = new ItensCarrinhoAdapter(new ItensCarrinhoAdapter.AcoesCarrinho() {
            @Override
            public void incrementar(int idProduto) {
                viewModel.incrementarQuantidade(idProduto);
            }

            @Override
            public void decrementar(int idProduto) {
                viewModel.decrementarQuantidade(idProduto);
            }

            @Override
            public void remover(int idProduto) {
                viewModel.removerItem(idProduto);
            }
        });
        binding.listaItens.setLayoutManager(new LinearLayoutManager(this));
        binding.listaItens.setAdapter(adapter);
    }

    private void renderizar(CartState estado) {
        adapter.atualizarItens(estado.getItens());
        binding.subtotal.setText(moeda.format(estado.getSubtotal()));
        binding.taxaEntrega.setText(moeda.format(estado.getTaxaEntrega()));
        binding.total.setText(moeda.format(estado.getTotal()));
        binding.carrinhoVazio.setVisibility(estado.getItens().isEmpty() ? View.VISIBLE : View.GONE);
        binding.progresso.setVisibility(estado.isFechandoPedido() ? View.VISIBLE : View.GONE);
        binding.fecharPedido.setEnabled(!estado.getItens().isEmpty());

        if (estado.isFechandoPedido()) {
            Toast.makeText(this, "Validando e enviando pedido...", Toast.LENGTH_SHORT).show();
        }
        if (estado.getMensagemErro() != null
                && !estado.getMensagemErro().equals(ultimoErroNotificado)) {
            ultimoErroNotificado = estado.getMensagemErro();
            Toast.makeText(this, estado.getMensagemErro(), Toast.LENGTH_SHORT).show();
        }
        if (estado.getPedidoFinalizado() != null
                && estado.getPedidoFinalizado() != ultimoPedidoNotificado) {
            ultimoPedidoNotificado = estado.getPedidoFinalizado();
            int idPedido = estado.getPedidoFinalizado().getPedido().getIdPedido();
            Toast.makeText(this, "Pedido #" + idPedido + " recebido com sucesso!",
                    Toast.LENGTH_LONG).show();
        }
    }
}
