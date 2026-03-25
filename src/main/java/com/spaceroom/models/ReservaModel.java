package com.spaceroom.models;

import com.spaceroom.entities.StatusReserva;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaModel {

    private Long idReserva;

    @NotNull(message = "O id da instituição é obrigatório.")
    private Long idInstituicao;

    @NotNull(message = "O id do usuário é obrigatório.")
    private Long idUsuario;

    @NotNull(message = "O id do espaço é obrigatório.")
    private Long idEspaco;

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    private String finalidade;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim é obrigatória.")
    private LocalDateTime dataFim;

    private StatusReserva status;

    private String observacao;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}