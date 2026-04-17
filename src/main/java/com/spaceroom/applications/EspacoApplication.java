package com.spaceroom.applications;

import com.spaceroom.entities.Espaco;
import com.spaceroom.exceptions.BusinessException;
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
        validarHierarquia(espaco);
        return espacoRepository.save(espaco);
    }

    public List<Espaco> listarTodos() {
        return espacoRepository.findAll();
    }

    public Espaco buscarPorId(Long idEspaco) {
        return espacoRepository.findById(idEspaco)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Espaço não encontrado para o id: " + idEspaco
                ));
    }

    public Espaco atualizar(Long idEspaco, Espaco dadosAtualizados) {
        validarHierarquia(dadosAtualizados);
        Espaco espacoExistente = buscarPorId(idEspaco);

        espacoExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        espacoExistente.setIdEspacoPai(dadosAtualizados.getIdEspacoPai());
        espacoExistente.setNome(dadosAtualizados.getNome());
        espacoExistente.setDescricao(dadosAtualizados.getDescricao());
        espacoExistente.setTipo(dadosAtualizados.getTipo());
        espacoExistente.setLocalizacao(dadosAtualizados.getLocalizacao());
        espacoExistente.setCapacidade(dadosAtualizados.getCapacidade());
        espacoExistente.setRecursosFixos(dadosAtualizados.getRecursosFixos());
        espacoExistente.setImagemUrl(dadosAtualizados.getImagemUrl());
        espacoExistente.setPermiteSubespacos(dadosAtualizados.getPermiteSubespacos());
        espacoExistente.setAtivo(dadosAtualizados.getAtivo());

        return espacoRepository.save(espacoExistente);
    }

    public void deletar(Long idEspaco) {
        Espaco espaco = buscarPorId(idEspaco);
        espacoRepository.delete(espaco);
    }

    private void validarHierarquia(Espaco espaco) {
        if (espaco.getIdEspacoPai() == null) {
            return;
        }

        Espaco espacoPai = buscarPorId(espaco.getIdEspacoPai());
        if (espaco.getIdInstituicao() != null && !espaco.getIdInstituicao().equals(espacoPai.getIdInstituicao())) {
            throw new BusinessException("O subespaço deve pertencer à mesma instituição do espaço principal.");
        }

        if (Boolean.FALSE.equals(espacoPai.getPermiteSubespacos())) {
            throw new BusinessException("O espaço principal selecionado não está habilitado para subespaços.");
        }
    }
}