package com.spaceroom.facades;

import com.spaceroom.applications.InstituicaoApplication;
import com.spaceroom.entities.Instituicao;
import com.spaceroom.models.InstituicaoModel;
import com.spaceroom.models.InstituicaoResumoModel;
import com.spaceroom.repositories.EspacoRepository;
import com.spaceroom.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstituicaoFacade {

    private final InstituicaoApplication instituicaoApplication;
    private final UsuarioRepository usuarioRepository;
    private final EspacoRepository espacoRepository;

    public InstituicaoModel criar(InstituicaoModel model) {
        Instituicao instituicao = converterModelParaEntity(model);
        Instituicao instituicaoSalva = instituicaoApplication.criar(instituicao);
        return converterEntityParaModel(instituicaoSalva);
    }

    public List<InstituicaoModel> listarTodas() {
        return instituicaoApplication.listarTodas()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public List<InstituicaoResumoModel> listarResumo() {
        List<Instituicao> instituicoes = instituicaoApplication.listarTodas();
        List<Long> ids = instituicoes.stream()
                .map(Instituicao::getIdInstituicao)
                .toList();
        if (ids.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> usuariosPorInstituicao = contarPorInstituicao(ids, usuarioRepository.countByInstituicaoIds(ids));
        Map<Long, Long> espacosPorInstituicao = contarPorInstituicao(ids, espacoRepository.countByInstituicaoIds(ids));

        return instituicoes.stream()
                .map(instituicao -> converterEntityParaResumoModel(
                        instituicao,
                        usuariosPorInstituicao.getOrDefault(instituicao.getIdInstituicao(), 0L),
                        espacosPorInstituicao.getOrDefault(instituicao.getIdInstituicao(), 0L)
                ))
                .toList();
    }

    public InstituicaoModel buscarPorId(Long idInstituicao) {
        Instituicao instituicao = instituicaoApplication.buscarPorId(idInstituicao);
        return converterEntityParaModel(instituicao);
    }

    public InstituicaoModel atualizar(Long idInstituicao, InstituicaoModel model) {
        Instituicao instituicao = converterModelParaEntity(model);
        Instituicao instituicaoAtualizada = instituicaoApplication.atualizar(idInstituicao, instituicao);
        return converterEntityParaModel(instituicaoAtualizada);
    }

    public void deletar(Long idInstituicao) {
        instituicaoApplication.deletar(idInstituicao);
    }

    private Instituicao converterModelParaEntity(InstituicaoModel model) {
        return Instituicao.builder()
                .idInstituicao(model.getIdInstituicao())
                .idPlano(model.getIdPlano())
                .nomeFantasia(model.getNomeFantasia())
                .razaoSocial(model.getRazaoSocial())
                .cnpj(model.getCnpj())
                .email(model.getEmail())
                .telefone(model.getTelefone())
                .responsavel(model.getResponsavel())
                .endereco(model.getEndereco())
                .cidade(model.getCidade())
                .estado(model.getEstado())
                .cep(model.getCep())
                .tipo(model.getTipo())
                .vitrineHabilitada(model.getVitrineHabilitada())
                .ativo(model.getAtivo())
                .criadoEm(model.getCriadoEm())
                .atualizadoEm(model.getAtualizadoEm())
                .build();
    }

    private InstituicaoModel converterEntityParaModel(Instituicao instituicao) {
        InstituicaoModel model = new InstituicaoModel();
        preencherModel(model, instituicao);
        return model;
    }

    private InstituicaoResumoModel converterEntityParaResumoModel(Instituicao instituicao, long totalUsuarios, long totalEspacos) {
        InstituicaoResumoModel model = new InstituicaoResumoModel();
        preencherModel(model, instituicao);
        model.setTotalUsuarios(totalUsuarios);
        model.setTotalEspacos(totalEspacos);
        return model;
    }

    private void preencherModel(InstituicaoModel model, Instituicao instituicao) {
        model.setIdInstituicao(instituicao.getIdInstituicao());
        model.setIdPlano(instituicao.getIdPlano());
        model.setNomeFantasia(instituicao.getNomeFantasia());
        model.setRazaoSocial(instituicao.getRazaoSocial());
        model.setCnpj(instituicao.getCnpj());
        model.setEmail(instituicao.getEmail());
        model.setTelefone(instituicao.getTelefone());
        model.setResponsavel(instituicao.getResponsavel());
        model.setEndereco(instituicao.getEndereco());
        model.setCidade(instituicao.getCidade());
        model.setEstado(instituicao.getEstado());
        model.setCep(instituicao.getCep());
        model.setTipo(instituicao.getTipo());
        model.setVitrineHabilitada(instituicao.getVitrineHabilitada());
        model.setAtivo(instituicao.getAtivo());
        model.setCriadoEm(instituicao.getCriadoEm());
        model.setAtualizadoEm(instituicao.getAtualizadoEm());
    }

    private Map<Long, Long> contarPorInstituicao(List<Long> ids, List<Object[]> resultados) {
        return resultados.stream()
                .collect(Collectors.toMap(
                        linha -> (Long) linha[0],
                        linha -> (Long) linha[1],
                        (primeiro, segundo) -> primeiro
                ));
    }
}
