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

    @NotNull(message = "O id da instituição é obrigatório.")
    private Long idInstituicao;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String descricao;

    @NotNull(message = "O tipo é obrigatório.")
    private TipoEspaco tipo;

    @Size(max = 120, message = "A localização deve ter no máximo 120 caracteres.")
    private String localizacao;

    @NotNull(message = "A capacidade é obrigatória.")
    private Integer capacidade;

    @Size(max = 500, message = "Os recursos fixos devem ter no máximo 500 caracteres.")
    private String recursosFixos;

    @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres.")
    private String imagemUrl;

    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
