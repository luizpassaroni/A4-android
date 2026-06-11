package com.trigodourado.app.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trigodourado.app.databinding.FragmentListaAdminBinding;

public final class EstoqueFragment extends Fragment {
    private FragmentListaAdminBinding binding;
    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle state) {
        binding = FragmentListaAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        AdminViewModel vm = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);
        EstoqueAdapter adapter = new EstoqueAdapter(vm::atualizarAtivo);
        binding.listaAdmin.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listaAdmin.setAdapter(adapter);
        vm.getProdutos().observe(getViewLifecycleOwner(), adapter::atualizar);
    }
    @Override public void onDestroyView() { binding = null; super.onDestroyView(); }
}
