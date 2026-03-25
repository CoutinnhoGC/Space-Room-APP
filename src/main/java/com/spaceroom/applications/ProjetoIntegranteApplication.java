package com.spaceroom.applications;

import com.spaceroom.entities.ProjetoIntegrante;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.ProjetoIntegranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoIntegranteApplication {

    private final ProjetoIntegranteRepository projetoIntegranteRepository;

    public ProjetoIntegrante criar(ProjetoIntegrante projetoIntegrante) {
        return projetoIntegranteRepository.save(projetoIntegrante);
    }

    public List<ProjetoIntegrante> listarTodos() {
        return projetoIntegranteRepository.findAll();
    }

    public ProjetoIntegrante buscarPorId(Long idProjetoIntegrante) {
        return projetoIntegranteRepository.findById(idProjetoIntegrante)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ProjetoIntegrante nao encontrado para o id: " + idProjetoIntegrante
                ));
    }

    public ProjetoIntegrante atualizar(Long idProjetoIntegrante, ProjetoIntegrante dadosAtualizados) {
        ProjetoIntegrante projetoIntegranteExistente = buscarPorId(idProjetoIntegrante);

        projetoIntegranteExistente.setIdProjeto(dadosAtualizados.getIdProjeto());
        projetoIntegranteExistente.setIdUsuario(dadosAtualizados.getIdUsuario());
        projetoIntegranteExistente.setFuncao(dadosAtualizados.getFuncao());

        return projetoIntegranteRepository.save(projetoIntegranteExistente);
    }

    public void deletar(Long idProjetoIntegrante) {
        ProjetoIntegrante projetoIntegrante = buscarPorId(idProjetoIntegrante);
        projetoIntegranteRepository.delete(projetoIntegrante);
    }
}
