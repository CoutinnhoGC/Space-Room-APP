package com.spaceroom.applications;

import com.spaceroom.entities.Espaco;
import com.spaceroom.entities.Reserva;
import com.spaceroom.entities.StatusReserva;
import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.EspacoRepository;
import com.spaceroom.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaApplication {

    private final ReservaRepository reservaRepository;
    private final EspacoRepository espacoRepository;
    private final AutorizacaoApplication autorizacaoApplication;

    public ReservaApplication(ReservaRepository reservaRepository) {
        this(reservaRepository, null, null);
    }

    public Reserva criar(Reserva reserva) {
        validarAutorizacao(reserva);
        validarCamposObrigatorios(reserva);
        validarDatas(reserva);
        validarSubespaco(reserva);
        validarConflitoHorario(reserva);
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarTodas() {
        if (autorizacaoApplication == null) {
            return reservaRepository.findAll();
        }
        Usuario usuarioAtual = autorizacaoApplication.obterUsuarioAtualObrigatorio();
        if (autorizacaoApplication.isAdminPlataforma(usuarioAtual)) {
            return reservaRepository.findAll();
        }
        return reservaRepository.findByIdInstituicao(usuarioAtual.getIdInstituicao());
    }

    public Reserva buscarPorId(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada para o id: " + idReserva));
        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarAcessoReserva(reserva);
        }
        return reserva;
    }

    public Reserva atualizar(Long idReserva, Reserva dadosAtualizados) {
        validarAutorizacao(dadosAtualizados);
        Reserva reservaExistente = buscarPorId(idReserva);

        validarCamposObrigatorios(dadosAtualizados);
        validarDatas(dadosAtualizados);
        validarSubespaco(dadosAtualizados);
        validarConflitoHorarioAtualizacao(idReserva, dadosAtualizados);

        reservaExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        reservaExistente.setIdUsuario(dadosAtualizados.getIdUsuario());
        reservaExistente.setIdEspaco(dadosAtualizados.getIdEspaco());
        reservaExistente.setIdSubespaco(dadosAtualizados.getIdSubespaco());
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

    private void validarSubespaco(Reserva reserva) {
        if (espacoRepository == null) {
            return;
        }

        Espaco espacoPrincipal = espacoRepository.findById(reserva.getIdEspaco())
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado para o id: " + reserva.getIdEspaco()));

        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarAcessoEspaco(espacoPrincipal);
        }

        if (espacoPrincipal.getIdEspacoPai() != null) {
            throw new BusinessException("Selecione o espaço principal e, se necessário, informe o subespaço separadamente.");
        }

        if (reserva.getIdSubespaco() == null) {
            return;
        }

        Espaco subespaco = espacoRepository.findById(reserva.getIdSubespaco())
                .orElseThrow(() -> new ResourceNotFoundException("Subespaço não encontrado para o id: " + reserva.getIdSubespaco()));

        if (!reserva.getIdEspaco().equals(subespaco.getIdEspacoPai())) {
            throw new BusinessException("O subespaço selecionado não pertence ao espaço principal informado.");
        }
    }

    private void validarConflitoHorario(Reserva reserva) {
        if (espacoRepository == null) {
            boolean existeConflito = reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(
                    reserva.getIdEspaco(),
                    reserva.getDataFim(),
                    reserva.getDataInicio()
            );

            if (existeConflito) {
                throw new BusinessException("Já existe uma reserva para este espaço nesse intervalo.");
            }
            return;
        }

        boolean existeConflito = reservaRepository.findByIdEspaco(reserva.getIdEspaco())
                .stream()
                .anyMatch(item -> possuiConflito(reserva, item));

        if (existeConflito) {
            throw new BusinessException("Já existe uma reserva para este espaço ou subespaço nesse intervalo.");
        }
    }

    private void validarConflitoHorarioAtualizacao(Long idReserva, Reserva reserva) {
        if (espacoRepository == null) {
            boolean existeConflito = reservaRepository.findByIdEspaco(reserva.getIdEspaco())
                    .stream()
                    .filter(item -> !item.getIdReserva().equals(idReserva))
                    .anyMatch(item -> possuiConflito(reserva, item));

            if (existeConflito) {
                throw new BusinessException("Já existe uma reserva para este espaço nesse intervalo.");
            }
            return;
        }

        boolean existeConflito = reservaRepository.findByIdEspaco(reserva.getIdEspaco())
                .stream()
                .filter(item -> !item.getIdReserva().equals(idReserva))
                .anyMatch(item -> possuiConflito(reserva, item));

        if (existeConflito) {
            throw new BusinessException("Já existe uma reserva para este espaço ou subespaço nesse intervalo.");
        }
    }

    private boolean possuiConflito(Reserva referencia, Reserva existente) {
        if (existente.getStatus() == StatusReserva.CANCELADA) {
            return false;
        }

        boolean intervaloSobreposto = referencia.getDataInicio().isBefore(existente.getDataFim())
                && referencia.getDataFim().isAfter(existente.getDataInicio());

        if (!intervaloSobreposto) {
            return false;
        }

        if (referencia.getIdSubespaco() == null || existente.getIdSubespaco() == null) {
            return true;
        }

        return referencia.getIdSubespaco().equals(existente.getIdSubespaco());
    }

    private void validarAutorizacao(Reserva reserva) {
        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarCriacaoOuEdicaoReserva(reserva);
        }
    }
}
