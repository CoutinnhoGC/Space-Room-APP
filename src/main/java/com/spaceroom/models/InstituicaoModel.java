package com.spaceroom.models;

import com.spaceroom.entities.TipoInstituicao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InstituicaoModel {

    private Long idInstituicao;
    private Integer idPlano;

    @NotBlank(message = "O nome fantasia e obrigatorio.")
    private String nomeFantasia;

    private String razaoSocial;
    private String cnpj;

    @Email(message = "Informe um e-mail de instituicao valido.")
    private String email;

    private String telefone;
    private String responsavel;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;

    @NotNull(message = "O tipo e obrigatorio.")
    private TipoInstituicao tipo;

    private Boolean vitrineHabilitada;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
