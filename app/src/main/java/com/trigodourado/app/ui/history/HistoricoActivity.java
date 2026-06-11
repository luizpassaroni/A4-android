package com.trigodourado.app.ui.history;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trigodourado.app.databinding.ActivityHistoricoBinding;
import com.trigodourado.app.util.WindowInsetsUtil;

public final class HistoricoActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHistoricoBinding binding = ActivityHistoricoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowInsetsUtil.aplicarSafeArea(this, binding.getRoot());
        PedidoAdapter adapter = new PedidoAdapter();
        binding.listaPedidos.setLayoutManager(new LinearLayoutManager(this));
        binding.listaPedidos.setAdapter(adapter);
        new ViewModelProvider(this).get(HistoricoViewModel.class)
                .getPedidos().observe(this, adapter::atualizar);
    }
}
