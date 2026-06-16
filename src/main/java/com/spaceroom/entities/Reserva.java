package com.spaceroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_instituicao", nullable = false)
    private Long idInstituicao;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_espaco", nullable = false)
    private Long idEspaco;

    @Column(name = "id_subespaco")
    private Long idSubespaco;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "finalidade", columnDefinition = "TEXT")
    private String finalidade;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "status_reserva")
    private StatusReserva status;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "observacao_aprovacao", columnDefinition = "TEXT")
    private String observacaoAprovacao;

    @Column(name = "aprovada_por_usuario_id")
    private Long aprovadaPorUsuarioId;

    @Column(name = "aprovada_em")
    private LocalDateTime aprovadaEm;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        LocalDateTime agora = LocalDateTime.now();
        this.criadoEm = agora;
        this.atualizadoEm = agora;

        if (this.status == null) {
            this.status = StatusReserva.PENDENTE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
