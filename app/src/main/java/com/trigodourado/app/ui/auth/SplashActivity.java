package com.trigodourado.app.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.trigodourado.app.data.model.Usuario;
import com.trigodourado.app.data.repository.AuthRepository;
import com.trigodourado.app.ui.admin.DashboardActivity;
import com.trigodourado.app.ui.menu.CardapioActivity;
import com.trigodourado.app.util.SessionManager;

@SuppressLint("CustomSplashScreen")
public final class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        SessionManager session = new SessionManager(this);
        Intent destino;
        if (!session.possuiSessao()) {
            destino = new Intent(this, LoginActivity.class);
        } else {
            AuthRepository.getInstance().restaurarSessao(new Usuario(
                    session.getIdUsuario(), "", "", "", "", null, null, session.getRole()));
            destino = SessionManager.ROLE_GERENTE.equals(session.getRole())
                    ? new Intent(this, DashboardActivity.class)
                    : new Intent(this, CardapioActivity.class);
        }
        startActivity(destino);
        finish();
    }
}
