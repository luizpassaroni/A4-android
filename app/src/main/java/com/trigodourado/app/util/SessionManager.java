package com.trigodourado.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public final class SessionManager {
    public static final String ROLE_CLIENTE = "CLIENTE";
    public static final String ROLE_GERENTE = "GERENTE";
    private static final String PREFS_NAME = "secure_session";
    private static final String KEY_ID_USUARIO = "id_usuario";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context.getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            preferences = EncryptedSharedPreferences.create(
                    context.getApplicationContext(),
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException exception) {
            throw new IllegalStateException("Não foi possível inicializar a sessão segura.", exception);
        }
    }

    public void salvarSessao(int idUsuario, String role) {
        preferences.edit()
                .putInt(KEY_ID_USUARIO, idUsuario)
                .putString(KEY_ROLE, role == null ? ROLE_CLIENTE : role)
                .apply();
    }

    public boolean possuiSessao() {
        return getIdUsuario() > 0;
    }

    public int getIdUsuario() {
        return preferences.getInt(KEY_ID_USUARIO, 0);
    }

    public String getRole() {
        return preferences.getString(KEY_ROLE, ROLE_CLIENTE);
    }

    public void limparSessao() {
        preferences.edit().clear().apply();
    }
}
