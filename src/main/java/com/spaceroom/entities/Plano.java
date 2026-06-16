package com.spaceroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    private Integer idPlano;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "limite_usuarios")
    private Integer limiteUsuarios;

    @Column(name = "limite_espacos")
    private Integer limiteEspacos;

    @Column(name = "limite_reservas_mes")
    private Integer limiteReservasMes;

    @Column(name = "vitrine_incluida", nullable = false)
    private Boolean vitrineIncluida;

    @Column(name = "multiunidade_habilitada", nullable = false)
    private Boolean multiunidadeHabilitada;

    @Column(name = "workflow_aprovacao_habilitado", nullable = false)
    private Boolean workflowAprovacaoHabilitado;

    @Column(name = "auditoria_avancada_habilitada", nullable = false)
    private Boolean auditoriaAvancadaHabilitada;

    @Column(name = "modulos_habilitados", columnDefinition = "TEXT")
    private String modulosHabilitados;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();

        if (this.vitrineIncluida == null) {
            this.vitrineIncluida = false;
        }

        if (this.multiunidadeHabilitada == null) {
            this.multiunidadeHabilitada = false;
        }

        if (this.workflowAprovacaoHabilitado == null) {
            this.workflowAprovacaoHabilitado = false;
        }

        if (this.auditoriaAvancadaHabilitada == null) {
            this.auditoriaAvancadaHabilitada = false;
        }

        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}
