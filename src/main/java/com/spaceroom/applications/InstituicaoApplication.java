package com.spaceroom.applications;

import com.spaceroom.entities.Instituicao;
import com.spaceroom.entities.TipoInstituicao;
import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.InstituicaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituicaoApplication {

    private final InstituicaoRepository instituicaoRepository;
    private final AutorizacaoApplication autorizacaoApplication;

    public Instituicao criar(Instituicao instituicao) {
        autorizacaoApplication.validarCriacaoInstituicao();
        instituicao.setTipo(normalizarTipoInstituicao(instituicao.getTipo()));
        return instituicaoRepository.save(instituicao);
    }

    public List<Instituicao> listarTodas() {
        Usuario usuarioAtual = autorizacaoApplication.obterUsuarioAtualObrigatorio();
        if (autorizacaoApplication.isAdminPlataforma(usuarioAtual)) {
            return instituicaoRepository.findAll();
        }
        return List.of(buscarPorId(usuarioAtual.getIdInstituicao()));
    }

    public Instituicao buscarPorId(Long idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada para o id: " + idInstituicao));
        autorizacaoApplication.validarAcessoInstituicao(instituicao.getIdInstituicao());
        return instituicao;
    }

    public Instituicao atualizar(Long idInstituicao, Instituicao dadosAtualizados) {
        autorizacaoApplication.validarGerenciamentoInstituicao(idInstituicao);
        Instituicao instituicaoExistente = buscarPorId(idInstituicao);

        instituicaoExistente.setIdPlano(dadosAtualizados.getIdPlano());
        instituicaoExistente.setNomeFantasia(dadosAtualizados.getNomeFantasia());
        instituicaoExistente.setRazaoSocial(dadosAtualizados.getRazaoSocial());
        instituicaoExistente.setCnpj(dadosAtualizados.getCnpj());
        instituicaoExistente.setEmail(dadosAtualizados.getEmail());
        instituicaoExistente.setTelefone(dadosAtualizados.getTelefone());
        instituicaoExistente.setResponsavel(dadosAtualizados.getResponsavel());
        instituicaoExistente.setEndereco(dadosAtualizados.getEndereco());
        instituicaoExistente.setCidade(dadosAtualizados.getCidade());
        instituicaoExistente.setEstado(dadosAtualizados.getEstado());
        instituicaoExistente.setCep(dadosAtualizados.getCep());
        instituicaoExistente.setTipo(normalizarTipoInstituicao(dadosAtualizados.getTipo()));
        instituicaoExistente.setVitrineHabilitada(dadosAtualizados.getVitrineHabilitada());
        instituicaoExistente.setAtivo(dadosAtualizados.getAtivo());

        return instituicaoRepository.save(instituicaoExistente);
    }

    public void deletar(Long idInstituicao) {
        autorizacaoApplication.validarCriacaoInstituicao();
        Instituicao instituicao = buscarPorId(idInstituicao);
        instituicaoRepository.delete(instituicao);
    }

    private TipoInstituicao normalizarTipoInstituicao(TipoInstituicao tipo) {
        if (tipo == null) {
            return TipoInstituicao.OUTRO;
        }

        return switch (tipo) {
            case ESCOLA, FACULDADE, UNIVERSIDADE, SENAI -> TipoInstituicao.INSTITUICAO_ENSINO;
            default -> tipo;
        };
    }
}
