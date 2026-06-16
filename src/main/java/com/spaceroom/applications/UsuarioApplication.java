package com.spaceroom.applications;

import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.models.UsuarioModel;
import com.spaceroom.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioApplication {

    private final UsuarioRepository usuarioRepository;
    private final AutorizacaoApplication autorizacaoApplication;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioApplication(UsuarioRepository usuarioRepository,
                              AutorizacaoApplication autorizacaoApplication,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.autorizacaoApplication = autorizacaoApplication;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioApplication(UsuarioRepository usuarioRepository) {
        this(usuarioRepository, null, new BCryptPasswordEncoder());
    }

    public Usuario criar(Usuario usuario) {
        return criar(usuario, null);
    }

    public Usuario criar(Usuario usuario, Boolean podeReservar) {
        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarAcessoInstituicao(usuario.getIdInstituicao());
        }
        String emailNormalizado = normalizarEmail(usuario.getEmail());
        usuario.setEmail(emailNormalizado);
        validarEmailDuplicado(emailNormalizado, null);
        usuario.setSenhaHash(prepararSenha(usuario.getSenhaHash(), null, true));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        sincronizarPermissaoReserva(usuarioSalvo, podeReservar);
        return usuarioRepository.findById(usuarioSalvo.getIdUsuario()).orElse(usuarioSalvo);
    }

    public List<Usuario> listarTodos() {
        if (autorizacaoApplication == null) {
            return usuarioRepository.findAll();
        }
        Usuario usuarioAtual = obterUsuarioAtual();
        if (autorizacaoApplication.isAdminPlataforma(usuarioAtual)) {
            return usuarioRepository.findAll();
        }
        return usuarioRepository.findByIdInstituicao(usuarioAtual.getIdInstituicao());
    }

    public Usuario buscarPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado para o id: " + idUsuario));
        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarAcessoUsuario(usuario);
        }
        return usuario;
    }

    public Usuario atualizar(Long idUsuario, Usuario dadosAtualizados) {
        return atualizar(idUsuario, dadosAtualizados, null);
    }

    public Usuario atualizar(Long idUsuario, Usuario dadosAtualizados, Boolean podeReservar) {
        Usuario usuarioExistente = buscarPorId(idUsuario);
        String emailNormalizado = normalizarEmail(dadosAtualizados.getEmail());

        validarEmailDuplicado(emailNormalizado, idUsuario);
        if (autorizacaoApplication != null) {
            autorizacaoApplication.validarAcessoInstituicao(dadosAtualizados.getIdInstituicao());
        }

        usuarioExistente.setIdInstituicao(dadosAtualizados.getIdInstituicao());
        usuarioExistente.setIdCargo(dadosAtualizados.getIdCargo());
        usuarioExistente.setNome(dadosAtualizados.getNome());
        usuarioExistente.setEmail(emailNormalizado);
        usuarioExistente.setSenhaHash(prepararSenha(dadosAtualizados.getSenhaHash(), usuarioExistente.getSenhaHash(), false));
        usuarioExistente.setPrimeiroAcesso(dadosAtualizados.getPrimeiroAcesso());
        usuarioExistente.setTokenDefinicaoSenha(dadosAtualizados.getTokenDefinicaoSenha());
        usuarioExistente.setTokenExpiracao(dadosAtualizados.getTokenExpiracao());
        usuarioExistente.setUltimoLoginEm(dadosAtualizados.getUltimoLoginEm());
        usuarioExistente.setAtivo(dadosAtualizados.getAtivo());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        sincronizarPermissaoReserva(usuarioAtualizado, podeReservar);
        return usuarioRepository.findById(usuarioAtualizado.getIdUsuario()).orElse(usuarioAtualizado);
    }

    public void deletar(Long idUsuario) {
        Usuario usuario = buscarPorId(idUsuario);
        usuarioRepository.delete(usuario);
    }

    public Usuario obterUsuarioAtual() {
        if (autorizacaoApplication == null) {
            throw new BusinessException("Contexto autenticado indisponivel.");
        }
        return autorizacaoApplication.obterUsuarioAtualObrigatorio();
    }

    public UsuarioModel toModel(Usuario usuario) {
        UsuarioModel model = new UsuarioModel();
        model.setIdUsuario(usuario.getIdUsuario());
        model.setIdInstituicao(usuario.getIdInstituicao());
        model.setIdCargo(usuario.getIdCargo());
        model.setNome(usuario.getNome());
        model.setEmail(usuario.getEmail());
        model.setPrimeiroAcesso(usuario.getPrimeiroAcesso());
        model.setUltimoLoginEm(usuario.getUltimoLoginEm());
        model.setAtivo(usuario.getAtivo());
        model.setPodeReservar(autorizacaoApplication != null ? autorizacaoApplication.resolverPodeReservar(usuario, null) : null);
        model.setAdminPlataforma(autorizacaoApplication != null && autorizacaoApplication.resolverAdminPlataforma(usuario));
        model.setCriadoEm(usuario.getCriadoEm());
        model.setAtualizadoEm(usuario.getAtualizadoEm());
        return model;
    }

    private void validarEmailDuplicado(String email, Long idUsuarioAtual) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            boolean emailPertenceOutroUsuario = idUsuarioAtual == null || !usuario.getIdUsuario().equals(idUsuarioAtual);
            if (emailPertenceOutroUsuario) {
                throw new BusinessException("Ja existe usuario cadastrado com o email informado.");
            }
        });
    }

    private void sincronizarPermissaoReserva(Usuario usuario, Boolean podeReservar) {
        if (autorizacaoApplication != null) {
            autorizacaoApplication.sincronizarPermissaoReserva(usuario, podeReservar);
        }
    }

    private String normalizarEmail(String email) {
        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String prepararSenha(String senhaRecebida, String senhaAtual, boolean obrigatoria) {
        if (senhaRecebida == null || senhaRecebida.isBlank()) {
            if (obrigatoria) {
                throw new BusinessException("A senha e obrigatoria.");
            }
            return senhaAtual;
        }

        if (autorizacaoApplication != null && senhaRecebida.length() < 8) {
            throw new BusinessException("A senha deve conter ao menos 8 caracteres.");
        }

        if (senhaRecebida.matches("^\\$2[aby]\\$.{56}$")) {
            return senhaRecebida;
        }

        return passwordEncoder.encode(senhaRecebida);
    }
}
