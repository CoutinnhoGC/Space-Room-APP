package com.spaceroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projeto_integrante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetoIntegrante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto_integrante")
    private Long idProjetoIntegrante;

    @Column(name = "id_projeto", nullable = false)
    private Long idProjeto;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "funcao")
    private String funcao;
}
