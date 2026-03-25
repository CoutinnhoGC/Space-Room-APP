package com.spaceroom.repositories;

import com.spaceroom.entities.UsuarioPermissao;
import com.spaceroom.entities.UsuarioPermissaoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioPermissaoRepository extends JpaRepository<UsuarioPermissao, UsuarioPermissaoId> {

    List<UsuarioPermissao> findByIdUsuario(Long idUsuario);

    List<UsuarioPermissao> findByIdPermissao(Integer idPermissao);
}
