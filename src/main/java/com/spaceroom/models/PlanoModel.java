package com.spaceroom.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PlanoModel {

    private Integer idPlano;

    @NotBlank(message = "O nome e obrigatorio.")
    private String nome;

    @NotNull(message = "O valor e obrigatorio.")
    private BigDecimal valor;

    private String descricao;
    private Integer limiteUsuarios;
    private Integer limiteEspacos;
    private Integer limiteReservasMes;
    private Boolean vitrineIncluida;
    private Boolean multiunidadeHabilitada;
    private Boolean workflowAprovacaoHabilitado;
    private Boolean auditoriaAvancadaHabilitada;
    private String modulosHabilitados;
    private Boolean ativo;
    private LocalDateTime criadoEm;
}
