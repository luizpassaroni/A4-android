package com.trigodourado.app.data.model;

public final class CartItemUI {
    private final int idItem;
    private final int idProduto;
    private final String nomeProduto;
    private final int quantidade;
    private final double precoUnitario;
    private final double subtotal;
    private final String imagemUrl;

    public CartItemUI(int idItem, int idProduto, String nomeProduto, int quantidade,
                      double precoUnitario, double subtotal, String imagemUrl) {
        this.idItem = idItem;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
        this.imagemUrl = imagemUrl;
    }

    public int getIdItem() { return idItem; }
    public int getIdProduto() { return idProduto; }
    public String getNomeProduto() { return nomeProduto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public double getSubtotal() { return subtotal; }
    public String getImagemUrl() { return imagemUrl; }

    public CartItemUI comQuantidade(int novaQuantidade) {
        return new CartItemUI(idItem, idProduto, nomeProduto, novaQuantidade,
                precoUnitario, precoUnitario * novaQuantidade, imagemUrl);
    }
}
