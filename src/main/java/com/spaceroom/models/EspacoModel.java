package com.spaceroom.models;

import com.spaceroom.entities.TipoEspaco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EspacoModel {

    private Long idEspaco;

    @NotNull(message = "O id da instituicao e obrigatorio.")
    private Long idInstituicao;

    private Long idEspacoPai;

    @NotBlank(message = "O nome e obrigatorio.")
    @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres.")
    private String nome;

    @Size(max = 500, message = "A descricao deve ter no maximo 500 caracteres.")
    private String descricao;

    @NotNull(message = "O tipo e obrigatorio.")
    private TipoEspaco tipo;

    @Size(max = 120, message = "A localizacao deve ter no maximo 120 caracteres.")
    private String localizacao;

    @NotNull(message = "A capacidade e obrigatoria.")
    private Integer capacidade;

    @Size(max = 500, message = "Os recursos fixos devem ter no maximo 500 caracteres.")
    private String recursosFixos;

    @Size(max = 500, message = "A URL da imagem deve ter no maximo 500 caracteres.")
    private String imagemUrl;

    @Size(max = 80, message = "O codigo da unidade deve ter no maximo 80 caracteres.")
    private String codigoUnidade;

    private Boolean permiteSubespacos;
    private Boolean exigeAprovacao;
    private Long idResponsavelEspaco;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
