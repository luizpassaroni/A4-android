package com.trigodourado.app.ui.address;

import com.trigodourado.app.data.api.ViaCepResponse;

public final class ViaCepState {
    private final boolean carregando;
    private final ViaCepResponse endereco;
    private final String mensagemErro;

    private ViaCepState(boolean carregando, ViaCepResponse endereco, String mensagemErro) {
        this.carregando = carregando;
        this.endereco = endereco;
        this.mensagemErro = mensagemErro;
    }

    public static ViaCepState inicial() { return new ViaCepState(false, null, null); }
    public static ViaCepState carregando() { return new ViaCepState(true, null, null); }
    public static ViaCepState sucesso(ViaCepResponse endereco) {
        return new ViaCepState(false, endereco, null);
    }
    public static ViaCepState erro(String mensagem) {
        return new ViaCepState(false, null, mensagem);
    }

    public boolean isCarregando() { return carregando; }
    public ViaCepResponse getEndereco() { return endereco; }
    public String getMensagemErro() { return mensagemErro; }
}
