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
import java.time.LocalDate;
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

        // Testa o fluxo normal de criação de uma reserva válida,
        // garantindo que não há conflito de horário e que o save é chamado corretamente

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(any(), any(), any())).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Reserva resultado = reservaApplication.criar(reserva);

        /* ========== Verificacoes ========== */
        // Verifica se os dados retornados estão corretos e se salvou no banco
        assertThat(resultado.getTitulo(), is("Reserva Teste"));
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    void testCriarReserva_DataInicioNula() {

        // Testa a validação quando a data de início não é informada.
        // A aplicação deve impedir a criação e lançar erro de negócio.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataInicio(null);

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        // Verifica se a mensagem de erro está correta e se não tentou salvar
        assertThat(exception.getMessage(), is("Data de inicio e data de fim sao obrigatorias."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataFimNula() {

        // Testa a validação quando a data de fim não é informada.
        // Também deve impedir a criação da reserva.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(null);

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Data de inicio e data de fim sao obrigatorias."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataFimIgualDataInicio() {

        // Testa a regra de negócio onde a data fim não pode ser igual à data início,
        // pois a reserva precisa ter um intervalo de tempo válido.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(reserva.getDataInicio());

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("A data fim deve ser maior que a data inicio."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataFimAntesDataInicio() {

        // Testa quando a data fim é anterior à data início.
        // Esse cenário é inválido e deve gerar erro.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(reserva.getDataInicio().minusHours(1));

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("A data fim deve ser maior que a data inicio."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_ConflitoHorario() {

        // Testa a regra de conflito de horário.
        // Simula que já existe uma reserva no mesmo espaço e no mesmo intervalo.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(any(), any(), any())).thenReturn(true);

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        /* ========== Verificacoes ========== */
        // Verifica se bloqueou a criação e retornou a mensagem correta
        assertThat(exception.getMessage(), is("Já existe uma reserva para este espaço nesse intervalo."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DataPassada() {
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataInicio(LocalDate.now().minusDays(1).atTime(10, 0));
        reserva.setDataFim(LocalDate.now().minusDays(1).atTime(11, 0));

        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        assertThat(exception.getMessage(), is("Não é permitido realizar reservas em datas passadas."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCriarReserva_DuracaoMenorQueTrintaMinutos() {
        Reserva reserva = novaReserva(1L, 10, 12);
        reserva.setDataFim(reserva.getDataInicio().plusMinutes(20));

        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.criar(reserva));

        assertThat(exception.getMessage(), is("A reserva deve ter duracao minima de 30 minutos."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testListarTodasReservas() {

        // Testa a listagem de todas as reservas cadastradas,
        // simulando o retorno de duas reservas no repositório.

        /* ========== Montagem do cenario ========== */
        when(reservaRepository.findAll()).thenReturn(List.of(novaReserva(1L, 10, 12), novaReserva(2L, 14, 16)));

        /* ========== Execucao ========== */
        List<Reserva> resultado = reservaApplication.listarTodas();

        /* ========== Verificacoes ========== */
        // Verifica se a quantidade retornada está correta
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testBuscarReservaPorId() {

        // Testa a busca de uma reserva existente pelo ID,
        // garantindo que o objeto correto seja retornado.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        /* ========== Execucao ========== */
        Reserva resultado = reservaApplication.buscarPorId(1L);

        /* ========== Verificacoes ========== */
        // Verifica se retornou a reserva correta
        assertThat(resultado.getIdReserva(), is(1L));
    }

    @Test
    void testBuscarReservaPorId_Inexistente() {

        // Testa o comportamento quando a reserva procurada não existe no banco.
        // Nesse caso, o sistema deve lançar uma exceção informando isso.

        /* ========== Montagem do cenario ========== */
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reservaApplication.buscarPorId(99L));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Reserva não encontrada para o id: 99"));
    }

    @Test
    void testAtualizarReserva() {

        // Testa a atualização de uma reserva existente com novos dados,
        // verificando se os valores alterados são mantidos no resultado final.

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
        // Verifica se os dados foram realmente atualizados
        assertThat(resultado.getTitulo(), is("Nova Reserva"));
    }

    @Test
    void testAtualizarReserva_CancelamentoNaoRevalidaDataPassada() {
        Reserva existente = novaReserva(1L, 8, 9);
        existente.setDataInicio(LocalDate.now().minusDays(2).atTime(8, 0));
        existente.setDataFim(LocalDate.now().minusDays(2).atTime(9, 0));
        Reserva cancelamento = novaReserva(1L, 8, 9);
        cancelamento.setStatus(StatusReserva.CANCELADA);
        cancelamento.setObservacao("Cancelada pelo responsavel");

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Reserva resultado = reservaApplication.atualizar(1L, cancelamento);

        assertThat(resultado.getStatus(), is(StatusReserva.CANCELADA));
        assertThat(resultado.getObservacao(), is("Cancelada pelo responsavel"));
    }

    @Test
    void testAtualizarReserva_CancelamentoDuplicado() {
        Reserva existente = novaReserva(1L, 8, 9);
        existente.setStatus(StatusReserva.CANCELADA);
        Reserva cancelamento = novaReserva(1L, 8, 9);
        cancelamento.setStatus(StatusReserva.CANCELADA);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));

        BusinessException exception = assertThrows(BusinessException.class, () -> reservaApplication.atualizar(1L, cancelamento));

        assertThat(exception.getMessage(), is("Esta reserva já está cancelada."));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testAtualizarReserva_Inexistente() {

        // Testa tentativa de atualizar uma reserva que não existe.
        // O sistema deve impedir a operação e informar que não encontrou o registro.

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

        // Testa a validação ao atualizar uma reserva com data de início nula.
        // Deve lançar erro e impedir o salvamento.

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

        // Testa a validação ao atualizar uma reserva com data de fim nula.
        // Também deve impedir a atualização no banco.

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

        // Testa a regra onde a data fim precisa ser maior que a data início também na atualização.
        // Se isso não acontecer, a alteração deve ser bloqueada.

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

        // Testa atualização que gera conflito com outra reserva já existente no mesmo espaço.
        // O sistema deve identificar o choque de horários e impedir a alteração.

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

        // Testa atualização sem conflito, considerando apenas a própria reserva.
        // Isso garante que o sistema não entenda a própria reserva como conflito indevido.

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

        // Testa a remoção de uma reserva existente.
        // Se ela for encontrada, o delete deve ser chamado normalmente.

        /* ========== Montagem do cenario ========== */
        Reserva reserva = novaReserva(1L, 10, 12);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        /* ========== Execucao ========== */
        reservaApplication.deletar(1L);

        /* ========== Verificacoes ========== */
        // Verifica se realmente chamou o delete
        verify(reservaRepository, times(1)).delete(reserva);
    }

    @Test
    void testDeletarReserva_Inexistente() {

        // Testa tentativa de deletar uma reserva que não existe.
        // Nesse caso, deve lançar erro e não chamar delete.

        /* ========== Montagem do cenario ========== */
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> reservaApplication.deletar(1L));

        /* ========== Verificacoes ========== */
        verify(reservaRepository, never()).delete(any());
    }

    @Test
    void testCriarReserva_ValidaParametrosConsultaConflito() {

        // Testa se os parâmetros enviados para a consulta de conflito estão corretos.
        // Esse teste garante que a regra de verificação usa os valores esperados.

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

        // Testa se os dados atualizados realmente chegam corretos no momento do save.
        // Aqui é usado ArgumentCaptor para capturar exatamente o objeto salvo.

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

        // Verifica se os dados foram realmente modificados antes de salvar
        assertThat(captor.getValue().getObservacao(), is("Atualizada"));
        assertNotNull(captor.getValue().getDataFim());
    }

    private Reserva novaReserva(Long id, int horaInicio, int horaFim) {
        LocalDateTime base = LocalDate.now().plusDays(1).atStartOfDay();
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
