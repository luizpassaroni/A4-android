package com.trigodourado.app.ui.auth;

import com.trigodourado.app.data.model.Usuario;

public final class AuthState {
    private final boolean carregando;
    private final Usuario usuario;
    private final String mensagemErro;

    private AuthState(boolean carregando, Usuario usuario, String mensagemErro) {
        this.carregando = carregando;
        this.usuario = usuario;
        this.mensagemErro = mensagemErro;
    }

    public static AuthState inicial() { return new AuthState(false, null, null); }
    public static AuthState carregando() { return new AuthState(true, null, null); }
    public static AuthState sucesso(Usuario usuario) { return new AuthState(false, usuario, null); }
    public static AuthState erro(String mensagem) { return new AuthState(false, null, mensagem); }

    public boolean isCarregando() { return carregando; }
    public Usuario getUsuario() { return usuario; }
    public String getMensagemErro() { return mensagemErro; }
    public boolean isSucesso() { return usuario != null; }
}
