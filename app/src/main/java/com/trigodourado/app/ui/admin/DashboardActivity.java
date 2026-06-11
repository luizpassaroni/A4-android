package com.trigodourado.app.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.trigodourado.app.databinding.ActivityDashboardBinding;
import com.trigodourado.app.ui.menu.CardapioActivity;
import com.trigodourado.app.util.SessionManager;
import com.trigodourado.app.util.WindowInsetsUtil;

public final class DashboardActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager session = new SessionManager(this);
        if (!session.possuiSessao() || !SessionManager.ROLE_GERENTE.equals(session.getRole())) {
            startActivity(new Intent(this, CardapioActivity.class));
            finish();
            return;
        }
        ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowInsetsUtil.aplicarSafeArea(this, binding.getRoot());
        binding.dashboardPager.setAdapter(new DashboardPagerAdapter(this));
        new TabLayoutMediator(binding.dashboardTabs, binding.dashboardPager,
                (tab, position) -> tab.setText(position == 0 ? "Pedidos Ativos" : "Controle de Estoque"))
                .attach();
    }
}
