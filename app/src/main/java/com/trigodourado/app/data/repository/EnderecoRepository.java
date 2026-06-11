package com.trigodourado.app.data.repository;

import androidx.lifecycle.LiveData;

import com.trigodourado.app.data.local.EnderecoDao;
import com.trigodourado.app.data.model.EnderecoEntrega;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class EnderecoRepository {
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Set<String> BAIRROS_ATENDIDOS = Set.of("copacabana", "ipanema", "leblon", "botafogo", "flamengo", "tijuca");

    private final EnderecoDao enderecoDao;

    public EnderecoRepository(EnderecoDao enderecoDao) {
        this.enderecoDao = enderecoDao;
    }

    public LiveData<List<EnderecoEntrega>> listar(int idUsuario) {
        return enderecoDao.listarAtivosPorUsuario(idUsuario);
    }

    public void cadastrar(int idUsuario, EnderecoEntrega endereco,
                          RepositoryCallback<EnderecoEntrega> callback) {
        IO_EXECUTOR.execute(() -> {
            if (idUsuario <= 0) {
                callback.onError("Faça login para cadastrar um endereço.");
                return;
            }
            if (endereco == null || vazio(endereco.getLogradouro()) || vazio(endereco.getNumero())
                    || vazio(endereco.getBairro()) || vazio(endereco.getCep())) {
                callback.onError("Logradouro, número, bairro e CEP são obrigatórios.");
                return;
            }
            if (!BAIRROS_ATENDIDOS.contains(endereco.getBairro().trim().toLowerCase(Locale.ROOT))) {
                callback.onError("O bairro informado ainda não está na área de entrega.");
                return;
            }
            EnderecoEntrega novoEndereco = new EnderecoEntrega(
                    0, idUsuario, endereco.getApelido(), endereco.getLogradouro(),
                    endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(),
                    endereco.getCidade(), endereco.getCep(), endereco.getReferencia(), true
            );
            long id = enderecoDao.salvar(novoEndereco);
            callback.onSuccess(new EnderecoEntrega(
                    (int) id, idUsuario, novoEndereco.getApelido(), novoEndereco.getLogradouro(),
                    novoEndereco.getNumero(), novoEndereco.getComplemento(), novoEndereco.getBairro(),
                    novoEndereco.getCidade(), novoEndereco.getCep(), novoEndereco.getReferencia(), true
            ));
        });
    }

    public void remover(EnderecoEntrega endereco, RepositoryCallback<Void> callback) {
        IO_EXECUTOR.execute(() -> {
            enderecoDao.remover(endereco);
            callback.onSuccess(null);
        });
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
