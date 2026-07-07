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

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotNull(message = "O valor é obrigatório.")
    private BigDecimal valor;

    private String descricao;
    private Integer limiteUsuarios;
    private Integer limiteEspacos;
    private Integer limiteReservasMes;
    private Boolean vitrineIncluida;
    private Boolean ativo;
    private LocalDateTime criadoEm;
}
