package com.spaceroom.repositories;

import com.spaceroom.entities.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EspacoRepository extends JpaRepository<Espaco, Long> {

    List<Espaco> findByIdInstituicao(Long idInstituicao);

    @Query("select e.idInstituicao, count(e) from Espaco e where e.idInstituicao in :ids group by e.idInstituicao")
    List<Object[]> countByInstituicaoIds(@Param("ids") List<Long> ids);
}
