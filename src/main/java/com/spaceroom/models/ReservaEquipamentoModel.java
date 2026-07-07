package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaEquipamentoModel {

    private Long idReservaEquipamento;

    @NotNull(message = "O id da reserva é obrigatório.")
    private Long idReserva;

    @NotNull(message = "O id do equipamento é obrigatório.")
    private Long idEquipamento;

    private Integer quantidade;
}
