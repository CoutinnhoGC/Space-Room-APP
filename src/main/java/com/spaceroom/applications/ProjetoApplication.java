package com.spaceroom.applications;

import com.spaceroom.entities.Projeto;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoApplication {

    private final ProjetoRepository projetoRepository;

    public Projeto criar(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    public Projeto buscarPorId(Long idProjeto) {
        return projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Projeto nao encontrado para o id: " + idProjeto
                ));
    }

    public Projeto atualizar(Long idProjeto, Projeto dadosAtualizados) {
        Projeto projetoExistente = buscarPorId(idProjeto);

        projetoExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        projetoExistente.setTitulo(dadosAtualizados.getTitulo());
        projetoExistente.setResumo(dadosAtualizados.getResumo());
        projetoExistente.setDescricao(dadosAtualizados.getDescricao());
        projetoExistente.setImagemUrl(dadosAtualizados.getImagemUrl());
        projetoExistente.setLinkExterno(dadosAtualizados.getLinkExterno());
        projetoExistente.setStatus(dadosAtualizados.getStatus());
        projetoExistente.setCriadoPor(dadosAtualizados.getCriadoPor());

        return projetoRepository.save(projetoExistente);
    }

    public void deletar(Long idProjeto) {
        Projeto projeto = buscarPorId(idProjeto);
        projetoRepository.delete(projeto);
    }
}
