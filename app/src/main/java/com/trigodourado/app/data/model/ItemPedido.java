package com.trigodourado.app.data.model;

public final class ItemPedido {
    private final int idItem;
    private final int idPedido;
    private final int idProduto;
    private final int quantidade;
    private final double precoUnitario;
    private final double subtotal;

    public ItemPedido(int idItem, int idPedido, int idProduto, int quantidade,
                      double precoUnitario, double subtotal) {
        this.idItem = idItem;
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }

    public int getIdItem() { return idItem; }
    public int getIdPedido() { return idPedido; }
    public int getIdProduto() { return idProduto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public double getSubtotal() { return subtotal; }

    public ItemPedido comQuantidade(int novaQuantidade) {
        return new ItemPedido(idItem, idPedido, idProduto, novaQuantidade,
                precoUnitario, precoUnitario * novaQuantidade);
    }
}
