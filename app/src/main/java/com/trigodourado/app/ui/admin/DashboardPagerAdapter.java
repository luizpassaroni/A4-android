package com.trigodourado.app.ui.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public final class DashboardPagerAdapter extends FragmentStateAdapter {
    public DashboardPagerAdapter(FragmentActivity activity) { super(activity); }
    @NonNull @Override public Fragment createFragment(int position) {
        return position == 0 ? new PedidosAtivosFragment() : new EstoqueFragment();
    }
    @Override public int getItemCount() { return 2; }
}
