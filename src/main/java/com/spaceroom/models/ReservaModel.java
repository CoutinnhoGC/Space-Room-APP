package com.spaceroom.models;

import com.spaceroom.entities.StatusReserva;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private Long idSubespaco;

    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    @NotBlank(message = "A finalidade e obrigatoria.")
    @Size(max = 500, message = "A finalidade deve ter no máximo 500 caracteres.")
    private String finalidade;

    @NotNull(message = "A data de inicio e obrigatoria.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim e obrigatoria.")
    private LocalDateTime dataFim;

    private StatusReserva status;

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres.")
    private String observacao;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
