package com.trigodourado.app.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trigodourado.app.data.local.AppDatabase;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.repository.PedidoRepository;
import com.trigodourado.app.util.SessionManager;

import java.util.List;

public final class HistoricoViewModel extends AndroidViewModel {
    private final LiveData<List<Pedido>> pedidos;

    public HistoricoViewModel(@NonNull Application application) {
        super(application);
        PedidoRepository repository =
                new PedidoRepository(AppDatabase.getInstance(application).pedidoDao());
        pedidos = repository.listarPorUsuario(new SessionManager(application).getIdUsuario());
    }

    public LiveData<List<Pedido>> getPedidos() { return pedidos; }
}
