package com.trigodourado.app.data.model;

public final class Pagamento {
    private final int idPagamento;
    private final int idPedido;
    private final FormaPagamento formaPagamento;
    private final StatusPagamento status;
    private final double valor;
    private final String dataPagamento;
    private final String transacaoId;

    public Pagamento(int idPagamento, int idPedido, FormaPagamento formaPagamento,
                     StatusPagamento status, double valor, String dataPagamento,
                     String transacaoId) {
        this.idPagamento = idPagamento;
        this.idPedido = idPedido;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.transacaoId = transacaoId;
    }

    public int getIdPagamento() { return idPagamento; }
    public int getIdPedido() { return idPedido; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public StatusPagamento getStatus() { return status; }
    public double getValor() { return valor; }
    public String getDataPagamento() { return dataPagamento; }
    public String getTransacaoId() { return transacaoId; }
}
