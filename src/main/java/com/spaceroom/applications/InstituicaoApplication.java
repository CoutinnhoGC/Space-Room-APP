package com.spaceroom.applications;

import com.spaceroom.entities.Instituicao;
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
        return instituicaoRepository.save(instituicao);
    }

    public List<Instituicao> listarTodas() {
        return instituicaoRepository.findAll();
    }

    public Instituicao buscarPorId(Long idInstituicao) {
        return instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Instituicao nao encontrada para o id: " + idInstituicao
                ));
    }

    public Instituicao atualizar(Long idInstituicao, Instituicao dadosAtualizados) {
        autorizacaoApplication.validarAcessoInstituicao(idInstituicao);
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
        instituicaoExistente.setTipo(dadosAtualizados.getTipo());
        instituicaoExistente.setVitrineHabilitada(dadosAtualizados.getVitrineHabilitada());
        instituicaoExistente.setAtivo(dadosAtualizados.getAtivo());

        return instituicaoRepository.save(instituicaoExistente);
    }

    public void deletar(Long idInstituicao) {
        autorizacaoApplication.validarCriacaoInstituicao();
        Instituicao instituicao = buscarPorId(idInstituicao);
        instituicaoRepository.delete(instituicao);
    }
}
