package com.spaceroom.applications;

import com.spaceroom.entities.Cargo;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.CargoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class CargoApplicationTest {

    @Mock
    private CargoRepository cargoRepository;

    private CargoApplication cargoApplication;

    @BeforeEach
    void setUp() {
        cargoApplication = new CargoApplication(cargoRepository);
    }

    @Test
    void testCriarCargo() {

        /* ========== Montagem do cenario ========== */
        Cargo cargo = novoCargo(1, "Gestor");
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.criar(cargo);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Gestor"));
        verify(cargoRepository, times(1)).save(cargo);
    }

    @Test
    void testCriarCargo_RetornaMesmoObjetoDoRepositorio() {

        /* ========== Montagem do cenario ========== */
        Cargo cargo = novoCargo(7, "Analista");
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.criar(cargo);

        /* ========== Verificacoes ========== */
        assertThat(resultado, is(cargo));
    }

    @Test
    void testCriarCargo_MantemAtivoFalse() {

        /* ========== Montagem do cenario ========== */
        Cargo cargo = novoCargo(8, "Visitante");
        cargo.setAtivo(false);
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.criar(cargo);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getAtivo(), is(false));
    }

    @Test
    void testListarTodosCargos() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findAll()).thenReturn(List.of(
                novoCargo(1, "Gestor"),
                novoCargo(2, "Coordenador")
        ));

        /* ========== Execucao ========== */
        List<Cargo> resultado = cargoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testListarTodosCargos_ListaVazia() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findAll()).thenReturn(List.of());

        /* ========== Execucao ========== */
        List<Cargo> resultado = cargoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.isEmpty(), is(true));
    }

    @Test
    void testListarTodosCargos_PreservaOrdemRetornadaPeloRepositorio() {

        /* ========== Montagem do cenario ========== */
        Cargo primeiro = novoCargo(1, "Primeiro");
        Cargo segundo = novoCargo(2, "Segundo");
        when(cargoRepository.findAll()).thenReturn(List.of(primeiro, segundo));

        /* ========== Execucao ========== */
        List<Cargo> resultado = cargoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.get(0).getIdCargo(), is(1));
        assertThat(resultado.get(1).getIdCargo(), is(2));
    }

    @Test
    void testListarTodosCargos_ComUmRegistro() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findAll()).thenReturn(List.of(novoCargo(10, "Unico")));

        /* ========== Execucao ========== */
        List<Cargo> resultado = cargoApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(1));
        assertThat(resultado.get(0).getNome(), is("Unico"));
    }

    @Test
    void testBuscarCargoPorId() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findById(1)).thenReturn(Optional.of(novoCargo(1, "Gestor")));

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.buscarPorId(1);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdCargo(), is(1));
        assertThat(resultado.getNome(), is("Gestor"));
    }

    @Test
    void testBuscarCargoPorId_MensagemComId() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findById(123)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cargoApplication.buscarPorId(123)
        );

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Cargo não encontrado para o id: 123"));
    }

    @Test
    void testBuscarCargoPorId_Inexistente() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findById(99)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cargoApplication.buscarPorId(99)
        );

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Cargo não encontrado para o id: 99"));
    }

    @Test
    void testAtualizarCargo() {

        /* ========== Montagem do cenario ========== */
        Cargo existente = novoCargo(1, "Gestor");
        Cargo atualizado = novoCargo(1, "Administrador");
        atualizado.setDescricao("Acesso completo");
        atualizado.setAtivo(false);

        when(cargoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Administrador"));
        assertThat(resultado.getDescricao(), is("Acesso completo"));
        assertThat(resultado.getAtivo(), is(false));
    }

    @Test
    void testAtualizarCargo_MantemIdOriginalDaEntidadeExistente() {

        /* ========== Montagem do cenario ========== */
        Cargo existente = novoCargo(1, "Gestor");
        Cargo atualizado = novoCargo(999, "Administrador");
        when(cargoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdCargo(), is(1));
    }

    @Test
    void testAtualizarCargo_AtualizaCamposEsperados() {

        /* ========== Montagem do cenario ========== */
        Cargo existente = novoCargo(1, "Gestor");
        Cargo atualizado = novoCargo(1, "Coordenador");
        atualizado.setDescricao("Atualizado");
        atualizado.setAtivo(false);

        when(cargoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        cargoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        ArgumentCaptor<Cargo> captor = ArgumentCaptor.forClass(Cargo.class);
        verify(cargoRepository).save(captor.capture());
        assertThat(captor.getValue().getNome(), is("Coordenador"));
        assertThat(captor.getValue().getDescricao(), is("Atualizado"));
        assertThat(captor.getValue().getAtivo(), is(false));
    }

    @Test
    void testAtualizarCargo_BuscaAntesDeSalvar() {

        /* ========== Montagem do cenario ========== */
        Cargo existente = novoCargo(1, "Gestor");
        Cargo atualizado = novoCargo(1, "Coordenador");
        when(cargoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        cargoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        InOrder ordem = inOrder(cargoRepository);
        ordem.verify(cargoRepository).findById(1);
        ordem.verify(cargoRepository).save(any(Cargo.class));
    }

    @Test
    void testAtualizarCargo_ChamaSaveUmaVez() {

        /* ========== Montagem do cenario ========== */
        Cargo existente = novoCargo(1, "Gestor");
        Cargo atualizado = novoCargo(1, "Coordenador");
        when(cargoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        cargoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void testAtualizarCargo_Inexistente_NaoChamaSave() {

        /* ========== Montagem do cenario ========== */
        Cargo atualizado = novoCargo(1, "Coordenador");
        when(cargoRepository.findById(1)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> cargoApplication.atualizar(1, atualizado));

        /* ========== Verificacoes ========== */
        verify(cargoRepository, never()).save(any());
    }

    @Test
    void testAtualizarCargo_AceitaCamposNulos() {

        /* ========== Montagem do cenario ========== */
        Cargo existente = novoCargo(1, "Gestor");
        Cargo atualizado = Cargo.builder().idCargo(1).nome(null).descricao(null).ativo(null).build();
        when(cargoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Cargo resultado = cargoApplication.atualizar(1, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is((String) null));
        assertThat(resultado.getDescricao(), is((String) null));
        assertThat(resultado.getAtivo(), is((Boolean) null));
    }

    @Test
    void testDeletarCargo() {

        /* ========== Montagem do cenario ========== */
        Cargo cargo = novoCargo(1, "Gestor");
        when(cargoRepository.findById(1)).thenReturn(Optional.of(cargo));

        /* ========== Execucao ========== */
        cargoApplication.deletar(1);

        /* ========== Verificacoes ========== */
        verify(cargoRepository, times(1)).delete(cargo);
    }

    @Test
    void testDeletarCargo_BuscaAntesDeDeletar() {

        /* ========== Montagem do cenario ========== */
        Cargo cargo = novoCargo(1, "Gestor");
        when(cargoRepository.findById(1)).thenReturn(Optional.of(cargo));

        /* ========== Execucao ========== */
        cargoApplication.deletar(1);

        /* ========== Verificacoes ========== */
        InOrder ordem = inOrder(cargoRepository);
        ordem.verify(cargoRepository).findById(1);
        ordem.verify(cargoRepository).delete(cargo);
    }

    @Test
    void testDeletarCargo_Inexistente() {

        /* ========== Montagem do cenario ========== */
        when(cargoRepository.findById(1)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> cargoApplication.deletar(1));

        /* ========== Verificacoes ========== */
        verify(cargoRepository, never()).delete(any());
    }

    private Cargo novoCargo(Integer idCargo, String nome) {
        return Cargo.builder()
                .idCargo(idCargo)
                .nome(nome)
                .descricao("Descricao " + nome)
                .ativo(true)
                .build();
    }
}
