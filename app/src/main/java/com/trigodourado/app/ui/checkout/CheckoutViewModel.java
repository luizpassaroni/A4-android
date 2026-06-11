package com.trigodourado.app.ui.checkout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trigodourado.app.data.local.AppDatabase;
import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.FormaPagamento;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.model.PedidoFinalizado;
import com.trigodourado.app.data.model.StatusPedido;
import com.trigodourado.app.data.model.Usuario;
import com.trigodourado.app.data.repository.AuthRepository;
import com.trigodourado.app.data.repository.CartRepository;
import com.trigodourado.app.data.repository.PedidoRepository;
import com.trigodourado.app.data.repository.RepositoryCallback;

import java.time.LocalDateTime;

public final class CheckoutViewModel extends AndroidViewModel {
    private final CartRepository cartRepository = CartRepository.getInstance();
    private final PedidoRepository pedidoRepository;
    private final AuthRepository authRepository = AuthRepository.getInstance();
    private final MutableLiveData<CheckoutState> estado =
            new MutableLiveData<>(CheckoutState.inicial());
    private FormaPagamento formaPagamento = FormaPagamento.PIX;

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        pedidoRepository = new PedidoRepository(AppDatabase.getInstance(application).pedidoDao());
    }

    public LiveData<CartState> getCarrinho() { return cartRepository.getEstado(); }
    public LiveData<CheckoutState> getEstado() { return estado; }
    public void selecionarEndereco(EnderecoEntrega endereco) { cartRepository.selecionarEndereco(endereco); }

    public void selecionarFormaPagamento(FormaPagamento formaPagamento) {
        if (formaPagamento != null) {
            this.formaPagamento = formaPagamento;
            cartRepository.definirFormaPagamento(formaPagamento);
        }
    }

    public void confirmarPedido() {
        CartState carrinho = cartRepository.getEstado().getValue();
        Usuario usuario = authRepository.getUsuarioAutenticado();
        if (usuario == null || carrinho == null || carrinho.getItens().isEmpty()) {
            estado.setValue(CheckoutState.erro("Sessão ou carrinho inválido."));
            return;
        }
        if (carrinho.getEnderecoSelecionado() == null || !carrinho.isAreaAtendida()) {
            estado.setValue(CheckoutState.erro("Selecione um endereço atendido."));
            return;
        }
        Pedido pedido = new Pedido(0, usuario.getIdUsuario(), LocalDateTime.now().toString(),
                StatusPedido.RECEBIDO, formaPagamento, carrinho.getSubtotal(),
                carrinho.getTaxaEntrega(), 0, carrinho.getTotal(), "", carrinho.getItens());
        estado.setValue(CheckoutState.processando());
        pedidoRepository.confirmarPedido(pedido, carrinho.getEnderecoSelecionado(),
                new RepositoryCallback<PedidoFinalizado>() {
                    @Override public void onSuccess(PedidoFinalizado resultado) {
                        cartRepository.limparCarrinho();
                        estado.setValue(CheckoutState.sucesso(resultado));
                    }
                    @Override public void onError(String mensagem) {
                        estado.setValue(CheckoutState.erro(mensagem));
                    }
                });
    }
}
