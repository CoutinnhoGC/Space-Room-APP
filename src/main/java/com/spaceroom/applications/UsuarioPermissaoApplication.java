package com.spaceroom.applications;

import com.spaceroom.entities.UsuarioPermissao;
import com.spaceroom.entities.UsuarioPermissaoId;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.UsuarioPermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioPermissaoApplication {

    private final UsuarioPermissaoRepository usuarioPermissaoRepository;

    public UsuarioPermissao criar(UsuarioPermissao usuarioPermissao) {
        return usuarioPermissaoRepository.save(usuarioPermissao);
    }

    public List<UsuarioPermissao> listarTodos() {
        return usuarioPermissaoRepository.findAll();
    }

    public UsuarioPermissao buscarPorId(Long idUsuario, Integer idPermissao) {
        UsuarioPermissaoId id = new UsuarioPermissaoId(idUsuario, idPermissao);
        return usuarioPermissaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permissão do usuário não encontrada para idUsuario: " + idUsuario + " e idPermissao: " + idPermissao
                ));
    }

    public UsuarioPermissao atualizar(Long idUsuario, Integer idPermissao, UsuarioPermissao dadosAtualizados) {
        UsuarioPermissao usuarioPermissaoExistente = buscarPorId(idUsuario, idPermissao);

        usuarioPermissaoExistente.setIdUsuario(dadosAtualizados.getIdUsuario());
        usuarioPermissaoExistente.setIdPermissao(dadosAtualizados.getIdPermissao());
        usuarioPermissaoExistente.setConcedida(dadosAtualizados.getConcedida());

        return usuarioPermissaoRepository.save(usuarioPermissaoExistente);
    }

    public void deletar(Long idUsuario, Integer idPermissao) {
        UsuarioPermissao usuarioPermissao = buscarPorId(idUsuario, idPermissao);
        usuarioPermissaoRepository.delete(usuarioPermissao);
    }
}
