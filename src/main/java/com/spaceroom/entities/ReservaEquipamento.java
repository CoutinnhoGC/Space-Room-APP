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

@Entity
@Table(name = "reserva_equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_equipamento")
    private Long idReservaEquipamento;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(name = "id_equipamento", nullable = false)
    private Long idEquipamento;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @PrePersist
    public void prePersist() {
        if (this.quantidade == null) {
            this.quantidade = 1;
        }
    }
}
