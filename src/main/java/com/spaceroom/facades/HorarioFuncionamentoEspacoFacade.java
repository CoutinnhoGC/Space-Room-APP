package com.spaceroom.facades;

import com.spaceroom.applications.HorarioFuncionamentoEspacoApplication;
import com.spaceroom.entities.HorarioFuncionamentoEspaco;
import com.spaceroom.models.HorarioFuncionamentoEspacoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HorarioFuncionamentoEspacoFacade {

    private final HorarioFuncionamentoEspacoApplication horarioFuncionamentoEspacoApplication;

    public HorarioFuncionamentoEspacoModel criar(HorarioFuncionamentoEspacoModel model) {
        HorarioFuncionamentoEspaco horario = converterModelParaEntity(model);
        HorarioFuncionamentoEspaco horarioSalvo = horarioFuncionamentoEspacoApplication.criar(horario);
        return converterEntityParaModel(horarioSalvo);
    }

    public List<HorarioFuncionamentoEspacoModel> listarTodos() {
        return horarioFuncionamentoEspacoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public HorarioFuncionamentoEspacoModel buscarPorId(Long idHorario) {
        HorarioFuncionamentoEspaco horario = horarioFuncionamentoEspacoApplication.buscarPorId(idHorario);
        return converterEntityParaModel(horario);
    }

    public HorarioFuncionamentoEspacoModel atualizar(Long idHorario, HorarioFuncionamentoEspacoModel model) {
        HorarioFuncionamentoEspaco horario = converterModelParaEntity(model);
        HorarioFuncionamentoEspaco horarioAtualizado = horarioFuncionamentoEspacoApplication.atualizar(idHorario, horario);
        return converterEntityParaModel(horarioAtualizado);
    }

    public void deletar(Long idHorario) {
        horarioFuncionamentoEspacoApplication.deletar(idHorario);
    }

    private HorarioFuncionamentoEspaco converterModelParaEntity(HorarioFuncionamentoEspacoModel model) {
        return HorarioFuncionamentoEspaco.builder()
                .idHorario(model.getIdHorario())
                .idEspaco(model.getIdEspaco())
                .diaSemana(model.getDiaSemana())
                .horaInicio(model.getHoraInicio())
                .horaFim(model.getHoraFim())
                .build();
    }

    private HorarioFuncionamentoEspacoModel converterEntityParaModel(HorarioFuncionamentoEspaco horario) {
        HorarioFuncionamentoEspacoModel model = new HorarioFuncionamentoEspacoModel();
        model.setIdHorario(horario.getIdHorario());
        model.setIdEspaco(horario.getIdEspaco());
        model.setDiaSemana(horario.getDiaSemana());
        model.setHoraInicio(horario.getHoraInicio());
        model.setHoraFim(horario.getHoraFim());
        return model;
    }
}
