package com.trigodourado.app.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SessionNavigatorTest {
    @Test
    public void resolveDestination_semSessao_redirecionaParaLogin() {
        assertEquals(SessionDestination.LOGIN,
                SessionNavigator.resolveDestination(0, SessionManager.ROLE_CLIENTE));
    }

    @Test
    public void resolveDestination_cliente_redirecionaParaCardapio() {
        assertEquals(SessionDestination.CARDAPIO,
                SessionNavigator.resolveDestination(7, SessionManager.ROLE_CLIENTE));
    }

    @Test
    public void resolveDestination_gerenteComRoleMinuscula_redirecionaParaDashboard() {
        assertEquals(SessionDestination.DASHBOARD,
                SessionNavigator.resolveDestination(9, " gerente "));
    }

    @Test
    public void resolveDestination_roleDesconhecida_redirecionaParaLogin() {
        assertEquals(SessionDestination.LOGIN,
                SessionNavigator.resolveDestination(5, "OPERADOR"));
    }
}
