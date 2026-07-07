package com.spaceroom.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LogAcaoModel {

    private Long idLog;
    private Long idUsuario;

    @NotBlank(message = "A ação é obrigatória.")
    private String acao;

    @NotBlank(message = "A entidade e obrigatoria.")
    private String entidade;

    private Long idEntidade;
    private String detalhes;
    private LocalDateTime criadoEm;
}
