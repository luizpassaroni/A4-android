package com.trigodourado.app.util;

import android.text.Editable;
import android.text.TextWatcher;

public final class CepMaskUtil implements TextWatcher {
    public interface OnCepCompleteListener {
        void onCepComplete(String cepNumerico);

        default void onCepIncomplete() {
        }
    }

    private final OnCepCompleteListener listener;
    private boolean atualizando;
    private String ultimoCepConsultado = "";

    public CepMaskUtil(OnCepCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (atualizando) {
            return;
        }
        String digitos = editable.toString().replaceAll("\\D", "");
        if (digitos.length() > 8) {
            digitos = digitos.substring(0, 8);
        }
        String formatado = digitos.length() > 5
                ? digitos.substring(0, 5) + "-" + digitos.substring(5)
                : digitos;
        if (!formatado.equals(editable.toString())) {
            atualizando = true;
            editable.replace(0, editable.length(), formatado);
            atualizando = false;
        }
        if (digitos.length() == 8 && !digitos.equals(ultimoCepConsultado)) {
            ultimoCepConsultado = digitos;
            listener.onCepComplete(digitos);
        } else if (digitos.length() < 8) {
            ultimoCepConsultado = "";
            listener.onCepIncomplete();
        }
    }
}
