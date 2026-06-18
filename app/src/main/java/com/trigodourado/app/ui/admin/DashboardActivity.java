package com.trigodourado.app.ui.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.trigodourado.app.databinding.ActivityDashboardBinding;
import com.trigodourado.app.util.SessionDestination;
import com.trigodourado.app.util.SessionNavigator;
import com.trigodourado.app.util.WindowInsetsUtil;

public final class DashboardActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionNavigator.redirectIfNeeded(this, SessionDestination.DASHBOARD)) {
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
