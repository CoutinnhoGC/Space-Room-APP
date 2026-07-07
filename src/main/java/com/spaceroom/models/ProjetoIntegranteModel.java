package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjetoIntegranteModel {

    private Long idProjetoIntegrante;

    @NotNull(message = "O id do projeto é obrigatório.")
    private Long idProjeto;

    @NotNull(message = "O id do usuário é obrigatório.")
    private Long idUsuario;

    private String funcao;
}
