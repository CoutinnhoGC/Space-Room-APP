package com.spaceroom.applications;

import com.spaceroom.entities.Reserva;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaApplication {

    private final ReservaRepository reservaRepository;
    private final AutorizacaoApplication autorizacaoApplication;

    @Autowired
    public ReservaApplication(ReservaRepository reservaRepository, AutorizacaoApplication autorizacaoApplication) {
        this.reservaRepository = reservaRepository;
        this.autorizacaoApplication = autorizacaoApplication;
    }

    public ReservaApplication(ReservaRepository reservaRepository) {
        this(reservaRepository, null);
    }

    public Reserva criar(Reserva reserva) {
        validarAutorizacao(reserva);
        validarCamposObrigatorios(reserva);
        validarDatas(reserva);
        validarConflitoHorario(reserva);
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long idReserva) {
        return reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva não encontrada para o id: " + idReserva
                ));
    }

    public Reserva atualizar(Long idReserva, Reserva dadosAtualizados) {
        validarAutorizacao(dadosAtualizados);
        Reserva reservaExistente = buscarPorId(idReserva);

        validarCamposObrigatorios(dadosAtualizados);
        validarDatas(dadosAtualizados);
        validarConflitoHorarioAtualizacao(idReserva, dadosAtualizados);

        reservaExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        reservaExistente.setIdUsuario(dadosAtualizados.getIdUsuario());
        reservaExistente.setIdEspaco(dadosAtualizados.getIdEspaco());
        reservaExistente.setTitulo(dadosAtualizados.getTitulo());
        reservaExistente.setFinalidade(dadosAtualizados.getFinalidade());
        reservaExistente.setDataInicio(dadosAtualizados.getDataInicio());
        reservaExistente.setDataFim(dadosAtualizados.getDataFim());
        reservaExistente.setStatus(dadosAtualizados.getStatus());
        reservaExistente.setObservacao(dadosAtualizados.getObservacao());

        return reservaRepository.save(reservaExistente);
    }

    public void deletar(Long idReserva) {
        Reserva reserva = buscarPorId(idReserva);
        reservaRepository.delete(reserva);
    }

    private void validarCamposObrigatorios(Reserva reserva) {
        if (reserva.getTitulo() == null || reserva.getTitulo().isBlank()) {
            throw new BusinessException("O título é obrigatório.");
        }

        if (reserva.getFinalidade() == null || reserva.getFinalidade().isBlank()) {
            throw new BusinessException("A finalidade é obrigatória.");
        }
    }

    private void validarDatas(Reserva reserva) {
        if (reserva.getDataInicio() == null || reserva.getDataFim() == null) {
            throw new BusinessException("Data de início e data de fim são obrigatórias.");
        }

        if (!reserva.getDataFim().isAfter(reserva.getDataInicio())) {
            throw new BusinessException("A data fim deve ser maior que a data início.");
        }
    }

    private void validarConflitoHorario(Reserva reserva) {
        boolean existeConflito = reservaRepository
                .existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(
                        reserva.getIdEspaco(),
                        reserva.getDataFim(),
                        reserva.getDataInicio()
                );

        if (existeConflito) {
            throw new BusinessException("Já existe uma reserva para este espaço nesse intervalo.");
        }
    }

    private void validarConflitoHorarioAtualizacao(Long idReserva, Reserva reserva) {
        List<Reserva> reservasMesmoEspaco = reservaRepository.findByIdEspaco(reserva.getIdEspaco());

        boolean existeConflito = reservasMesmoEspaco.stream()
                .filter(item -> !item.getIdReserva().equals(idReserva))
                .anyMatch(item -> reserva.getDataInicio().isBefore(item.getDataFim()) && reserva.getDataFim().isAfter(item.getDataInicio()));

        if (existeConflito) {
            throw new BusinessException("Já existe uma reserva para este espaço nesse intervalo.");
        }
    }

    private void validarAutorizacao(Reserva reserva) {
        if (autorizacaoApplication == null) {
            return;
        }

        autorizacaoApplication.validarCriacaoOuEdicaoReserva(reserva);
    }
}
