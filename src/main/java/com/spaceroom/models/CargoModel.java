package com.spaceroom.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoModel {

    private Integer idCargo;

    @NotBlank(message = "O nome e obrigatorio.")
    private String nome;

    private String descricao;
    private Boolean ativo;
}
