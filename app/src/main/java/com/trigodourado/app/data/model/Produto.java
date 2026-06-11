package com.trigodourado.app.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "produtos")
public final class Produto {
    @PrimaryKey
    @ColumnInfo(name = "id_produto")
    private final int idProduto;
    @ColumnInfo(name = "id_categoria")
    private final int idCategoria;
    @ColumnInfo(name = "nome")
    private final String nome;
    @ColumnInfo(name = "descricao")
    private final String descricao;
    @ColumnInfo(name = "preco")
    private final double preco;
    @ColumnInfo(name = "imagem")
    private final String imagem;
    @ColumnInfo(name = "ativo")
    private final boolean ativo;
    @ColumnInfo(name = "data_cadastro")
    private final String dataCadastro;

    public Produto(int idProduto, int idCategoria, String nome, String descricao, double preco,
                   String imagem, boolean ativo, String dataCadastro) {
        this.idProduto = idProduto;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

    public int getIdProduto() { return idProduto; }
    public int getIdCategoria() { return idCategoria; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public String getImagem() { return imagem; }
    public boolean isAtivo() { return ativo; }
    public String getDataCadastro() { return dataCadastro; }
}
