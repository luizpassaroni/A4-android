package com.trigodourado.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.trigodourado.app.data.model.EnderecoEntrega;

import java.util.List;

@Dao
public interface EnderecoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long salvar(EnderecoEntrega endereco);

    @Delete
    void remover(EnderecoEntrega endereco);

    @Query("SELECT * FROM enderecos WHERE id_usuario = :idUsuario AND ativo = 1")
    LiveData<List<EnderecoEntrega>> listarAtivosPorUsuario(int idUsuario);
}
