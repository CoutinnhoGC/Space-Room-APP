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

    @NotNull(message = "O id da instituicao e obrigatorio.")
    private Long idInstituicao;

    @NotNull(message = "O id do usuario e obrigatorio.")
    private Long idUsuario;

    @NotNull(message = "O id do espaco e obrigatorio.")
    private Long idEspaco;

    private Long idSubespaco;

    @NotBlank(message = "O titulo e obrigatorio.")
    @Size(max = 150, message = "O titulo deve ter no maximo 150 caracteres.")
    private String titulo;

    @NotBlank(message = "A finalidade e obrigatoria.")
    @Size(max = 500, message = "A finalidade deve ter no maximo 500 caracteres.")
    private String finalidade;

    @NotNull(message = "A data de inicio e obrigatoria.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim e obrigatoria.")
    private LocalDateTime dataFim;

    private StatusReserva status;

    @Size(max = 500, message = "A observacao deve ter no maximo 500 caracteres.")
    private String observacao;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
