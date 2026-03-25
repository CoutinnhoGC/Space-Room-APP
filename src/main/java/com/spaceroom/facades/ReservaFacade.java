package com.spaceroom.facades;

import com.spaceroom.applications.ReservaApplication;
import com.spaceroom.entities.Reserva;
import com.spaceroom.entities.StatusReserva;
import com.spaceroom.models.ReservaModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservaFacade {

    private final ReservaApplication reservaApplication;

    public ReservaModel criar(ReservaModel model) {
        Reserva reserva = converterModelParaEntity(model);
        Reserva reservaSalva = reservaApplication.criar(reserva);
        return converterEntityParaModel(reservaSalva);
    }

    public List<ReservaModel> listarTodas() {
        return reservaApplication.listarTodas()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public ReservaModel buscarPorId(Long idReserva) {
        Reserva reserva = reservaApplication.buscarPorId(idReserva);
        return converterEntityParaModel(reserva);
    }

    public ReservaModel atualizar(Long idReserva, ReservaModel model) {
        Reserva reserva = converterModelParaEntity(model);
        Reserva reservaAtualizada = reservaApplication.atualizar(idReserva, reserva);
        return converterEntityParaModel(reservaAtualizada);
    }

    public void deletar(Long idReserva) {
        reservaApplication.deletar(idReserva);
    }

    private Reserva converterModelParaEntity(ReservaModel model) {
        return Reserva.builder()
                .idReserva(model.getIdReserva())
                .idInstituicao(model.getIdInstituicao())
                .idUsuario(model.getIdUsuario())
                .idEspaco(model.getIdEspaco())
                .titulo(model.getTitulo())
                .finalidade(model.getFinalidade())
                .dataInicio(model.getDataInicio())
                .dataFim(model.getDataFim())
                .status(model.getStatus() != null ? model.getStatus() : StatusReserva.PENDENTE)
                .observacao(model.getObservacao())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }

    private ReservaModel converterEntityParaModel(Reserva reserva) {
        ReservaModel model = new ReservaModel();
        model.setIdReserva(reserva.getIdReserva());
        model.setIdInstituicao(reserva.getIdInstituicao());
        model.setIdUsuario(reserva.getIdUsuario());
        model.setIdEspaco(reserva.getIdEspaco());
        model.setTitulo(reserva.getTitulo());
        model.setFinalidade(reserva.getFinalidade());
        model.setDataInicio(reserva.getDataInicio());
        model.setDataFim(reserva.getDataFim());
        model.setStatus(reserva.getStatus());
        model.setObservacao(reserva.getObservacao());
        model.setCriadoEm(reserva.getCriadoEm());
        model.setAtualizadoEm(reserva.getAtualizadoEm());
        return model;
    }
}