package com.spaceroom.models;

import com.spaceroom.entities.TipoEspaco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EspacoModel {

    private Long idEspaco;

    @NotNull(message = "O id da instituicao e obrigatorio.")
    private Long idInstituicao;

    @NotBlank(message = "O nome e obrigatorio.")
    private String nome;

    private String descricao;

    @NotNull(message = "O tipo e obrigatorio.")
    private TipoEspaco tipo;

    private String localizacao;

    @NotNull(message = "A capacidade e obrigatoria.")
    private Integer capacidade;

    private String recursosFixos;
    private String imagemUrl;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
