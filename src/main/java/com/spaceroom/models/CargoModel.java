package com.spaceroom.models;

import com.spaceroom.entities.TipoInstituicao;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoModel {

    private Integer idCargo;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private String descricao;
    private Long idInstituicao;
    private TipoInstituicao tipoInstituicao;
    private Boolean sistema;
    private Boolean personalizado;
    private Boolean ativo;
}
