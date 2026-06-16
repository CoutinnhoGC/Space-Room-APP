package com.spaceroom.applications;

import com.spaceroom.entities.Espaco;
import com.spaceroom.entities.Reserva;
import com.spaceroom.entities.StatusReserva;
import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.EspacoRepository;
import com.spaceroom.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaApplication {

    private final ReservaRepository reservaRepository;
    private final EspacoRepository espacoRepository;
    private final AutorizacaoApplication autorizacaoApplication;

    @Autowired
    public ReservaApplication(ReservaRepository reservaRepository,
                              EspacoRepository espacoRepository,
                              AutorizacaoApplication autorizacaoApplication) {
        this.reservaRepository = reservaRepository;
        this.espacoRepository = espacoRepository;
        this.autorizacaoApplication = autorizacaoApplication;
    }

    public ReservaApplication(ReservaRepository reservaRepository) {
        this(reservaRepository, null, null);
    }

    public Reserva criar(Reserva reserva) {
        validarAutorizacao(reserva);
        validarCamposObrigatorios(reserva);
        validarDatas(reserva);
        Espaco espacoPrincipal = validarSubespaco(reserva);
        aplicarPoliticaAprovacaoCriacao(reserva, espacoPrincipal);
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
                .orElseThrow(() -> new ResourceNotFoundException("Reserva nao encontrada para o id: " + idReserva));
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
        Espaco espacoPrincipal = validarSubespaco(dadosAtualizados);
        validarConflitoHorarioAtualizacao(idReserva, dadosAtualizados);

        reservaExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        reservaExistente.setIdUsuario(dadosAtualizados.getIdUsuario());
        reservaExistente.setIdEspaco(dadosAtualizados.getIdEspaco());
        reservaExistente.setIdSubespaco(dadosAtualizados.getIdSubespaco());
        reservaExistente.setTitulo(dadosAtualizados.getTitulo());
        reservaExistente.setFinalidade(dadosAtualizados.getFinalidade());
        reservaExistente.setDataInicio(dadosAtualizados.getDataInicio());
        reservaExistente.setDataFim(dadosAtualizados.getDataFim());
        reservaExistente.setObservacao(dadosAtualizados.getObservacao());

        aplicarPoliticaAprovacaoAtualizacao(reservaExistente, dadosAtualizados, espacoPrincipal);

        return reservaRepository.save(reservaExistente);
    }

    public void deletar(Long idReserva) {
        Reserva reserva = buscarPorId(idReserva);
        reservaRepository.delete(reserva);
    }

    private void validarCamposObrigatorios(Reserva reserva) {
        if (reserva.getTitulo() == null || reserva.getTitulo().isBlank()) {
            throw new BusinessException("O titulo e obrigatorio.");
        }

        if (reserva.getFinalidade() == null || reserva.getFinalidade().isBlank()) {
            throw new BusinessException("A finalidade e obrigatoria.");
        }
    }

    private void validarDatas(Reserva reserva) {
        if (reserva.getDataInicio() == null || reserva.getDataFim() == null) {
            throw new BusinessException("Data de inicio e data de fim sao obrigatorias.");
        }

        if (!reserva.getDataFim().isAfter(reserva.getDataInicio())) {
            throw new BusinessException("A data fim deve ser maior que a data inicio.");
        }
    }

    private Espaco validarSubespaco(Reserva reserva) {
        if (espacoRepository == null) {
            return null;
        }

        Espaco espacoPrincipal = espacoRepository.findById(reserva.getIdEspaco())
                .orElseThrow(() -> new ResourceNotFoundException("Espaco nao encontrado para o id: " + reserva.getIdEspaco()));

        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarAcessoEspaco(espacoPrincipal);
        }

        if (espacoPrincipal.getIdEspacoPai() != null) {
            throw new BusinessException("Selecione o espaco principal e, se necessario, informe o subespaco separadamente.");
        }

        if (reserva.getIdSubespaco() == null) {
            return espacoPrincipal;
        }

        Espaco subespaco = espacoRepository.findById(reserva.getIdSubespaco())
                .orElseThrow(() -> new ResourceNotFoundException("Subespaco nao encontrado para o id: " + reserva.getIdSubespaco()));

        if (!reserva.getIdEspaco().equals(subespaco.getIdEspacoPai())) {
            throw new BusinessException("O subespaco selecionado nao pertence ao espaco principal informado.");
        }

        return espacoPrincipal;
    }

    private void validarConflitoHorario(Reserva reserva) {
        if (espacoRepository == null) {
            boolean existeConflito = reservaRepository.existsByIdEspacoAndDataInicioLessThanAndDataFimGreaterThan(
                    reserva.getIdEspaco(),
                    reserva.getDataFim(),
                    reserva.getDataInicio()
            );

            if (existeConflito) {
                throw new BusinessException("Ja existe uma reserva para este espaco nesse intervalo.");
            }
            return;
        }

        boolean existeConflito = reservaRepository.findByIdEspaco(reserva.getIdEspaco())
                .stream()
                .anyMatch(item -> possuiConflito(reserva, item));

        if (existeConflito) {
            throw new BusinessException("Ja existe uma reserva para este espaco ou subespaco nesse intervalo.");
        }
    }

    private void validarConflitoHorarioAtualizacao(Long idReserva, Reserva reserva) {
        boolean existeConflito = reservaRepository.findByIdEspaco(reserva.getIdEspaco())
                .stream()
                .filter(item -> !item.getIdReserva().equals(idReserva))
                .anyMatch(item -> possuiConflito(reserva, item));

        if (existeConflito) {
            throw new BusinessException("Ja existe uma reserva para este espaco ou subespaco nesse intervalo.");
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

    private void aplicarPoliticaAprovacaoCriacao(Reserva reserva, Espaco espacoPrincipal) {
        if (autorizacaoApplication == null || espacoPrincipal == null) {
            return;
        }

        Usuario usuarioAtual = autorizacaoApplication.obterUsuarioAtualObrigatorio();
        boolean exigeAprovacao = Boolean.TRUE.equals(espacoPrincipal.getExigeAprovacao());
        boolean podeAprovar = autorizacaoApplication.podeAprovarReservaNoEspaco(usuarioAtual, espacoPrincipal);

        if (exigeAprovacao && !podeAprovar) {
            reserva.setStatus(StatusReserva.PENDENTE);
            reserva.setAprovadaPorUsuarioId(null);
            reserva.setAprovadaEm(null);
            return;
        }

        if (reserva.getStatus() == null) {
            reserva.setStatus(exigeAprovacao ? StatusReserva.PENDENTE : StatusReserva.CONFIRMADA);
        }

        if (reserva.getStatus() == StatusReserva.CONFIRMADA && podeAprovar) {
            registrarDecisaoAprovacao(reserva, usuarioAtual);
        }
    }

    private void aplicarPoliticaAprovacaoAtualizacao(Reserva reservaExistente, Reserva dadosAtualizados, Espaco espacoPrincipal) {
        StatusReserva statusAnterior = reservaExistente.getStatus();
        StatusReserva statusSolicitado = dadosAtualizados.getStatus() != null
                ? dadosAtualizados.getStatus()
                : statusAnterior;

        reservaExistente.setStatus(statusSolicitado);
        reservaExistente.setObservacaoAprovacao(dadosAtualizados.getObservacaoAprovacao());

        if (autorizacaoApplication == null || espacoPrincipal == null) {
            reservaExistente.setAprovadaPorUsuarioId(dadosAtualizados.getAprovadaPorUsuarioId());
            reservaExistente.setAprovadaEm(dadosAtualizados.getAprovadaEm());
            return;
        }

        Usuario usuarioAtual = autorizacaoApplication.obterUsuarioAtualObrigatorio();
        boolean exigeAprovacao = Boolean.TRUE.equals(espacoPrincipal.getExigeAprovacao());
        boolean podeAprovar = autorizacaoApplication.podeAprovarReservaNoEspaco(usuarioAtual, espacoPrincipal);

        if (exigeAprovacao && statusAnterior == StatusReserva.PENDENTE && statusSolicitado == StatusReserva.CONFIRMADA && !podeAprovar) {
            throw new BusinessException("Somente responsaveis do espaco ou aprovadores podem confirmar reservas pendentes.");
        }

        if (statusSolicitado == StatusReserva.PENDENTE) {
            reservaExistente.setAprovadaPorUsuarioId(null);
            reservaExistente.setAprovadaEm(null);
            return;
        }

        if (statusSolicitado == StatusReserva.CONFIRMADA && podeAprovar) {
            registrarDecisaoAprovacao(reservaExistente, usuarioAtual);
            return;
        }

        if (statusSolicitado == StatusReserva.CANCELADA && exigeAprovacao && podeAprovar) {
            registrarDecisaoAprovacao(reservaExistente, usuarioAtual);
            return;
        }

        reservaExistente.setAprovadaPorUsuarioId(dadosAtualizados.getAprovadaPorUsuarioId());
        reservaExistente.setAprovadaEm(dadosAtualizados.getAprovadaEm());
    }

    private void registrarDecisaoAprovacao(Reserva reserva, Usuario aprovador) {
        reserva.setAprovadaPorUsuarioId(aprovador.getIdUsuario());
        reserva.setAprovadaEm(LocalDateTime.now());
    }
}
