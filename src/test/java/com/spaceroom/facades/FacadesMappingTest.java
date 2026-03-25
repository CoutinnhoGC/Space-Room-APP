package com.spaceroom.facades;

import com.spaceroom.applications.CargoPermissaoApplication;
import com.spaceroom.applications.CargoApplication;
import com.spaceroom.applications.EquipamentoApplication;
import com.spaceroom.applications.EspacoApplication;
import com.spaceroom.applications.InstituicaoApplication;
import com.spaceroom.applications.PlanoApplication;
import com.spaceroom.applications.ProjetoApplication;
import com.spaceroom.applications.ReservaEquipamentoApplication;
import com.spaceroom.applications.ReservaApplication;
import com.spaceroom.applications.UsuarioApplication;
import com.spaceroom.applications.UsuarioPermissaoApplication;
import com.spaceroom.entities.*;
import com.spaceroom.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacadesMappingTest {

    @Mock
    private ReservaApplication reservaApplication;
    @Mock
    private UsuarioApplication usuarioApplication;
    @Mock
    private PlanoApplication planoApplication;
    @Mock
    private ProjetoApplication projetoApplication;
    @Mock
    private EquipamentoApplication equipamentoApplication;
    @Mock
    private InstituicaoApplication instituicaoApplication;
    @Mock
    private ReservaEquipamentoApplication reservaEquipamentoApplication;
    @Mock
    private UsuarioPermissaoApplication usuarioPermissaoApplication;
    @Mock
    private CargoPermissaoApplication cargoPermissaoApplication;
    @Mock
    private EspacoApplication espacoApplication;
    @Mock
    private CargoApplication cargoApplication;

    private ReservaFacade reservaFacade;
    private UsuarioFacade usuarioFacade;
    private PlanoFacade planoFacade;
    private ProjetoFacade projetoFacade;
    private EquipamentoFacade equipamentoFacade;
    private InstituicaoFacade instituicaoFacade;
    private ReservaEquipamentoFacade reservaEquipamentoFacade;
    private UsuarioPermissaoFacade usuarioPermissaoFacade;
    private CargoPermissaoFacade cargoPermissaoFacade;
    private EspacoFacade espacoFacade;
    private CargoFacade cargoFacade;

    @BeforeEach
    void setUp() {
        reservaFacade = new ReservaFacade(reservaApplication);
        usuarioFacade = new UsuarioFacade(usuarioApplication);
        planoFacade = new PlanoFacade(planoApplication);
        projetoFacade = new ProjetoFacade(projetoApplication);
        equipamentoFacade = new EquipamentoFacade(equipamentoApplication);
        instituicaoFacade = new InstituicaoFacade(instituicaoApplication);
        reservaEquipamentoFacade = new ReservaEquipamentoFacade(reservaEquipamentoApplication);
        usuarioPermissaoFacade = new UsuarioPermissaoFacade(usuarioPermissaoApplication);
        cargoPermissaoFacade = new CargoPermissaoFacade(cargoPermissaoApplication);
        espacoFacade = new EspacoFacade(espacoApplication);
        cargoFacade = new CargoFacade(cargoApplication);
    }

    @Test
    void testReservaFacadeCriar_MapeiaStatusPadrao() {

        /* ========== Montagem do cenario ========== */
        ReservaModel model = new ReservaModel();
        model.setIdInstituicao(1L);
        model.setIdUsuario(2L);
        model.setIdEspaco(3L);
        model.setTitulo("Titulo");
        model.setDataInicio(LocalDateTime.of(2026, 3, 25, 10, 0));
        model.setDataFim(LocalDateTime.of(2026, 3, 25, 12, 0));

        Reserva retorno = Reserva.builder().idReserva(1L).status(StatusReserva.PENDENTE).titulo("Titulo").build();
        when(reservaApplication.criar(any(Reserva.class))).thenReturn(retorno);

        /* ========== Execucao ========== */
        ReservaModel resultado = reservaFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getStatus(), is(StatusReserva.PENDENTE));
    }

    @Test
    void testReservaFacadeListarTodas() {

        /* ========== Montagem do cenario ========== */
        when(reservaApplication.listarTodas()).thenReturn(List.of(
                Reserva.builder().idReserva(1L).titulo("A").build(),
                Reserva.builder().idReserva(2L).titulo("B").build()
        ));

        /* ========== Execucao ========== */
        List<ReservaModel> resultado = reservaFacade.listarTodas();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testReservaFacadeBuscarPorId() {

        /* ========== Montagem do cenario ========== */
        when(reservaApplication.buscarPorId(10L)).thenReturn(Reserva.builder().idReserva(10L).titulo("R").build());

        /* ========== Execucao ========== */
        ReservaModel resultado = reservaFacade.buscarPorId(10L);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdReserva(), is(10L));
    }

    @Test
    void testReservaFacadeAtualizar() {

        /* ========== Montagem do cenario ========== */
        ReservaModel model = new ReservaModel();
        model.setTitulo("Atualizada");
        when(reservaApplication.atualizar(any(Long.class), any(Reserva.class)))
                .thenReturn(Reserva.builder().idReserva(1L).titulo("Atualizada").build());

        /* ========== Execucao ========== */
        ReservaModel resultado = reservaFacade.atualizar(1L, model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getTitulo(), is("Atualizada"));
    }

    @Test
    void testReservaFacadeDeletar() {

        /* ========== Montagem do cenario ========== */

        /* ========== Execucao ========== */
        reservaFacade.deletar(1L);

        /* ========== Verificacoes ========== */
        verify(reservaApplication, times(1)).deletar(1L);
    }

    @Test
    void testUsuarioFacadeCriar_MapeiaCampos() {

        /* ========== Montagem do cenario ========== */
        UsuarioModel model = new UsuarioModel();
        model.setIdInstituicao(1L);
        model.setIdCargo(1);
        model.setNome("Ana");
        model.setEmail("ana@x.com");

        when(usuarioApplication.criar(any(Usuario.class))).thenReturn(Usuario.builder().idUsuario(1L).nome("Ana").email("ana@x.com").build());

        /* ========== Execucao ========== */
        UsuarioModel resultado = usuarioFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getEmail(), is("ana@x.com"));
    }

    @Test
    void testUsuarioFacadeListarTodos() {

        /* ========== Montagem do cenario ========== */
        when(usuarioApplication.listarTodos()).thenReturn(List.of(
                Usuario.builder().idUsuario(1L).build(),
                Usuario.builder().idUsuario(2L).build()
        ));

        /* ========== Execucao ========== */
        List<UsuarioModel> resultado = usuarioFacade.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testUsuarioFacadeBuscarPorId() {

        /* ========== Montagem do cenario ========== */
        when(usuarioApplication.buscarPorId(1L)).thenReturn(Usuario.builder().idUsuario(1L).nome("U").build());

        /* ========== Execucao ========== */
        UsuarioModel resultado = usuarioFacade.buscarPorId(1L);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdUsuario(), is(1L));
    }

    @Test
    void testUsuarioFacadeAtualizar() {

        /* ========== Montagem do cenario ========== */
        UsuarioModel model = new UsuarioModel();
        model.setNome("Novo");
        when(usuarioApplication.atualizar(any(Long.class), any(Usuario.class)))
                .thenReturn(Usuario.builder().idUsuario(1L).nome("Novo").build());

        /* ========== Execucao ========== */
        UsuarioModel resultado = usuarioFacade.atualizar(1L, model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Novo"));
    }

    @Test
    void testUsuarioFacadeDeletar() {

        /* ========== Montagem do cenario ========== */

        /* ========== Execucao ========== */
        usuarioFacade.deletar(1L);

        /* ========== Verificacoes ========== */
        verify(usuarioApplication).deletar(1L);
    }

    @Test
    void testPlanoFacadeCriar_MapeiaCampos() {

        /* ========== Montagem do cenario ========== */
        PlanoModel model = new PlanoModel();
        model.setNome("Basico");
        model.setValor(BigDecimal.TEN);
        when(planoApplication.criar(any(Plano.class))).thenReturn(Plano.builder().idPlano(1).nome("Basico").valor(BigDecimal.TEN).build());

        /* ========== Execucao ========== */
        PlanoModel resultado = planoFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdPlano(), is(1));
    }

    @Test
    void testPlanoFacadeListarTodos() {

        /* ========== Montagem do cenario ========== */
        when(planoApplication.listarTodos()).thenReturn(List.of(Plano.builder().idPlano(1).build()));

        /* ========== Execucao ========== */
        List<PlanoModel> resultado = planoFacade.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(1));
    }

    @Test
    void testProjetoFacadeCriar_MapeiaStatusPadrao() {

        /* ========== Montagem do cenario ========== */
        ProjetoModel model = new ProjetoModel();
        model.setIdInstituicao(1L);
        model.setTitulo("Projeto");
        model.setCriadoPor(2L);
        when(projetoApplication.criar(any(Projeto.class))).thenReturn(Projeto.builder().idProjeto(1L).status(StatusProjeto.PENDENTE).build());

        /* ========== Execucao ========== */
        ProjetoModel resultado = projetoFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getStatus(), is(StatusProjeto.PENDENTE));
    }

    @Test
    void testProjetoFacadeBuscarPorId() {

        /* ========== Montagem do cenario ========== */
        when(projetoApplication.buscarPorId(9L)).thenReturn(Projeto.builder().idProjeto(9L).titulo("Projeto 9").build());

        /* ========== Execucao ========== */
        ProjetoModel resultado = projetoFacade.buscarPorId(9L);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdProjeto(), is(9L));
    }

    @Test
    void testEquipamentoFacadeCriar_MapeiaStatusPadrao() {

        /* ========== Montagem do cenario ========== */
        EquipamentoModel model = new EquipamentoModel();
        model.setIdInstituicao(1L);
        model.setNome("Projetor");
        when(equipamentoApplication.criar(any(Equipamento.class))).thenReturn(Equipamento.builder().idEquipamento(1L).status(StatusEquipamento.DISPONIVEL).build());

        /* ========== Execucao ========== */
        EquipamentoModel resultado = equipamentoFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getStatus(), is(StatusEquipamento.DISPONIVEL));
    }

    @Test
    void testInstituicaoFacadeCriar_MapeiaTipo() {

        /* ========== Montagem do cenario ========== */
        InstituicaoModel model = new InstituicaoModel();
        model.setNomeFantasia("IFSP");
        model.setTipo(TipoInstituicao.ESCOLA);
        when(instituicaoApplication.criar(any(Instituicao.class))).thenReturn(Instituicao.builder().idInstituicao(1L).tipo(TipoInstituicao.ESCOLA).build());

        /* ========== Execucao ========== */
        InstituicaoModel resultado = instituicaoFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getTipo(), is(TipoInstituicao.ESCOLA));
    }

    @Test
    void testReservaEquipamentoFacadeCriar_MapeiaQuantidade() {

        /* ========== Montagem do cenario ========== */
        ReservaEquipamentoModel model = new ReservaEquipamentoModel();
        model.setIdReserva(1L);
        model.setIdEquipamento(2L);
        model.setQuantidade(3);
        when(reservaEquipamentoApplication.criar(any(ReservaEquipamento.class))).thenReturn(ReservaEquipamento.builder().idReservaEquipamento(1L).quantidade(3).build());

        /* ========== Execucao ========== */
        ReservaEquipamentoModel resultado = reservaEquipamentoFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getQuantidade(), is(3));
    }

    @Test
    void testUsuarioPermissaoFacadeCriar_MapeiaConcedida() {

        /* ========== Montagem do cenario ========== */
        UsuarioPermissaoModel model = new UsuarioPermissaoModel();
        model.setIdUsuario(1L);
        model.setIdPermissao(2);
        model.setConcedida(true);
        when(usuarioPermissaoApplication.criar(any(UsuarioPermissao.class))).thenReturn(UsuarioPermissao.builder().idUsuario(1L).idPermissao(2).concedida(true).build());

        /* ========== Execucao ========== */
        UsuarioPermissaoModel resultado = usuarioPermissaoFacade.criar(model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getConcedida(), is(true));
    }

    @Test
    void testCargoPermissaoFacadeBuscarPorId() {

        /* ========== Montagem do cenario ========== */
        when(cargoPermissaoApplication.buscarPorId(1, 2)).thenReturn(CargoPermissao.builder().idCargo(1).idPermissao(2).build());

        /* ========== Execucao ========== */
        CargoPermissaoModel resultado = cargoPermissaoFacade.buscarPorId(1, 2);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdCargo(), is(1));
        assertThat(resultado.getIdPermissao(), is(2));
    }

    @Test
    void testEspacoFacadeAtualizar_MapeiaCampos() {

        /* ========== Montagem do cenario ========== */
        EspacoModel model = new EspacoModel();
        model.setNome("Sala 101");
        model.setIdInstituicao(1L);
        model.setTipo(TipoEspaco.SALA);
        model.setCapacidade(30);

        when(espacoApplication.atualizar(any(Long.class), any(Espaco.class)))
                .thenReturn(Espaco.builder().idEspaco(1L).nome("Sala 101").capacidade(30).build());

        /* ========== Execucao ========== */
        EspacoModel resultado = espacoFacade.atualizar(1L, model);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Sala 101"));
        assertThat(resultado.getCapacidade(), is(30));
    }

}
