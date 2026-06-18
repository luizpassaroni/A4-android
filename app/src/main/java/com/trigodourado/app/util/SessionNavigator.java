package com.trigodourado.app.util;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.trigodourado.app.data.model.Usuario;
import com.trigodourado.app.data.repository.AuthRepository;
import com.trigodourado.app.ui.admin.DashboardActivity;
import com.trigodourado.app.ui.auth.LoginActivity;
import com.trigodourado.app.ui.menu.CardapioActivity;

import java.util.Locale;

public final class SessionNavigator {
    private SessionNavigator() {
    }

    public static SessionDestination resolveDestination(int idUsuario, String role) {
        if (idUsuario <= 0) {
            return SessionDestination.LOGIN;
        }
        switch (normalizarRole(role)) {
            case SessionManager.ROLE_CLIENTE:
                return SessionDestination.CARDAPIO;
            case SessionManager.ROLE_GERENTE:
                return SessionDestination.DASHBOARD;
            default:
                return SessionDestination.LOGIN;
        }
    }

    public static SessionDestination resolveDestination(SessionManager sessionManager) {
        return resolveDestination(sessionManager.getIdUsuario(), sessionManager.getRole());
    }

    public static void openResolvedDestination(AppCompatActivity activity) {
        SessionManager sessionManager = SessionManager.getInstance(activity);
        SessionDestination destino = resolveDestination(sessionManager);
        sincronizarSessao(sessionManager, destino);
        activity.startActivity(createIntent(activity, destino));
        activity.finish();
    }

    public static boolean redirectIfNeeded(AppCompatActivity activity,
                                           SessionDestination expectedDestination) {
        SessionManager sessionManager = SessionManager.getInstance(activity);
        SessionDestination destinoAtual = resolveDestination(sessionManager);
        sincronizarSessao(sessionManager, destinoAtual);
        if (destinoAtual == expectedDestination) {
            return false;
        }
        activity.startActivity(createIntent(activity, destinoAtual));
        activity.finish();
        return true;
    }

    public static Intent createIntent(Context context, SessionDestination destination) {
        switch (destination) {
            case DASHBOARD:
                return new Intent(context, DashboardActivity.class);
            case CARDAPIO:
                return new Intent(context, CardapioActivity.class);
            case LOGIN:
            default:
                return new Intent(context, LoginActivity.class);
        }
    }

    private static void sincronizarSessao(SessionManager sessionManager,
                                          SessionDestination destination) {
        if (destination == SessionDestination.LOGIN) {
            if (sessionManager.getIdUsuario() > 0) {
                sessionManager.limparSessao();
            }
            return;
        }
        AuthRepository.getInstance().restaurarSessao(new Usuario(
                sessionManager.getIdUsuario(), "", "", "", "", null, null,
                normalizarRole(sessionManager.getRole())));
    }

    private static String normalizarRole(String role) {
        return role == null ? "" : role.trim().toUpperCase(Locale.ROOT);
    }
}
