package com.trigodourado.app.data.model;

public final class CategoriaProduto {
    private final int idCategoria;
    private final String nome;
    private final String descricao;
    private final String imagem;
    private final boolean ativo;

    public CategoriaProduto(int idCategoria, String nome, String descricao,
                            String imagem, boolean ativo) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.ativo = ativo;
    }

    public int getIdCategoria() { return idCategoria; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getImagem() { return imagem; }
    public boolean isAtivo() { return ativo; }
}
