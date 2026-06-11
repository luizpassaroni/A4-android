package com.trigodourado.app.data.model;

import java.util.List;

public final class CartState {
    private final List<CartItemUI> itens;
    private final int totalItens;
    private final double subtotal;
    private final double taxaEntrega;
    private final double total;
    private final boolean fechandoPedido;
    private final String mensagemErro;
    private final PedidoFinalizado pedidoFinalizado;
    private final EnderecoEntrega enderecoSelecionado;
    private final boolean areaAtendida;

    public CartState(List<CartItemUI> itens, double subtotal, double taxaEntrega, double total,
                     boolean fechandoPedido, String mensagemErro,
                     PedidoFinalizado pedidoFinalizado, EnderecoEntrega enderecoSelecionado,
                     boolean areaAtendida) {
        this.itens = List.copyOf(itens);
        this.totalItens = calcularTotalItens(itens);
        this.subtotal = subtotal;
        this.taxaEntrega = taxaEntrega;
        this.total = total;
        this.fechandoPedido = fechandoPedido;
        this.mensagemErro = mensagemErro;
        this.pedidoFinalizado = pedidoFinalizado;
        this.enderecoSelecionado = enderecoSelecionado;
        this.areaAtendida = areaAtendida;
    }

    public List<CartItemUI> getItens() { return itens; }
    public int getTotalItens() { return totalItens; }
    public double getSubtotal() { return subtotal; }
    public double getTaxaEntrega() { return taxaEntrega; }
    public double getTotal() { return total; }
    public boolean isFechandoPedido() { return fechandoPedido; }
    public String getMensagemErro() { return mensagemErro; }
    public PedidoFinalizado getPedidoFinalizado() { return pedidoFinalizado; }
    public EnderecoEntrega getEnderecoSelecionado() { return enderecoSelecionado; }
    public boolean isAreaAtendida() { return areaAtendida; }

    private int calcularTotalItens(List<CartItemUI> itens) {
        int quantidade = 0;
        for (CartItemUI item : itens) {
            quantidade += item.getQuantidade();
        }
        return quantidade;
    }
}
