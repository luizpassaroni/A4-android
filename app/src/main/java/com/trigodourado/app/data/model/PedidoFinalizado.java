package com.trigodourado.app.data.model;

public final class PedidoFinalizado {
    private final Pedido pedido;
    private final Pagamento pagamento;
    private final Entrega entrega;

    public PedidoFinalizado(Pedido pedido, Pagamento pagamento, Entrega entrega) {
        this.pedido = pedido;
        this.pagamento = pagamento;
        this.entrega = entrega;
    }

    public Pedido getPedido() { return pedido; }
    public Pagamento getPagamento() { return pagamento; }
    public Entrega getEntrega() { return entrega; }
}
