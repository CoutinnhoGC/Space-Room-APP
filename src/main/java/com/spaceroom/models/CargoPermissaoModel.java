package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoPermissaoModel {

    @NotNull(message = "O id do cargo é obrigatório.")
    private Integer idCargo;

    @NotNull(message = "O id da permissão é obrigatório.")
    private Integer idPermissao;
}
