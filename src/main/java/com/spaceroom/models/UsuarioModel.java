package com.spaceroom.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioModel {

    private Long idUsuario;

    @NotNull(message = "O id da instituição é obrigatório.")
    private Long idInstituicao;

    @NotNull(message = "O id do cargo é obrigatório.")
    private Integer idCargo;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
    private String email;

    @Size(max = 120, message = "A senha deve ter no máximo 120 caracteres.")
    private String senhaHash;

    private Boolean primeiroAcesso;

    @Size(max = 255, message = "O token deve ter no máximo 255 caracteres.")
    private String tokenDefinicaoSenha;

    private LocalDateTime tokenExpiracao;
    private LocalDateTime ultimoLoginEm;
    private Boolean ativo;
    private Boolean podeReservar;
    private Boolean adminPlataforma;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
