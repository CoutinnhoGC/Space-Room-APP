package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjetoIntegranteModel {

    private Long idProjetoIntegrante;

    @NotNull(message = "O id do projeto e obrigatorio.")
    private Long idProjeto;

    @NotNull(message = "O id do usuario e obrigatorio.")
    private Long idUsuario;

    private String funcao;
}
