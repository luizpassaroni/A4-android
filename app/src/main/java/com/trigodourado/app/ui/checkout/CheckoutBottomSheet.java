package com.trigodourado.app.ui.checkout;

import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.trigodourado.app.R;
import com.trigodourado.app.data.model.CartState;
import com.trigodourado.app.data.api.ViaCepResponse;
import com.trigodourado.app.data.model.EnderecoEntrega;
import com.trigodourado.app.data.model.FormaPagamento;
import com.trigodourado.app.databinding.DialogCheckoutBinding;
import com.trigodourado.app.ui.address.EnderecoState;
import com.trigodourado.app.ui.address.EnderecoViewModel;
import com.trigodourado.app.ui.address.ViaCepState;
import com.trigodourado.app.ui.menu.CardapioActivity;
import com.trigodourado.app.util.CepMaskUtil;
import com.trigodourado.app.util.WindowInsetsUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CheckoutBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "CheckoutBottomSheet";

    private DialogCheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private EnderecoViewModel enderecoViewModel;
    private final List<EnderecoEntrega> enderecos = new ArrayList<>();
    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private CartState carrinhoAtual;
    private String ultimoErro;
    private boolean cepValido;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogCheckoutBinding.inflate(inflater, container, false);
        WindowInsetsUtil.aplicarSafeAreaComTeclado(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        enderecoViewModel = new ViewModelProvider(this).get(EnderecoViewModel.class);

        configurarPagamentos();
        configurarEnderecos();
        configurarBuscaCep();
        observarEstados();
        binding.confirmarPedido.setOnClickListener(v -> {
            int feedback = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                    ? HapticFeedbackConstants.CONFIRM
                    : HapticFeedbackConstants.VIRTUAL_KEY;
            v.performHapticFeedback(feedback);
            checkoutViewModel.confirmarPedido();
        });
        binding.salvarEndereco.setOnClickListener(v -> salvarEndereco());
        enderecoViewModel.listar();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() instanceof BottomSheetDialog) {
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            View sheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (sheet != null) {
                sheet.setBackgroundResource(android.R.color.transparent);
                BottomSheetBehavior.from(sheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    private void configurarPagamentos() {
        binding.pagamentoPix.setChecked(true);
        binding.formasPagamento.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                return;
            }
            if (checkedId == R.id.pagamento_cartao) {
                checkoutViewModel.selecionarFormaPagamento(FormaPagamento.CARTAO_CREDITO);
            } else if (checkedId == R.id.pagamento_dinheiro) {
                checkoutViewModel.selecionarFormaPagamento(FormaPagamento.DINHEIRO);
            } else {
                checkoutViewModel.selecionarFormaPagamento(FormaPagamento.PIX);
            }
        });
    }

    private void configurarEnderecos() {
        binding.enderecoEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < enderecos.size()) {
                    checkoutViewModel.selecionarEndereco(enderecos.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void configurarBuscaCep() {
        binding.cep.addTextChangedListener(new CepMaskUtil(new CepMaskUtil.OnCepCompleteListener() {
            @Override
            public void onCepComplete(String cep) {
                ocultarTeclado(binding.cep);
                enderecoViewModel.buscarCep(cep);
            }

            @Override
            public void onCepIncomplete() {
                cepValido = false;
                binding.salvarEndereco.setEnabled(false);
                limparCamposOficiais();
                enderecoViewModel.limparBuscaCep();
            }
        }));
    }

    private void observarEstados() {
        checkoutViewModel.getCarrinho().observe(getViewLifecycleOwner(), this::renderizarCarrinho);
        checkoutViewModel.getEstado().observe(getViewLifecycleOwner(), this::renderizarCheckout);
        enderecoViewModel.getEstado().observe(getViewLifecycleOwner(), this::renderizarEnderecos);
        enderecoViewModel.getEstadoCep().observe(getViewLifecycleOwner(), this::renderizarViaCep);
    }

    private void renderizarCarrinho(CartState carrinho) {
        carrinhoAtual = carrinho;
        binding.subtotal.setText(getString(R.string.subtotal_valor,
                moeda.format(carrinho.getSubtotal())));
        binding.taxaEntrega.setText(getString(R.string.taxa_entrega_valor,
                moeda.format(carrinho.getTaxaEntrega())));
        binding.totalPagar.setText(moeda.format(carrinho.getTotal()));
        boolean invalido = carrinho.getEnderecoSelecionado() != null && !carrinho.isAreaAtendida();
        binding.avisoArea.setVisibility(invalido ? View.VISIBLE : View.GONE);
        atualizarBotao();
    }

    private void renderizarEnderecos(EnderecoState estado) {
        binding.progressoEnderecos.setVisibility(estado.isCarregando() ? View.VISIBLE : View.GONE);
        enderecos.clear();
        enderecos.addAll(estado.getEnderecos());
        List<String> rotulos = new ArrayList<>();
        for (EnderecoEntrega endereco : enderecos) {
            rotulos.add(endereco.getApelido() + " · " + endereco.getEnderecoFormatado()
                    + " · " + endereco.getBairro());
        }
        if (rotulos.isEmpty()) {
            rotulos.add(getString(R.string.nenhum_endereco));
        }
        binding.enderecoEntrega.setAdapter(new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, rotulos));
        binding.enderecoEntrega.setEnabled(!enderecos.isEmpty());
        if ("Endereço cadastrado.".equals(estado.getMensagem()) && !enderecos.isEmpty()) {
            binding.enderecoEntrega.setSelection(enderecos.size() - 1);
            limparFormularioEndereco();
        }
        if (estado.isErro() && estado.getMensagem() != null) {
            Toast.makeText(requireContext(), estado.getMensagem(), Toast.LENGTH_SHORT).show();
        }
        atualizarBotao();
    }

    private void renderizarViaCep(ViaCepState estado) {
        binding.progressoCep.setVisibility(estado.isCarregando() ? View.VISIBLE : View.GONE);
        binding.cep.setEnabled(!estado.isCarregando());
        if (estado.isCarregando()) {
            cepValido = false;
            binding.salvarEndereco.setEnabled(false);
            limparCamposOficiais();
            return;
        }
        ViaCepResponse endereco = estado.getEndereco();
        if (endereco != null) {
            cepValido = true;
            binding.logradouro.setText(endereco.getLogradouro());
            binding.bairro.setText(endereco.getBairro());
            binding.cidade.setText(endereco.getLocalidade());
            binding.complemento.setText(endereco.getComplemento());
            binding.salvarEndereco.setEnabled(true);
            binding.numero.requestFocus();
        } else if (estado.getMensagemErro() != null) {
            cepValido = false;
            binding.salvarEndereco.setEnabled(false);
            limparCamposOficiais();
            Toast.makeText(requireContext(), estado.getMensagemErro(), Toast.LENGTH_SHORT).show();
        }
    }

    private void renderizarCheckout(CheckoutState estado) {
        binding.progressoPedido.setVisibility(estado.isProcessando() ? View.VISIBLE : View.GONE);
        atualizarBotao();
        if (estado.getMensagemErro() != null && !estado.getMensagemErro().equals(ultimoErro)) {
            ultimoErro = estado.getMensagemErro();
            Toast.makeText(requireContext(), estado.getMensagemErro(), Toast.LENGTH_SHORT).show();
        }
        if (estado.getPedidoFinalizado() != null) {
            int idPedido = estado.getPedidoFinalizado().getPedido().getIdPedido();
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.pedido_confirmado)
                    .setMessage(getString(R.string.pedido_recebido, idPedido))
                    .setCancelable(false)
                    .setPositiveButton(R.string.voltar_cardapio, (dialog, which) -> voltarAoCardapio())
                    .show();
        }
    }

    private void atualizarBotao() {
        CheckoutState checkout = checkoutViewModel == null ? null : checkoutViewModel.getEstado().getValue();
        boolean processando = checkout != null && checkout.isProcessando();
        boolean valido = carrinhoAtual != null
                && !carrinhoAtual.getItens().isEmpty()
                && carrinhoAtual.getEnderecoSelecionado() != null
                && carrinhoAtual.isAreaAtendida()
                && !enderecos.isEmpty();
        binding.confirmarPedido.setEnabled(valido && !processando);
        binding.formasPagamento.setEnabled(!processando);
        binding.enderecoEntrega.setEnabled(!processando && !enderecos.isEmpty());
    }

    private void voltarAoCardapio() {
        Intent intent = new Intent(requireContext(), CardapioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        requireActivity().finish();
        dismissAllowingStateLoss();
    }

    private void salvarEndereco() {
        if (!cepValido) {
            Toast.makeText(requireContext(), R.string.consulte_cep_valido, Toast.LENGTH_SHORT).show();
            return;
        }
        String numero = texto(binding.numero);
        if (numero.isEmpty()) {
            binding.numero.setError(getString(R.string.informe_numero));
            binding.numero.requestFocus();
            return;
        }
        EnderecoEntrega endereco = new EnderecoEntrega(
                0, 0, "Entrega", texto(binding.logradouro), numero, texto(binding.complemento),
                texto(binding.bairro), texto(binding.cidade), texto(binding.cep), "", true);
        enderecoViewModel.cadastrar(endereco);
    }

    private void limparCamposOficiais() {
        binding.logradouro.setText("");
        binding.bairro.setText("");
        binding.cidade.setText("");
    }

    private void limparFormularioEndereco() {
        cepValido = false;
        binding.cep.setText("");
        limparCamposOficiais();
        binding.numero.setText("");
        binding.complemento.setText("");
        binding.salvarEndereco.setEnabled(false);
    }

    private String texto(android.widget.TextView campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }

    private void ocultarTeclado(View view) {
        InputMethodManager teclado =
                (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        teclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
