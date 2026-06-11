package com.trigodourado.app.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.trigodourado.app.data.local.PedidoDao;
import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.Entrega;
import com.trigodourado.app.data.model.FormaPagamento;
import com.trigodourado.app.data.model.Pagamento;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.model.PedidoFinalizado;
import com.trigodourado.app.data.model.StatusEntrega;
import com.trigodourado.app.data.model.StatusPagamento;
import com.trigodourado.app.data.model.StatusPedido;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PedidoRepository {
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final PedidoDao pedidoDao;

    public PedidoRepository(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    public LiveData<List<Pedido>> listarPorUsuario(int idUsuario) {
        return pedidoDao.listarPorUsuario(idUsuario);
    }

    public LiveData<List<Pedido>> listarAtivos() {
        return pedidoDao.listarAtivos();
    }

    public void atualizarStatus(Pedido pedido) {
        StatusPedido proximo = proximoStatus(pedido.getStatus());
        IO_EXECUTOR.execute(() -> pedidoDao.atualizarStatus(pedido.getIdPedido(), proximo));
    }

    public void confirmarPedido(Pedido pedido, EnderecoEntrega endereco,
                                RepositoryCallback<PedidoFinalizado> callback) {
        if (pedido == null || endereco == null) {
            callback.onError("Dados do pedido incompletos.");
            return;
        }
        handler.postDelayed(() -> IO_EXECUTOR.execute(() -> {
            long id = pedidoDao.salvar(pedido);
            Pedido persistido = new Pedido((int) id, pedido.getIdUsuario(), pedido.getDataPedido(),
                    pedido.getStatus(), pedido.getFormaPagamento(), pedido.getSubtotal(),
                    pedido.getTaxaEntrega(), pedido.getDesconto(), pedido.getTotal(),
                    pedido.getObservacao(), pedido.getItens());
            FormaPagamento forma = pedido.getFormaPagamento();
            Pagamento pagamento = new Pagamento((int) id, (int) id, forma,
                    StatusPagamento.PENDENTE, pedido.getTotal(), null, null);
            Entrega entrega = new Entrega((int) id, (int) id, endereco.getEnderecoFormatado(),
                    endereco.getBairro(), endereco.getCep(), pedido.getTaxaEntrega(),
                    StatusEntrega.AGUARDANDO, null, null, null);
            handler.post(() -> callback.onSuccess(new PedidoFinalizado(persistido, pagamento, entrega)));
        }), 2000);
    }

    private StatusPedido proximoStatus(StatusPedido atual) {
        if (atual == StatusPedido.RECEBIDO) return StatusPedido.EM_PREPARO;
        if (atual == StatusPedido.EM_PREPARO) return StatusPedido.SAINDO_PARA_ENTREGA;
        if (atual == StatusPedido.SAINDO_PARA_ENTREGA) return StatusPedido.ENTREGUE;
        return atual;
    }
}
