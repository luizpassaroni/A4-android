package com.trigodourado.app.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.trigodourado.app.R;
import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.model.CategoriaProduto;
import com.trigodourado.app.data.model.Produto;
import com.trigodourado.app.databinding.ActivityCardapioBinding;
import com.trigodourado.app.ui.adapter.ProdutosAdapter;
import com.trigodourado.app.ui.cart.CarrinhoActivity;
import com.trigodourado.app.ui.history.HistoricoActivity;
import com.trigodourado.app.util.WindowInsetsUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class CardapioActivity extends AppCompatActivity {
    private ActivityCardapioBinding binding;
    private CardapioViewModel viewModel;
    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private List<Produto> produtosAtuais = Collections.emptyList();
    private List<CategoriaProduto> categoriasVisiveis = Collections.emptyList();
    private int idCategoriaSelecionada;
    private ProdutosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardapioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowInsetsUtil.aplicarSafeArea(this, binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CardapioViewModel.class);
        adapter = new ProdutosAdapter(produto -> {
            viewModel.adicionarAoCarrinho(produto);
            Snackbar.make(binding.getRoot(), produto.getNome() + " adicionado à sacola",
                            Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.previewSacola)
                    .show();
        });
        binding.listaProdutos.setLayoutManager(new LinearLayoutManager(this));
        binding.listaProdutos.setAdapter(adapter);
        binding.previewSacola.setOnClickListener(v ->
                startActivity(new Intent(this, CarrinhoActivity.class)));
        binding.abrirHistorico.setOnClickListener(v ->
                startActivity(new Intent(this, HistoricoActivity.class)));
        viewModel.getEstadoCarrinho().observe(this, this::renderizarSacola);
        viewModel.getEstadoProdutos().observe(this, this::renderizarProdutos);
        viewModel.getCategorias().observe(this, this::renderizarCategorias);
        binding.categoriasTabs.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position >= 0 && position < categoriasVisiveis.size()) {
                    idCategoriaSelecionada = categoriasVisiveis.get(position).getIdCategoria();
                    aplicarFiltro();
                }
            }
            @Override public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) { }
            @Override public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) { }
        });
    }

    private void renderizarSacola(CartState estado) {
        boolean possuiItens = estado != null && estado.getTotalItens() > 0;
        binding.previewSacola.setVisibility(possuiItens ? View.VISIBLE : View.GONE);
        if (!possuiItens) {
            return;
        }
        binding.totalItens.setText(getResources().getQuantityString(
                com.trigodourado.app.R.plurals.total_itens,
                estado.getTotalItens(),
                estado.getTotalItens()));
        binding.valorTotal.setText(moeda.format(estado.getTotal()));
    }

    private void renderizarProdutos(CardapioState estado) {
        binding.skeletonProdutos.setVisibility(estado.isCarregando() ? View.VISIBLE : View.GONE);
        binding.listaProdutos.setVisibility(estado.isCarregando() ? View.GONE : View.VISIBLE);
        if (estado.isCarregando()) {
            binding.skeletonProdutos.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.skeleton_fade));
        } else {
            binding.skeletonProdutos.clearAnimation();
            produtosAtuais = estado.getProdutos();
            aplicarFiltro();
        }
    }

    private void renderizarCategorias(List<CategoriaProduto> categorias) {
        List<CategoriaProduto> categoriasAtuais = categorias == null ? Collections.emptyList() : categorias;
        List<CategoriaProduto> ativas = new ArrayList<>();
        binding.categoriasTabs.removeAllTabs();
        int selecionada = 0;
        for (CategoriaProduto categoria : categoriasAtuais) {
            if (!categoria.isAtivo()) {
                continue;
            }
            ativas.add(categoria);
            binding.categoriasTabs.addTab(binding.categoriasTabs.newTab().setText(categoria.getNome()));
            if (categoria.getIdCategoria() == idCategoriaSelecionada) {
                selecionada = binding.categoriasTabs.getTabCount() - 1;
            }
        }
        categoriasVisiveis = ativas;
        if (binding.categoriasTabs.getTabAt(selecionada) != null) {
            Objects.requireNonNull(binding.categoriasTabs.getTabAt(selecionada)).select();
        }
    }

    private void aplicarFiltro() {
        if (adapter == null) {
            return;
        }
        if (idCategoriaSelecionada == 0) {
            adapter.atualizarProdutos(produtosAtuais);
            return;
        }
        List<Produto> filtrados = new ArrayList<>();
        for (Produto produto : produtosAtuais) {
            if (produto.getIdCategoria() == idCategoriaSelecionada) {
                filtrados.add(produto);
            }
        }
        adapter.atualizarProdutos(filtrados);
    }
}
