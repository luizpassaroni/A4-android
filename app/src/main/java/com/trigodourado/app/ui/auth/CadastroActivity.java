package com.trigodourado.app.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.trigodourado.app.data.model.Usuario;
import com.trigodourado.app.databinding.ActivityCadastroBinding;
import com.trigodourado.app.util.WindowInsetsUtil;

public final class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;
    private AuthViewModel viewModel;
    private String ultimoErroExibido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowInsetsUtil.aplicarSafeArea(this, binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.registrar.setOnClickListener(v -> registrarConta());
        viewModel.getEstado().observe(this, this::renderizar);
    }

    private void registrarConta() {
        Usuario usuario = new Usuario(
                0,
                binding.nome.getText().toString().trim(),
                binding.email.getText().toString().trim(),
                binding.senha.getText().toString(),
                binding.telefone.getText().toString().trim(),
                null,
                null
        );
        viewModel.cadastrar(usuario);
    }

    private void renderizar(AuthState estado) {
        binding.progresso.setVisibility(estado.isCarregando() ? View.VISIBLE : View.GONE);
        binding.registrar.setEnabled(!estado.isCarregando());
        binding.nome.setEnabled(!estado.isCarregando());
        binding.email.setEnabled(!estado.isCarregando());
        binding.senha.setEnabled(!estado.isCarregando());
        binding.telefone.setEnabled(!estado.isCarregando());

        if (estado.getMensagemErro() != null
                && !estado.getMensagemErro().equals(ultimoErroExibido)) {
            ultimoErroExibido = estado.getMensagemErro();
            Toast.makeText(this, estado.getMensagemErro(), Toast.LENGTH_SHORT).show();
        }
        if (estado.isSucesso()) {
            Toast.makeText(this, "Conta criada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
