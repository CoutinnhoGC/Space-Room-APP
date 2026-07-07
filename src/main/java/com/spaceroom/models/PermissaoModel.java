package com.spaceroom.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissaoModel {

    private Integer idPermissao;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private String descricao;
}
