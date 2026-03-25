package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoPermissaoModel {

    @NotNull(message = "O id do cargo e obrigatorio.")
    private Integer idCargo;

    @NotNull(message = "O id da permissao e obrigatorio.")
    private Integer idPermissao;
}
