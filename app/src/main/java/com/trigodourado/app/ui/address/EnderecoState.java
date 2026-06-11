package com.trigodourado.app.ui.address;

import com.trigodourado.app.data.model.EnderecoEntrega;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EnderecoState {
    private final boolean carregando;
    private final List<EnderecoEntrega> enderecos;
    private final String mensagem;
    private final boolean erro;

    private EnderecoState(boolean carregando, List<EnderecoEntrega> enderecos,
                          String mensagem, boolean erro) {
        this.carregando = carregando;
        this.enderecos = Collections.unmodifiableList(new ArrayList<>(enderecos));
        this.mensagem = mensagem;
        this.erro = erro;
    }

    public static EnderecoState inicial() {
        return new EnderecoState(false, Collections.emptyList(), null, false);
    }

    public static EnderecoState carregando(List<EnderecoEntrega> atuais) {
        return new EnderecoState(true, atuais, null, false);
    }

    public static EnderecoState sucesso(List<EnderecoEntrega> enderecos, String mensagem) {
        return new EnderecoState(false, enderecos, mensagem, false);
    }

    public static EnderecoState erro(List<EnderecoEntrega> atuais, String mensagem) {
        return new EnderecoState(false, atuais, mensagem, true);
    }

    public boolean isCarregando() { return carregando; }
    public List<EnderecoEntrega> getEnderecos() { return enderecos; }
    public String getMensagem() { return mensagem; }
    public boolean isErro() { return erro; }
}
