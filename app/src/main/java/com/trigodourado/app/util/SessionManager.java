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

    private static volatile SessionManager instance;

    private final SharedPreferences preferences;

    private SessionManager(Context context) {
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

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager(context.getApplicationContext());
                }
            }
        }
        return instance;
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

    /**
     * Encerra a sessão atual, removendo todos os dados armazenados.
     * Atende ao Contexto Técnico da issue (encerrarSessao()).
     */
    public void encerrarSessao() {
        limparSessao();
    }

    /**
     * Mantido por compatibilidade com chamadas já existentes no código.
     */
    public void limparSessao() {
        preferences.edit().clear().apply();
    }
}
