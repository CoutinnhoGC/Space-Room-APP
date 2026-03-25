package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaEquipamentoModel {

    private Long idReservaEquipamento;

    @NotNull(message = "O id da reserva e obrigatorio.")
    private Long idReserva;

    @NotNull(message = "O id do equipamento e obrigatorio.")
    private Long idEquipamento;

    private Integer quantidade;
}
