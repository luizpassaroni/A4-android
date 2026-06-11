package com.trigodourado.app.ui.checkout;

import com.trigodourado.app.data.model.PedidoFinalizado;

public final class CheckoutState {
    private final boolean processando;
    private final String mensagemErro;
    private final PedidoFinalizado pedidoFinalizado;

    private CheckoutState(boolean processando, String mensagemErro,
                          PedidoFinalizado pedidoFinalizado) {
        this.processando = processando;
        this.mensagemErro = mensagemErro;
        this.pedidoFinalizado = pedidoFinalizado;
    }

    public static CheckoutState inicial() { return new CheckoutState(false, null, null); }
    public static CheckoutState processando() { return new CheckoutState(true, null, null); }
    public static CheckoutState erro(String mensagem) {
        return new CheckoutState(false, mensagem, null);
    }
    public static CheckoutState sucesso(PedidoFinalizado pedido) {
        return new CheckoutState(false, null, pedido);
    }

    public boolean isProcessando() { return processando; }
    public String getMensagemErro() { return mensagemErro; }
    public PedidoFinalizado getPedidoFinalizado() { return pedidoFinalizado; }
}
