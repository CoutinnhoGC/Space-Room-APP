package com.spaceroom.models;

import com.spaceroom.entities.TipoInstituicao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InstituicaoModel {

    private Long idInstituicao;
    private Integer idPlano;

    @NotBlank(message = "O nome fantasia é obrigatório.")
    @Size(max = 120, message = "O nome fantasia deve ter no máximo 120 caracteres.")
    private String nomeFantasia;

    @Size(max = 160, message = "A razão social deve ter no máximo 160 caracteres.")
    private String razaoSocial;

    @Size(max = 30, message = "O CNPJ deve ter no máximo 30 caracteres.")
    private String cnpj;

    @Email(message = "Informe um e-mail de instituição válido.")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
    private String email;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    private String telefone;

    @Size(max = 120, message = "O responsável deve ter no máximo 120 caracteres.")
    private String responsavel;

    @Size(max = 160, message = "O endereço deve ter no máximo 160 caracteres.")
    private String endereco;

    @Size(max = 80, message = "A cidade deve ter no máximo 80 caracteres.")
    private String cidade;

    @Size(max = 2, message = "O estado deve ter no máximo 2 caracteres.")
    private String estado;

    @Size(max = 20, message = "O CEP deve ter no máximo 20 caracteres.")
    private String cep;

    @NotNull(message = "O tipo é obrigatório.")
    private TipoInstituicao tipo;

    private Boolean vitrineHabilitada;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
