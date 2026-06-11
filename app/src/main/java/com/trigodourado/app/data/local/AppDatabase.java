package com.trigodourado.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.Pedido;
import com.trigodourado.app.data.model.Produto;

@Database(entities = {EnderecoEntrega.class, Pedido.class, Produto.class}, version = 2, exportSchema = false)
@TypeConverters(RoomConverters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "trigo_dourado.db";
    private static volatile AppDatabase instance;

    public abstract EnderecoDao enderecoDao();
    public abstract PedidoDao pedidoDao();
    public abstract ProdutoDao produtoDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `pedidos` (`id_pedido` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_usuario` INTEGER NOT NULL, `data_pedido` TEXT, `status` TEXT, `forma_pagamento` TEXT, `subtotal` REAL NOT NULL, `taxa_entrega` REAL NOT NULL, `desconto` REAL NOT NULL, `total` REAL NOT NULL, `observacao` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `produtos` (`id_produto` INTEGER NOT NULL, `id_categoria` INTEGER NOT NULL, `nome` TEXT, `descricao` TEXT, `preco` REAL NOT NULL, `imagem` TEXT, `ativo` INTEGER NOT NULL, `data_cadastro` TEXT, PRIMARY KEY(`id_produto`))");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    ).addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return instance;
    }
}
