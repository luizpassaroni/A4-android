package com.trigodourado.app.data.local;

import androidx.room.TypeConverter;

import com.trigodourado.app.data.model.FormaPagamento;
import com.trigodourado.app.data.model.StatusPedido;

public final class RoomConverters {
    @TypeConverter
    public static String statusPedidoParaString(StatusPedido status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static StatusPedido stringParaStatusPedido(String status) {
        return status == null ? null : StatusPedido.valueOf(status);
    }

    @TypeConverter
    public static String formaPagamentoParaString(FormaPagamento forma) {
        return forma == null ? null : forma.name();
    }

    @TypeConverter
    public static FormaPagamento stringParaFormaPagamento(String forma) {
        return forma == null ? null : FormaPagamento.valueOf(forma);
    }
}
