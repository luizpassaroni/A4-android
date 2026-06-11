package com.trigodourado.app.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.List;

@Entity(tableName = "pedidos")
public final class Pedido {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_pedido")
    private final int idPedido;
    @ColumnInfo(name = "id_usuario")
    private final int idUsuario;
    @ColumnInfo(name = "data_pedido")
    private final String dataPedido;
    @ColumnInfo(name = "status")
    private final StatusPedido status;
    @ColumnInfo(name = "forma_pagamento")
    private final FormaPagamento formaPagamento;
    @ColumnInfo(name = "subtotal")
    private final double subtotal;
    @ColumnInfo(name = "taxa_entrega")
    private final double taxaEntrega;
    @ColumnInfo(name = "desconto")
    private final double desconto;
    @ColumnInfo(name = "total")
    private final double total;
    @ColumnInfo(name = "observacao")
    private final String observacao;
    @Ignore
    private final List<ItemPedido> itens;

    public Pedido(int idPedido, int idUsuario, String dataPedido, StatusPedido status,
                  FormaPagamento formaPagamento, double subtotal, double taxaEntrega,
                  double desconto, double total, String observacao) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.dataPedido = dataPedido;
        this.status = status;
        this.formaPagamento = formaPagamento;
        this.subtotal = subtotal;
        this.taxaEntrega = taxaEntrega;
        this.desconto = desconto;
        this.total = total;
        this.observacao = observacao;
        this.itens = Collections.emptyList();
    }

    @Ignore
    public Pedido(int idPedido, int idUsuario, String dataPedido, StatusPedido status,
                  FormaPagamento formaPagamento, double subtotal, double taxaEntrega,
                  double desconto, double total, String observacao, List<ItemPedido> itens) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.dataPedido = dataPedido;
        this.status = status;
        this.formaPagamento = formaPagamento;
        this.subtotal = subtotal;
        this.taxaEntrega = taxaEntrega;
        this.desconto = desconto;
        this.total = total;
        this.observacao = observacao;
        this.itens = List.copyOf(itens);
    }

    public int getIdPedido() { return idPedido; }
    public int getIdUsuario() { return idUsuario; }
    public String getDataPedido() { return dataPedido; }
    public StatusPedido getStatus() { return status; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public double getSubtotal() { return subtotal; }
    public double getTaxaEntrega() { return taxaEntrega; }
    public double getDesconto() { return desconto; }
    public double getTotal() { return total; }
    public String getObservacao() { return observacao; }
    public List<ItemPedido> getItens() { return itens; }
}
