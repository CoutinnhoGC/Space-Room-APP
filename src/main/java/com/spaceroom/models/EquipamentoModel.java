package com.spaceroom.models;

import com.spaceroom.entities.StatusEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EquipamentoModel {

    private Long idEquipamento;

    @NotNull(message = "O id da instituição é obrigatório.")
    private Long idInstituicao;

    private Long idEspaco;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private String descricao;
    private String patrimonio;
    private StatusEquipamento status;
    private Integer quantidadeTotal;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
