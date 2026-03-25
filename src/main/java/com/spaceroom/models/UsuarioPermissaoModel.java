package com.spaceroom.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPermissaoModel {

    @NotNull(message = "O id do usuario e obrigatorio.")
    private Long idUsuario;

    @NotNull(message = "O id da permissao e obrigatorio.")
    private Integer idPermissao;

    private Boolean concedida;
}
