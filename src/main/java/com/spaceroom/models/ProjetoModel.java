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

    @NotNull(message = "O id da instituicao e obrigatorio.")
    private Long idInstituicao;

    @NotBlank(message = "O titulo e obrigatorio.")
    private String titulo;

    private String resumo;
    private String descricao;
    private String imagemUrl;
    private String linkExterno;
    private StatusProjeto status;

    @NotNull(message = "O id do criador e obrigatorio.")
    private Long criadoPor;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
