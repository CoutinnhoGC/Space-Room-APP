package com.spaceroom.facades;

import com.spaceroom.applications.LogAcaoApplication;
import com.spaceroom.entities.LogAcao;
import com.spaceroom.models.LogAcaoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LogAcaoFacade {

    private final LogAcaoApplication logAcaoApplication;

    public LogAcaoModel criar(LogAcaoModel model) {
        LogAcao logAcao = converterModelParaEntity(model);
        LogAcao logAcaoSalvo = logAcaoApplication.criar(logAcao);
        return converterEntityParaModel(logAcaoSalvo);
    }

    public List<LogAcaoModel> listarTodos() {
        return logAcaoApplication.listarTodos()
                .stream()
                .map(this::converterEntityParaModel)
                .toList();
    }

    public LogAcaoModel buscarPorId(Long idLog) {
        LogAcao logAcao = logAcaoApplication.buscarPorId(idLog);
        return converterEntityParaModel(logAcao);
    }

    public LogAcaoModel atualizar(Long idLog, LogAcaoModel model) {
        LogAcao logAcao = converterModelParaEntity(model);
        LogAcao logAcaoAtualizado = logAcaoApplication.atualizar(idLog, logAcao);
        return converterEntityParaModel(logAcaoAtualizado);
    }

    public void deletar(Long idLog) {
        logAcaoApplication.deletar(idLog);
    }

    private LogAcao converterModelParaEntity(LogAcaoModel model) {
        return LogAcao.builder()
                .idLog(model.getIdLog())
                .idUsuario(model.getIdUsuario())
                .acao(model.getAcao())
                .entidade(model.getEntidade())
                .idEntidade(model.getIdEntidade())
                .detalhes(model.getDetalhes())
                .criadoEm(model.getCriadoEm())
                .build();
    }

    private LogAcaoModel converterEntityParaModel(LogAcao logAcao) {
        LogAcaoModel model = new LogAcaoModel();
        model.setIdLog(logAcao.getIdLog());
        model.setIdUsuario(logAcao.getIdUsuario());
        model.setAcao(logAcao.getAcao());
        model.setEntidade(logAcao.getEntidade());
        model.setIdEntidade(logAcao.getIdEntidade());
        model.setDetalhes(logAcao.getDetalhes());
        model.setCriadoEm(logAcao.getCriadoEm());
        return model;
    }
}
