package com.trigodourado.app.data.repository;

import com.trigodourado.app.data.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class AuthRepository {
    private static final AuthRepository INSTANCE = new AuthRepository();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicInteger proximoId = new AtomicInteger(1);
    private final List<Usuario> usuarios = new ArrayList<>();
    private volatile Usuario usuarioAutenticado;

    private AuthRepository() {
        usuarios.add(new Usuario(
                proximoId.getAndIncrement(), "Gerente", "gerente@trigodourado.com",
                gerarHash("gerente123"), "", LocalDateTime.now().toString(), null, "GERENTE"));
    }

    public static AuthRepository getInstance() {
        return INSTANCE;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void restaurarSessao(Usuario usuario) {
        usuarioAutenticado = usuario;
    }

    public void login(String email, String senha, RepositoryCallback<Usuario> callback) {
        executor.execute(() -> {
            simularLatencia();
            if (vazio(email) || vazio(senha)) {
                callback.onError("Informe e-mail e senha.");
                return;
            }
            String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
            String senhaHash = gerarHash(senha);
            synchronized (usuarios) {
                for (Usuario usuario : usuarios) {
                    if (usuario.getEmail().equalsIgnoreCase(emailNormalizado)
                            && usuario.getSenhaHash().equals(senhaHash)) {
                        usuarioAutenticado = usuario;
                        callback.onSuccess(usuario);
                        return;
                    }
                }
            }
            callback.onError("E-mail ou senha inválidos.");
        });
    }

    public void cadastrar(Usuario usuario, RepositoryCallback<Usuario> callback) {
        executor.execute(() -> {
            simularLatencia();
            if (usuario == null || vazio(usuario.getNome()) || vazio(usuario.getEmail())
                    || vazio(usuario.getSenhaHash())) {
                callback.onError("Nome, e-mail e senha são obrigatórios.");
                return;
            }
            String emailNormalizado = usuario.getEmail().trim().toLowerCase(Locale.ROOT);
            synchronized (usuarios) {
                for (Usuario existente : usuarios) {
                    if (existente.getEmail().equalsIgnoreCase(emailNormalizado)) {
                        callback.onError("Já existe uma conta com este e-mail.");
                        return;
                    }
                }
                Usuario cadastrado = new Usuario(
                        proximoId.getAndIncrement(),
                        usuario.getNome().trim(),
                        emailNormalizado,
                        gerarHash(usuario.getSenhaHash()),
                        usuario.getTelefone(),
                        LocalDateTime.now().toString(),
                        usuario.getDataNascimento()
                        , usuario.getRole()
                );
                usuarios.add(cadastrado);
                usuarioAutenticado = cadastrado;
                callback.onSuccess(cadastrado);
            }
        });
    }

    private String gerarHash(String senha) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder valor = new StringBuilder();
            for (byte parte : hash) {
                valor.append(String.format(Locale.ROOT, "%02x", parte));
            }
            return valor.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível.", e);
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void simularLatencia() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
