package com.spaceroom.applications;

import com.spaceroom.entities.HorarioFuncionamentoEspaco;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.HorarioFuncionamentoEspacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioFuncionamentoEspacoApplication {

    private final HorarioFuncionamentoEspacoRepository horarioFuncionamentoEspacoRepository;

    public HorarioFuncionamentoEspaco criar(HorarioFuncionamentoEspaco horario) {
        return horarioFuncionamentoEspacoRepository.save(horario);
    }

    public List<HorarioFuncionamentoEspaco> listarTodos() {
        return horarioFuncionamentoEspacoRepository.findAll();
    }

    public HorarioFuncionamentoEspaco buscarPorId(Long idHorario) {
        return horarioFuncionamentoEspacoRepository.findById(idHorario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HorarioFuncionamentoEspaco nao encontrado para o id: " + idHorario
                ));
    }

    public HorarioFuncionamentoEspaco atualizar(Long idHorario, HorarioFuncionamentoEspaco dadosAtualizados) {
        HorarioFuncionamentoEspaco horarioExistente = buscarPorId(idHorario);

        horarioExistente.setIdEspaco(dadosAtualizados.getIdEspaco());
        horarioExistente.setDiaSemana(dadosAtualizados.getDiaSemana());
        horarioExistente.setHoraInicio(dadosAtualizados.getHoraInicio());
        horarioExistente.setHoraFim(dadosAtualizados.getHoraFim());

        return horarioFuncionamentoEspacoRepository.save(horarioExistente);
    }

    public void deletar(Long idHorario) {
        HorarioFuncionamentoEspaco horario = buscarPorId(idHorario);
        horarioFuncionamentoEspacoRepository.delete(horario);
    }
}
