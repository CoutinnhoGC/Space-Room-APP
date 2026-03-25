package com.spaceroom.applications;

import com.spaceroom.entities.Reserva;
import com.spaceroom.entities.StatusReserva;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaApplicationTest {

    @Mock
    private ReservaRepository reservaRepository;

    private ReservaApplication reservaApplication;

    @BeforeEach
    void setUp() {
        reservaApplication = new ReservaApplication(reservaRepository);
    }

    @Test
    void testCriarReserva() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(any(), any(), any())).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Reserva resultado = reservaApplication.criar(reserva);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getTitulo(), is("Reserva Teste"));
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    void testCriarReserva_DataInicioNula() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataInicio(null);

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Data de início e data de fim são obrigatórias."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataFimNula() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(null);

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Data de início e data de fim são obrigatórias."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataFimIgualDataInicio() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(reserva.getDataInicio());

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("A data fim deve ser maior que a data início."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataFimAntesDataInicio() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(reserva.getDataInicio().minusHours(1));

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("A data fim deve ser maior que a data início."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_ConflitoHorario() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(any(), any(), any())).thenReturn(true);

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Já existe uma reserva para este espaço nesse intervalo."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testListarTodasReservas() {

        /* ========== Montagem do cenario ========== */
        when(reservaRepository.findAll()).thenReturn(List.of(novaReserva(1L, 10, 12), novaReserva(2L, 14, 16)));

        /* ========== Execucao ========== */
        List<Reserva> resultado = reservaApplication.listarTodas();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testBuscarReservaPorId() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        /* ========== Execucao ========== */
        Reserva resultado = reservaApplication.buscarPorId(1L);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdReserva(), is(1L));
    }

    @Test
    void testBuscarReservaPorId_Inexistente() {

        /* ========== Montagem do cenario ========== */
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reservaApplication.buscarPorId(99L));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Reserva não encontrada para o id: 99"));
    }

    @Test
    void testAtualizarReserva() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);
        atualizada.setTitulo("Nova Reserva");

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reservaRepository.findByIdEspaco(3L)).thenReturn(List.of(existente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Reserva resultado = reservaApplication.atualizar(1L, atualizada);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getTitulo(), is("Nova Reserva"));
    }

    @Test
    void testAtualizarReserva_Inexistente() {

        /* ========== Montagem do cenario ========== */
        Reserva atualizada = novaReserva(1L, 10, 12);
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reservaApplication.atualizar(1L, atualizada));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Reserva não encontrada para o id: 1"));
    }

    @Test
    void testAtualizarReserva_DataInicioNula() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);
        atualizada.setDataInicio(null);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));

        /* ========== Execucao ========== */
        assertThrows(BusinessException.class, () -> reservaApplication.atualizar(1L, atualizada));

        /* ========== Verificacoes ========== */
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testAtualizarReserva_DataFimNula() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);
        atualizada.setDataFim(null);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));

        /* ========== Execucao ========== */
        assertThrows(BusinessException.class, () -> reservaApplication.atualizar(1L, atualizada));

        /* ========== Verificacoes ========== */
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testAtualizarReserva_DataFimNaoPosterior() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);
        atualizada.setDataFim(atualizada.getDataInicio());
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));

        /* ========== Execucao ========== */
        assertThrows(BusinessException.class, () -> reservaApplication.atualizar(1L, atualizada));

        /* ========== Verificacoes ========== */
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testAtualizarReserva_ComConflitoComOutraReserva() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);
        Reserva outraReserva = novaReserva(2L, 11, 13);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reservaRepository.findByIdEspaco(3L)).thenReturn(List.of(existente, outraReserva));

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.atualizar(1L, atualizada));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Já existe uma reserva para este espaço nesse intervalo."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testAtualizarReserva_SemConflitoMesmaReserva() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reservaRepository.findByIdEspaco(3L)).thenReturn(List.of(existente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Reserva resultado = reservaApplication.atualizar(1L, atualizada);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getDataInicio(), is(atualizada.getDataInicio()));
    }

    @Test
    void testDeletarReserva() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        /* ========== Execucao ========== */
        reservaApplication.deletar(1L);

        /* ========== Verificacoes ========== */
        verify(reservaRepository, times(1)).delete(reserva);
    }

    @Test
    void testDeletarReserva_Inexistente() {

        /* ========== Montagem do cenario ========== */
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> reservaApplication.deletar(1L));

        /* ========== Verificacoes ========== */
        verify(reservaRepository, never()).delete(any());
    }

    @Test
    void testCriarReserva_ValidaParametrosConsultaConflito() {

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(7L, 18, 20);
        when(reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(any(), any(), any())).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        /* ========== Execucao ========== */
        reservaApplication.criar(reserva);

        /* ========== Verificacoes ========== */
        verify(reservaRepository).existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(
                eq(3L), eq(reserva.getDataFim()), eq(reserva.getDataInicio())
        );
    }

    @Test
    void testAtualizarReserva_CamposAtualizadosNoSave() {

        /* ========== Montagem do cenario ========== */
        Reserva existente = novaReserva(1L, 8, 9);
        Reserva atualizada = novaReserva(1L, 10, 12);
        atualizada.setObservacao("Atualizada");
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reservaRepository.findByIdEspaco(3L)).thenReturn(List.of(existente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        reservaApplication.atualizar(1L, atualizada);

        /* ========== Verificacoes ========== */
        ArgumentCaptor<Reserva> captor = ArgumentCaptor.forClass(Reserva.class);
        verify(reservaRepository).save(captor.capture());
        assertThat(captor.getValue().getObservacao(), is("Atualizada"));
        assertNotNull(captor.getValue().getDataFim());
    }

    private Reserva novaReserva(Long id, int horaInicio, int horaFim) {
        LocalDateTime base = LocalDateTime.of(2026, 3, 25, 0, 0);
        return Reserva.builder()
                .idReserva(id)
                .idInstituicao(1L)
                .idUsuario(2L)
                .idEspaco(3L)
                .titulo("Reserva Teste")
                .finalidade("Teste")
                .dataInicio(base.plusHours(horaInicio))
                .dataFim(base.plusHours(horaFim))
                .status(StatusReserva.PENDENTE)
                .observacao("Obs")
                .build();
    }
}