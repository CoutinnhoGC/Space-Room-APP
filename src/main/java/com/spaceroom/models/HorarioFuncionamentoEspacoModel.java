package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class HorarioFuncionamentoEspacoModel {

    private Long idHorario;

    @NotNull(message = "O id do espaço é obrigatório.")
    private Long idEspaco;

    @NotNull(message = "O dia da semana é obrigatório.")
    private Integer diaSemana;

    @NotNull(message = "A hora de inicio e obrigatoria.")
    private LocalTime horaInicio;

    @NotNull(message = "A hora de fim e obrigatoria.")
    private LocalTime horaFim;
}
