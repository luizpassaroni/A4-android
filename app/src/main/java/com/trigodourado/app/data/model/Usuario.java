package com.trigodourado.app.data.model;

public final class Usuario {
    private final int idUsuario;
    private final String nome;
    private final String email;
    private final String senhaHash;
    private final String telefone;
    private final String dataCadastro;
    private final String dataNascimento;
    private final String role;

    public Usuario(int idUsuario, String nome, String email, String senhaHash, String telefone,
                   String dataCadastro, String dataNascimento) {
        this(idUsuario, nome, email, senhaHash, telefone, dataCadastro, dataNascimento, "CLIENTE");
    }

    public Usuario(int idUsuario, String nome, String email, String senhaHash, String telefone,
                   String dataCadastro, String dataNascimento, String role) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
        this.dataNascimento = dataNascimento;
        this.role = role == null ? "CLIENTE" : role;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public String getTelefone() { return telefone; }
    public String getDataCadastro() { return dataCadastro; }
    public String getDataNascimento() { return dataNascimento; }
    public String getRole() { return role; }
}
