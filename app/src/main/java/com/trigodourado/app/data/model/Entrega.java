package com.trigodourado.app.data.model;

public final class Entrega {
    private final int idEntrega;
    private final int idPedido;
    private final String enderecoEntrega;
    private final String bairro;
    private final String cep;
    private final double taxaEntrega;
    private final StatusEntrega status;
    private final String previsaoEntrega;
    private final String dataEntrega;
    private final String entregador;

    public Entrega(int idEntrega, int idPedido, String enderecoEntrega, String bairro,
                   String cep, double taxaEntrega, StatusEntrega status,
                   String previsaoEntrega, String dataEntrega, String entregador) {
        this.idEntrega = idEntrega;
        this.idPedido = idPedido;
        this.enderecoEntrega = enderecoEntrega;
        this.bairro = bairro;
        this.cep = cep;
        this.taxaEntrega = taxaEntrega;
        this.status = status;
        this.previsaoEntrega = previsaoEntrega;
        this.dataEntrega = dataEntrega;
        this.entregador = entregador;
    }

    public int getIdEntrega() { return idEntrega; }
    public int getIdPedido() { return idPedido; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public String getBairro() { return bairro; }
    public String getCep() { return cep; }
    public double getTaxaEntrega() { return taxaEntrega; }
    public StatusEntrega getStatus() { return status; }
    public String getPrevisaoEntrega() { return previsaoEntrega; }
    public String getDataEntrega() { return dataEntrega; }
    public String getEntregador() { return entregador; }
}
