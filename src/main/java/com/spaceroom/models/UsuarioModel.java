package com.spaceroom.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^\\s*[\\p{L}]+(?:[\\s-]+[\\p{L}]+)*\\s*$", message = "O nome completo deve conter apenas letras, acentos, espaços e hífen.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail valido.")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 120, message = "A senha deve ter no máximo 120 caracteres.")
    private String senhaHash;

    private Boolean primeiroAcesso;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 255, message = "O token deve ter no máximo 255 caracteres.")
    private String tokenDefinicaoSenha;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime tokenExpiracao;
    private LocalDateTime ultimoLoginEm;
    private Boolean ativo;
    private Boolean podeReservar;
    private Boolean adminPlataforma;
    private Boolean podeGerenciarUsuarios;
    private Boolean podeGerenciarEspacos;
    private Boolean podeAprovarReservas;
    private Boolean podeGerenciarComunicados;
    private Boolean podeVisualizarAuditoria;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
