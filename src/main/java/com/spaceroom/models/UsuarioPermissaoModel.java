package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPermissaoModel {

    @NotNull(message = "O id do usuário é obrigatório.")
    private Long idUsuario;

    @NotNull(message = "O id da permissão é obrigatório.")
    private Integer idPermissao;

    private Boolean concedida;
}
