package com.spaceroom.repositories;

import com.spaceroom.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByIdInstituicao(Long idInstituicao);

    @Query("select u.idInstituicao, count(u) from Usuario u where u.idInstituicao in :ids group by u.idInstituicao")
    List<Object[]> countByInstituicaoIds(@Param("ids") List<Long> ids);

    List<Usuario> findByIdCargo(Integer idCargo);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTokenDefinicaoSenha(String tokenDefinicaoSenha);
}
