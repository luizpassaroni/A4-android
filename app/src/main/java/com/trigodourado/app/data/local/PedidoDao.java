package com.trigodourado.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.model.StatusPedido;

import java.util.List;

@Dao
public interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long salvar(Pedido pedido);

    @Update
    void atualizar(Pedido pedido);

    @Query("SELECT * FROM pedidos WHERE id_usuario = :idUsuario ORDER BY id_pedido DESC")
    LiveData<List<Pedido>> listarPorUsuario(int idUsuario);

    @Query("SELECT * FROM pedidos WHERE status NOT IN ('ENTREGUE', 'CANCELADO') ORDER BY id_pedido DESC")
    LiveData<List<Pedido>> listarAtivos();

    @Query("UPDATE pedidos SET status = :status WHERE id_pedido = :idPedido")
    void atualizarStatus(int idPedido, StatusPedido status);
}
