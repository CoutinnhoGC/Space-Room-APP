package com.spaceroom.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioModel {

    private Long idUsuario;

    @NotNull(message = "O id da instituicao e obrigatorio.")
    private Long idInstituicao;

    @NotNull(message = "O id do cargo e obrigatorio.")
    private Integer idCargo;

    @NotBlank(message = "O nome e obrigatorio.")
    private String nome;

    @NotBlank(message = "O email e obrigatorio.")
    @Email(message = "Informe um e-mail valido.")
    private String email;

    private String senhaHash;

    private Boolean primeiroAcesso;

    private String tokenDefinicaoSenha;

    private LocalDateTime tokenExpiracao;

    private LocalDateTime ultimoLoginEm;

    private Boolean ativo;

    private Boolean podeReservar;

    private Boolean adminPlataforma;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
