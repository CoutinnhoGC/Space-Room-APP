package com.spaceroom.applications;

import com.spaceroom.entities.Permissao;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissaoApplication {

    private final PermissaoRepository permissaoRepository;

    public Permissao criar(Permissao permissao) {
        return permissaoRepository.save(permissao);
    }

    public List<Permissao> listarTodas() {
        return permissaoRepository.findAll();
    }

    public Permissao buscarPorId(Integer idPermissao) {
        return permissaoRepository.findById(idPermissao)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permissão não encontrada para o id: " + idPermissao
                ));
    }

    public Permissao atualizar(Integer idPermissao, Permissao dadosAtualizados) {
        Permissao permissaoExistente = buscarPorId(idPermissao);

        permissaoExistente.setNome(dadosAtualizados.getNome());
        permissaoExistente.setDescricao(dadosAtualizados.getDescricao());

        return permissaoRepository.save(permissaoExistente);
    }

    public void deletar(Integer idPermissao) {
        Permissao permissao = buscarPorId(idPermissao);
        permissaoRepository.delete(permissao);
    }
}
