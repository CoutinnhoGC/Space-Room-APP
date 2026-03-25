package com.spaceroom.applications;

import com.spaceroom.entities.Plano;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.PlanoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanoApplicationTest {

    @Mock
    private PlanoRepository planoRepository;

    private PlanoApplication planoApplication;

    @BeforeEach
    void setUp() {
        planoApplication = new PlanoApplication(planoRepository);
    }

    @Test
    void testCriarPlano() {

        /* ========== Montagem do cenario ========== */
        Plano plano = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        when(planoRepository.save(any(Plano.class))).thenReturn(plano);

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.criar(plano);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Basico"));
        assertThat(resultado.getValor(), is(BigDecimal.valueOf(29.90)));
        verify(planoRepository, times(1)).save(plano);
    }

    @Test
    void testCriarPlano_RetornaMesmoObjetoDoRepositorio() {

        /* ========== Montagem do cenario ========== */
        Plano plano = novoPlano(8, "Start", BigDecimal.valueOf(19.90));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.criar(plano);

        /* ========== Verificacoes ========== */
        assertThat(resultado, is(plano));
    }

    @Test
    void testCriarPlano_MantemCamposBooleanosInformados() {

        /* ========== Montagem do cenario ========== */
        Plano plano = novoPlano(9, "Empresarial", BigDecimal.valueOf(149.90));
        plano.setVitrineIncluida(true);
        plano.setAtivo(false);
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.criar(plano);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getVitrineIncluida(), is(true));
        assertThat(resultado.getAtivo(), is(false));
    }

    @Test
    void testListarTodosPlanos() {

        /* ========== Montagem do cenario ========== */
        when(planoRepository.findAll()).thenReturn(List.of(
                novoPlano(1, "Basico", BigDecimal.valueOf(29.90)),
                novoPlano(2, "Premium", BigDecimal.valueOf(99.90))
        ));

        /* ========== Execucao ========== */
        List<Plano> resultado = planoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testListarTodosPlanos_ListaVazia() {

        /* ========== Montagem do cenario ========== */
        when(planoRepository.findAll()).thenReturn(List.of());

        /* ========== Execucao ========== */
        List<Plano> resultado = planoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.isEmpty(), is(true));
    }

    @Test
    void testListarTodosPlanos_PreservaOrdemRetornadaPeloRepositorio() {

        /* ========== Montagem do cenario ========== */
        Plano primeiro = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano segundo = novoPlano(2, "Premium", BigDecimal.valueOf(99.90));
        when(planoRepository.findAll()).thenReturn(List.of(primeiro, segundo));

        /* ========== Execucao ========== */
        List<Plano> resultado = planoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.get(0).getIdPlano(), is(1));
        assertThat(resultado.get(1).getIdPlano(), is(2));
    }

    @Test
    void testBuscarPlanoPorId() {

        /* ========== Montagem do cenario ========== */
        when(planoRepository.findById(1)).thenReturn(Optional.of(novoPlano(1, "Basico", BigDecimal.valueOf(29.90))));

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.buscarPorId(1);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdPlano(), is(1));
        assertThat(resultado.getNome(), is("Basico"));
    }

    @Test
    void testBuscarPlanoPorId_MensagemComId() {

        /* ========== Montagem do cenario ========== */
        when(planoRepository.findById(123)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> planoApplication.buscarPorId(123)
        );

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Plano nao encontrado para o id: 123"));
    }

    @Test
    void testBuscarPlanoPorId_Inexistente() {

        /* ========== Montagem do cenario ========== */
        when(planoRepository.findById(404)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> planoApplication.buscarPorId(404)
        );

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Plano nao encontrado para o id: 404"));
    }

    @Test
    void testAtualizarPlano() {

        /* ========== Montagem do cenario ========== */
        Plano existente = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano atualizado = novoPlano(1, "Premium", BigDecimal.valueOf(99.90));
        atualizado.setDescricao("Plano completo");
        atualizado.setLimiteUsuarios(100);
        atualizado.setLimiteEspacos(20);
        atualizado.setLimiteReservasMes(400);
        atualizado.setVitrineIncluida(true);
        atualizado.setAtivo(false);

        when(planoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Premium"));
        assertThat(resultado.getValor(), is(BigDecimal.valueOf(99.90)));
        assertThat(resultado.getLimiteUsuarios(), is(100));
        assertThat(resultado.getVitrineIncluida(), is(true));
        assertThat(resultado.getAtivo(), is(false));
    }

    @Test
    void testAtualizarPlano_MantemIdOriginalDaEntidadeExistente() {

        /* ========== Montagem do cenario ========== */
        Plano existente = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano atualizado = novoPlano(999, "Premium", BigDecimal.valueOf(99.90));
        when(planoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdPlano(), is(1));
    }

    @Test
    void testAtualizarPlano_AtualizaTodosOsCampos() {

        /* ========== Montagem do cenario ========== */
        Plano existente = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano atualizado = novoPlano(1, "Pro", BigDecimal.valueOf(59.90));
        atualizado.setDescricao("Descricao Pro");
        atualizado.setLimiteUsuarios(50);
        atualizado.setLimiteEspacos(10);
        atualizado.setLimiteReservasMes(200);
        atualizado.setVitrineIncluida(true);
        atualizado.setAtivo(true);

        when(planoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        planoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        ArgumentCaptor<Plano> captor = ArgumentCaptor.forClass(Plano.class);
        verify(planoRepository).save(captor.capture());
        assertThat(captor.getValue().getNome(), is("Pro"));
        assertThat(captor.getValue().getValor(), is(BigDecimal.valueOf(59.90)));
        assertThat(captor.getValue().getDescricao(), is("Descricao Pro"));
        assertThat(captor.getValue().getLimiteUsuarios(), is(50));
        assertThat(captor.getValue().getLimiteEspacos(), is(10));
        assertThat(captor.getValue().getLimiteReservasMes(), is(200));
        assertThat(captor.getValue().getVitrineIncluida(), is(true));
        assertThat(captor.getValue().getAtivo(), is(true));
    }

    @Test
    void testAtualizarPlano_BuscaAntesDeSalvar() {

        /* ========== Montagem do cenario ========== */
        Plano existente = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano atualizado = novoPlano(1, "Pro", BigDecimal.valueOf(59.90));
        when(planoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        planoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        InOrder ordem = inOrder(planoRepository);
        ordem.verify(planoRepository).findById(1);
        ordem.verify(planoRepository).save(any(Plano.class));
    }

    @Test
    void testAtualizarPlano_ChamaSaveUmaVez() {

        /* ========== Montagem do cenario ========== */
        Plano existente = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano atualizado = novoPlano(1, "Pro", BigDecimal.valueOf(59.90));
        when(planoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        planoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        verify(planoRepository, times(1)).save(any(Plano.class));
    }

    @Test
    void testAtualizarPlano_Inexistente_NaoChamaSave() {

        /* ========== Montagem do cenario ========== */
        Plano atualizado = novoPlano(1, "Pro", BigDecimal.valueOf(59.90));
        when(planoRepository.findById(1)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> planoApplication.atualizar(1, atualizado));

        /* ========== Verificacoes ========== */
        verify(planoRepository, never()).save(any());
    }

    @Test
    void testAtualizarPlano_AceitaCamposNulos() {

        /* ========== Montagem do cenario ========== */
        Plano existente = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        Plano atualizado = Plano.builder()
                .idPlano(1)
                .nome(null)
                .valor(null)
                .descricao(null)
                .limiteUsuarios(null)
                .limiteEspacos(null)
                .limiteReservasMes(null)
                .vitrineIncluida(null)
                .ativo(null)
                .build();
        when(planoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(planoRepository.save(any(Plano.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Plano resultado = planoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is((String) null));
        assertThat(resultado.getValor(), is((BigDecimal) null));
        assertThat(resultado.getLimiteUsuarios(), is((Integer) null));
        assertThat(resultado.getVitrineIncluida(), is((Boolean) null));
        assertThat(resultado.getAtivo(), is((Boolean) null));
    }

    @Test
    void testDeletarPlano() {

        /* ========== Montagem do cenario ========== */
        Plano plano = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        when(planoRepository.findById(1)).thenReturn(Optional.of(plano));

        /* ========== Execucao ========== */
        planoApplication.deletar(1);

        /* ========== Verificacoes ========== */
        verify(planoRepository, times(1)).delete(plano);
    }

    @Test
    void testDeletarPlano_BuscaAntesDeDeletar() {

        /* ========== Montagem do cenario ========== */
        Plano plano = novoPlano(1, "Basico", BigDecimal.valueOf(29.90));
        when(planoRepository.findById(1)).thenReturn(Optional.of(plano));

        /* ========== Execucao ========== */
        planoApplication.deletar(1);

        /* ========== Verificacoes ========== */
        InOrder ordem = inOrder(planoRepository);
        ordem.verify(planoRepository).findById(1);
        ordem.verify(planoRepository).delete(plano);
    }

    @Test
    void testDeletarPlano_Inexistente() {

        /* ========== Montagem do cenario ========== */
        when(planoRepository.findById(1)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> planoApplication.deletar(1));

        /* ========== Verificacoes ========== */
        verify(planoRepository, never()).delete(any());
    }

    private Plano novoPlano(Integer idPlano, String nome, BigDecimal valor) {
        return Plano.builder()
                .idPlano(idPlano)
                .nome(nome)
                .valor(valor)
                .descricao("Descricao " + nome)
                .limiteUsuarios(10)
                .limiteEspacos(5)
                .limiteReservasMes(50)
                .vitrineIncluida(false)
                .ativo(true)
                .build();
    }
}
