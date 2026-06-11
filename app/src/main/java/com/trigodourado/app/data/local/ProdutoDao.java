package com.trigodourado.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.trigodourado.app.data.model.Produto;

import java.util.List;

@Dao
public interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void inserirTodos(List<Produto> produtos);

    @Query("SELECT * FROM produtos ORDER BY id_produto")
    LiveData<List<Produto>> listarTodos();

    @Query("SELECT * FROM produtos WHERE ativo = 1 ORDER BY id_produto")
    LiveData<List<Produto>> listarAtivos();

    @Query("UPDATE produtos SET ativo = :ativo WHERE id_produto = :idProduto")
    void atualizarAtivo(int idProduto, boolean ativo);
}
