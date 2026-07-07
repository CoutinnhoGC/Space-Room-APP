package com.spaceroom.applications;

import com.spaceroom.entities.LogAcao;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.LogAcaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogAcaoApplication {

    private final LogAcaoRepository logAcaoRepository;

    public LogAcao criar(LogAcao logAcao) {
        return logAcaoRepository.save(logAcao);
    }

    public List<LogAcao> listarTodos() {
        return logAcaoRepository.findAll();
    }

    public LogAcao buscarPorId(Long idLog) {
        return logAcaoRepository.findById(idLog)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Log de ação não encontrado para o id: " + idLog
                ));
    }

    public LogAcao atualizar(Long idLog, LogAcao dadosAtualizados) {
        LogAcao logAcaoExistente = buscarPorId(idLog);

        logAcaoExistente.setIdUsuario(dadosAtualizados.getIdUsuario());
        logAcaoExistente.setAcao(dadosAtualizados.getAcao());
        logAcaoExistente.setEntidade(dadosAtualizados.getEntidade());
        logAcaoExistente.setIdEntidade(dadosAtualizados.getIdEntidade());
        logAcaoExistente.setDetalhes(dadosAtualizados.getDetalhes());

        return logAcaoRepository.save(logAcaoExistente);
    }

    public void deletar(Long idLog) {
        LogAcao logAcao = buscarPorId(idLog);
        logAcaoRepository.delete(logAcao);
    }
}
