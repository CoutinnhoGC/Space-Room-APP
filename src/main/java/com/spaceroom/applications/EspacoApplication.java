package com.spaceroom.applications;

import com.spaceroom.entities.Espaco;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.EspacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspacoApplication {

    private final EspacoRepository espacoRepository;

    public Espaco criar(Espaco espaco) {
        return espacoRepository.save(espaco);
    }

    public List<Espaco> listarTodos() {
        return espacoRepository.findAll();
    }

    public Espaco buscarPorId(Long idEspaco) {
        return espacoRepository.findById(idEspaco)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Espaco nao encontrado para o id: " + idEspaco
                ));
    }

    public Espaco atualizar(Long idEspaco, Espaco dadosAtualizados) {
        Espaco espacoExistente = buscarPorId(idEspaco);

        espacoExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        espacoExistente.setNome(dadosAtualizados.getNome());
        espacoExistente.setDescricao(dadosAtualizados.getDescricao());
        espacoExistente.setTipo(dadosAtualizados.getTipo());
        espacoExistente.setLocalizacao(dadosAtualizados.getLocalizacao());
        espacoExistente.setCapacidade(dadosAtualizados.getCapacidade());
        espacoExistente.setRecursosFixos(dadosAtualizados.getRecursosFixos());
        espacoExistente.setImagemUrl(dadosAtualizados.getImagemUrl());
        espacoExistente.setAtivo(dadosAtualizados.getAtivo());

        return espacoRepository.save(espacoExistente);
    }

    public void deletar(Long idEspaco) {
        Espaco espaco = buscarPorId(idEspaco);
        espacoRepository.delete(espaco);
    }
}
