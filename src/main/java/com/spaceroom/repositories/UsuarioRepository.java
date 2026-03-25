package com.spaceroom.repositories;

import com.spaceroom.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByIdInstituicao(Long idInstituicao);

    List<Usuario> findByIdCargo(Integer idCargo);

    Optional<Usuario> findByEmail(String email);
}
