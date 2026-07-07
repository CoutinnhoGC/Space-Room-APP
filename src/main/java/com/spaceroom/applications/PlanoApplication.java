package com.spaceroom.applications;

import com.spaceroom.entities.Plano;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoApplication {

    private final PlanoRepository planoRepository;

    public Plano criar(Plano plano) {
        return planoRepository.save(plano);
    }

    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }

    public Plano buscarPorId(Integer idPlano) {
        return planoRepository.findById(idPlano)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plano não encontrado para o id: " + idPlano
                ));
    }

    public Plano atualizar(Integer idPlano, Plano dadosAtualizados) {
        Plano planoExistente = buscarPorId(idPlano);

        planoExistente.setNome(dadosAtualizados.getNome());
        planoExistente.setValor(dadosAtualizados.getValor());
        planoExistente.setDescricao(dadosAtualizados.getDescricao());
        planoExistente.setLimiteUsuarios(dadosAtualizados.getLimiteUsuarios());
        planoExistente.setLimiteEspacos(dadosAtualizados.getLimiteEspacos());
        planoExistente.setLimiteReservasMes(dadosAtualizados.getLimiteReservasMes());
        planoExistente.setVitrineIncluida(dadosAtualizados.getVitrineIncluida());
        planoExistente.setAtivo(dadosAtualizados.getAtivo());

        return planoRepository.save(planoExistente);
    }

    public void deletar(Integer idPlano) {
        Plano plano = buscarPorId(idPlano);
        planoRepository.delete(plano);
    }
}
