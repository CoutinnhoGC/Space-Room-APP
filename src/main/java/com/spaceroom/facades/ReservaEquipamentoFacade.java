package com.spaceroom.facades;

import com.spaceroom.applications.ReservaEquipamentoApplication;
import com.spaceroom.entities.ReservaEquipamento;
import com.spaceroom.models.ReservaEquipamentoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservaEquipamentoFacade {

    private final ReservaEquipamentoApplication reservaEquipamentoApplication;

    public ReservaEquipamentoModel criar(ReservaEquipamentoModel model) {
        ReservaEquipamento reservaEquipamento = converterModelParaEntity(model);
        ReservaEquipamento reservaEquipamentoSalvo = reservaEquipamentoApplication.criar(reservaEquipamento);
        return converterEntityParaModel(reservaEquipamentoSalvo);
    }

    public List<ReservaEquipamentoModel> listarTodos() {
        return reservaEquipamentoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public ReservaEquipamentoModel buscarPorId(Long idReservaEquipamento) {
        ReservaEquipamento reservaEquipamento = reservaEquipamentoApplication.buscarPorId(idReservaEquipamento);
        return converterEntityParaModel(reservaEquipamento);
    }

    public ReservaEquipamentoModel atualizar(Long idReservaEquipamento, ReservaEquipamentoModel model) {
        ReservaEquipamento reservaEquipamento = converterModelParaEntity(model);
        ReservaEquipamento reservaEquipamentoAtualizado = reservaEquipamentoApplication.atualizar(idReservaEquipamento, reservaEquipamento);
        return converterEntityParaModel(reservaEquipamentoAtualizado);
    }

    public void deletar(Long idReservaEquipamento) {
        reservaEquipamentoApplication.deletar(idReservaEquipamento);
    }

    private ReservaEquipamento converterModelParaEntity(ReservaEquipamentoModel model) {
        return ReservaEquipamento.builder()
                .idReservaEquipamento(model.getIdReservaEquipamento())
                .idReserva(model.getIdReserva())
                .idEquipamento(model.getIdEquipamento())
                .quantidade(model.getQuantidade())
                .build();
    }

    private ReservaEquipamentoModel converterEntityParaModel(ReservaEquipamento reservaEquipamento) {
        ReservaEquipamentoModel model = new ReservaEquipamentoModel();
        model.setIdReservaEquipamento(reservaEquipamento.getIdReservaEquipamento());
        model.setIdReserva(reservaEquipamento.getIdReserva());
        model.setIdEquipamento(reservaEquipamento.getIdEquipamento());
        model.setQuantidade(reservaEquipamento.getQuantidade());
        return model;
    }
}
