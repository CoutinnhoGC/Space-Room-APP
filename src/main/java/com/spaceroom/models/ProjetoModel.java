package com.spaceroom.models;

import com.spaceroom.entities.StatusProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjetoModel {

    private Long idProjeto;

    @NotNull(message = "O id da instituição é obrigatório.")
    private Long idInstituicao;

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    private String resumo;
    private String descricao;
    private String imagemUrl;
    private String linkExterno;
    private StatusProjeto status;

    @NotNull(message = "O id do criador é obrigatório.")
    private Long criadoPor;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
