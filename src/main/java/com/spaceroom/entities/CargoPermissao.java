package com.spaceroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cargo_permissao")
@IdClass(CargoPermissaoId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoPermissao {

    @Id
    @Column(name = "id_cargo", nullable = false)
    private Integer idCargo;

    @Id
    @Column(name = "id_permissao", nullable = false)
    private Integer idPermissao;
}
