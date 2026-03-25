package com.spaceroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_permissao")
@IdClass(UsuarioPermissaoId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPermissao {

    @Id
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Id
    @Column(name = "id_permissao", nullable = false)
    private Integer idPermissao;

    @Column(name = "concedida", nullable = false)
    private Boolean concedida;

    @PrePersist
    public void prePersist() {
        if (this.concedida == null) {
            this.concedida = true;
        }
    }
}
