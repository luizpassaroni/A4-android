package com.trigodourado.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.trigodourado.app.databinding.ActivityLoginBinding;
import com.trigodourado.app.ui.menu.CardapioActivity;
import com.trigodourado.app.ui.admin.DashboardActivity;
import com.trigodourado.app.util.SessionManager;
import com.trigodourado.app.util.WindowInsetsUtil;

public final class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;
    private String ultimoErroExibido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowInsetsUtil.aplicarSafeArea(this, binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.entrar.setOnClickListener(v -> realizarLogin());
        binding.abrirCadastro.setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));
        viewModel.getEstado().observe(this, this::renderizar);
    }

    private void realizarLogin() {
        String email = binding.email.getText().toString().trim();
        String senha = binding.senha.getText().toString();
        viewModel.login(email, senha);
    }

    private void renderizar(AuthState estado) {
        binding.progresso.setVisibility(estado.isCarregando() ? View.VISIBLE : View.GONE);
        binding.entrar.setEnabled(!estado.isCarregando());
        binding.email.setEnabled(!estado.isCarregando());
        binding.senha.setEnabled(!estado.isCarregando());

        if (estado.getMensagemErro() != null
                && !estado.getMensagemErro().equals(ultimoErroExibido)) {
            ultimoErroExibido = estado.getMensagemErro();
            Toast.makeText(this, estado.getMensagemErro(), Toast.LENGTH_SHORT).show();
        }
        if (estado.isSucesso()) {
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.salvarSessao(estado.getUsuario().getIdUsuario(), estado.getUsuario().getRole());
            Intent intent = SessionManager.ROLE_GERENTE.equals(estado.getUsuario().getRole())
                    ? new Intent(this, DashboardActivity.class)
                    : new Intent(this, CardapioActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
