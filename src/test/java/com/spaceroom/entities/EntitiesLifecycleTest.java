package com.spaceroom.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntitiesLifecycleTest {

    @Test
    void testReservaPrePersistDefineStatusPadrao() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = Reserva.builder().status(null).build();

        /* ========== Execucao ========== */
        reserva.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(reserva.getStatus(), is(StatusReserva.PENDENTE));
    }

    @Test
    void testReservaPrePersistDefineTimestamps() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = Reserva.builder().status(StatusReserva.CONFIRMADA).build();

        /* ========== Execucao ========== */
        reserva.prePersist();

        /* ========== Verificacoes ========== */
        assertNotNull(reserva.getCriadoEm());
        assertNotNull(reserva.getAtualizadoEm());
    }

    @Test
    void testReservaPreUpdateAtualizaAtualizadoEm() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = Reserva.builder().atualizadoEm(LocalDateTime.now().minusDays(1)).build();

        /* ========== Execucao ========== */
        reserva.preUpdate();

        /* ========== Verificacoes ========== */
        assertNotNull(reserva.getAtualizadoEm());
    }

    @Test
    void testUsuarioPrePersistDefineDefaults() {

        /* ========== Montagem do cenario ========== */
        Usuario usuario = Usuario.builder().primeiroAcesso(null).ativo(null).build();

        /* ========== Execucao ========== */
        usuario.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(usuario.getPrimeiroAcesso(), is(true));
        assertThat(usuario.getAtivo(), is(true));
    }

    @Test
    void testUsuarioPrePersistPreservaValores() {

        /* ========== Montagem do cenario ========== */
        Usuario usuario = Usuario.builder().primeiroAcesso(false).ativo(false).build();

        /* ========== Execucao ========== */
        usuario.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(usuario.getPrimeiroAcesso(), is(false));
        assertThat(usuario.getAtivo(), is(false));
    }

    @Test
    void testUsuarioPreUpdateAtualizaAtualizadoEm() {

        /* ========== Montagem do cenario ========== */
        Usuario usuario = Usuario.builder().atualizadoEm(LocalDateTime.now().minusHours(2)).build();

        /* ========== Execucao ========== */
        usuario.preUpdate();

        /* ========== Verificacoes ========== */
        assertNotNull(usuario.getAtualizadoEm());
    }

    @Test
    void testEquipamentoPrePersistDefineDefaults() {

        /* ========== Montagem do cenario ========== */
        Equipamento equipamento = Equipamento.builder().status(null).quantidadeTotal(null).ativo(null).build();

        /* ========== Execucao ========== */
        equipamento.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(equipamento.getStatus(), is(StatusEquipamento.DISPONIVEL));
        assertThat(equipamento.getQuantidadeTotal(), is(1));
        assertThat(equipamento.getAtivo(), is(true));
    }

    @Test
    void testEquipamentoPrePersistPreservaValores() {

        /* ========== Montagem do cenario ========== */
        Equipamento equipamento = Equipamento.builder().status(StatusEquipamento.EM_USO).quantidadeTotal(10).ativo(false).build();

        /* ========== Execucao ========== */
        equipamento.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(equipamento.getStatus(), is(StatusEquipamento.EM_USO));
        assertThat(equipamento.getQuantidadeTotal(), is(10));
        assertThat(equipamento.getAtivo(), is(false));
    }

    @Test
    void testEquipamentoPreUpdateAtualizaAtualizadoEm() {

        /* ========== Montagem do cenario ========== */
        Equipamento equipamento = Equipamento.builder().atualizadoEm(LocalDateTime.now().minusDays(1)).build();

        /* ========== Execucao ========== */
        equipamento.preUpdate();

        /* ========== Verificacoes ========== */
        assertNotNull(equipamento.getAtualizadoEm());
    }

    @Test
    void testInstituicaoPrePersistDefineDefaults() {

        /* ========== Montagem do cenario ========== */
        Instituicao instituicao = Instituicao.builder().vitrineHabilitada(null).ativo(null).build();

        /* ========== Execucao ========== */
        instituicao.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(instituicao.getVitrineHabilitada(), is(false));
        assertThat(instituicao.getAtivo(), is(true));
    }

    @Test
    void testInstituicaoPrePersistPreservaValores() {

        /* ========== Montagem do cenario ========== */
        Instituicao instituicao = Instituicao.builder().vitrineHabilitada(true).ativo(false).build();

        /* ========== Execucao ========== */
        instituicao.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(instituicao.getVitrineHabilitada(), is(true));
        assertThat(instituicao.getAtivo(), is(false));
    }

    @Test
    void testInstituicaoPreUpdateAtualizaAtualizadoEm() {

        /* ========== Montagem do cenario ========== */
        Instituicao instituicao = Instituicao.builder().atualizadoEm(LocalDateTime.now().minusHours(5)).build();

        /* ========== Execucao ========== */
        instituicao.preUpdate();

        /* ========== Verificacoes ========== */
        assertNotNull(instituicao.getAtualizadoEm());
    }

    @Test
    void testPlanoPrePersistDefineDefaults() {

        /* ========== Montagem do cenario ========== */
        Plano plano = Plano.builder().nome("Basico").valor(BigDecimal.ONE).vitrineIncluida(null).ativo(null).build();

        /* ========== Execucao ========== */
        plano.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(plano.getVitrineIncluida(), is(false));
        assertThat(plano.getAtivo(), is(true));
    }

    @Test
    void testPlanoPrePersistDefineCriadoEm() {

        /* ========== Montagem do cenario ========== */
        Plano plano = Plano.builder().nome("Premium").valor(BigDecimal.TEN).build();

        /* ========== Execucao ========== */
        plano.prePersist();

        /* ========== Verificacoes ========== */
        assertNotNull(plano.getCriadoEm());
    }

    @Test
    void testProjetoPrePersistDefineStatusPadrao() {

        /* ========== Montagem do cenario ========== */
        Projeto projeto = Projeto.builder().status(null).build();

        /* ========== Execucao ========== */
        projeto.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(projeto.getStatus(), is(StatusProjeto.PENDENTE));
    }

    @Test
    void testProjetoPrePersistPreservaStatus() {

        /* ========== Montagem do cenario ========== */
        Projeto projeto = Projeto.builder().status(StatusProjeto.APROVADO).build();

        /* ========== Execucao ========== */
        projeto.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(projeto.getStatus(), is(StatusProjeto.APROVADO));
    }

    @Test
    void testProjetoPreUpdateAtualizaAtualizadoEm() {

        /* ========== Montagem do cenario ========== */
        Projeto projeto = Projeto.builder().atualizadoEm(LocalDateTime.now().minusHours(2)).build();

        /* ========== Execucao ========== */
        projeto.preUpdate();

        /* ========== Verificacoes ========== */
        assertNotNull(projeto.getAtualizadoEm());
    }

    @Test
    void testReservaEquipamentoPrePersistDefineQuantidadePadrao() {

        /* ========== Montagem do cenario ========== */
        ReservaEquipamento reservaEquipamento = ReservaEquipamento.builder().quantidade(null).build();

        /* ========== Execucao ========== */
        reservaEquipamento.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(reservaEquipamento.getQuantidade(), is(1));
    }

    @Test
    void testUsuarioPermissaoPrePersistDefineConcedidaPadrao() {

        /* ========== Montagem do cenario ========== */
        UsuarioPermissao usuarioPermissao = UsuarioPermissao.builder().concedida(null).build();

        /* ========== Execucao ========== */
        usuarioPermissao.prePersist();

        /* ========== Verificacoes ========== */
        assertThat(usuarioPermissao.getConcedida(), is(true));
    }

    @Test
    void testLogAcaoPrePersistDefineCriadoEm() {

        /* ========== Montagem do cenario ========== */
        LogAcao logAcao = LogAcao.builder().build();

        /* ========== Execucao ========== */
        logAcao.prePersist();

        /* ========== Verificacoes ========== */
        assertNotNull(logAcao.getCriadoEm());
    }
}