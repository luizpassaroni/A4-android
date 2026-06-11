package com.trigodourado.app.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "enderecos")
public final class EnderecoEntrega {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_endereco")
    private final int idEndereco;
    @ColumnInfo(name = "id_usuario")
    private final int idUsuario;
    @ColumnInfo(name = "apelido")
    private final String apelido;
    @ColumnInfo(name = "logradouro")
    private final String logradouro;
    @ColumnInfo(name = "numero")
    private final String numero;
    @ColumnInfo(name = "complemento")
    private final String complemento;
    @ColumnInfo(name = "bairro")
    private final String bairro;
    @ColumnInfo(name = "cidade")
    private final String cidade;
    @ColumnInfo(name = "cep")
    private final String cep;
    @ColumnInfo(name = "referencia")
    private final String referencia;
    @ColumnInfo(name = "ativo")
    private final boolean ativo;

    public EnderecoEntrega(int idEndereco, int idUsuario, String apelido, String logradouro,
                           String numero, String complemento, String bairro, String cidade,
                           String cep, String referencia, boolean ativo) {
        this.idEndereco = idEndereco;
        this.idUsuario = idUsuario;
        this.apelido = apelido;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.referencia = referencia;
        this.ativo = ativo;
    }

    public int getIdEndereco() { return idEndereco; }
    public int getIdUsuario() { return idUsuario; }
    public String getApelido() { return apelido; }
    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getCep() { return cep; }
    public String getReferencia() { return referencia; }
    public boolean isAtivo() { return ativo; }

    public String getEnderecoFormatado() {
        String base = logradouro + ", " + numero;
        return complemento == null || complemento.trim().isEmpty() ? base : base + " - " + complemento;
    }
}
