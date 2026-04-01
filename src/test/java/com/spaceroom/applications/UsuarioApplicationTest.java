package com.spaceroom.applications;

import com.spaceroom.entities.Usuario;
import com.spaceroom.exceptions.BusinessException;
import com.spaceroom.exceptions.ResourceNotFoundException;
import com.spaceroom.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioApplicationTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioApplication usuarioApplication;

    @BeforeEach
    void setUp() {
        usuarioApplication = new UsuarioApplication(usuarioRepository);
    }

    @Test
    void testCriarUsuario() {

        /* ========== Montagem do cenario ==========
        * Criar um usuário com email que NÃO existe no sistema
          deve permitir criar normalmente */
        Usuario usuario = novoUsuario(1L, "ana@spaceroom.com");
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.criar(usuario);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getEmail(), is("ana@spaceroom.com"));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCriarUsuario_EmailDuplicado() {

         /* ========== Montagem do cenario ==========
           Já existe um usuário cadastrado com esse email
           ao tentar criar outro com o mesmo email
           o sistema deve impedir a criação por duplicidade
        */

        Usuario existente = novoUsuario(1L, "ana@spaceroom.com");
        Usuario novo = novoUsuario(2L, "ana@spaceroom.com");
        when(usuarioRepository.findByEmail("ana@spaceroom.com")).thenReturn(Optional.of(existente));

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> usuarioApplication.criar(novo));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Ja existe usuario cadastrado com o email informado."));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testListarTodosUsuarios() {

        /* ========== Montagem do cenario ==========
           Existem usuários cadastrados no sistema
           ao solicitar a listagem
           o sistema deve retornar todos os usuários
        */
        when(usuarioRepository.findAll()).thenReturn(List.of(novoUsuario(1L, "u1@x.com"), novoUsuario(2L, "u2@x.com")));

        /* ========== Execucao ========== */
        List<Usuario> resultado = usuarioApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.size(), is(2));
    }

    @Test
    void testBuscarUsuarioPorId() {

        /* ========== Montagem do cenario ==========
           Existe um usuário com o ID informado
           ao buscar por esse ID
           o sistema deve retornar o usuário correspondente
        */
        Usuario usuario = novoUsuario(1L, "u1@x.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.buscarPorId(1L);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdUsuario(), is(1L));
    }

    @Test
    void testBuscarUsuarioPorId_Inexistente() {

        /* ========== Montagem do cenario ==========
           Não existe nenhum usuário com o ID informado
           ao tentar buscar esse registro
           o sistema deve lançar erro de não encontrado
        */
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> usuarioApplication.buscarPorId(99L));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Usuario nao encontrado para o id: 99"));
    }

    @Test
    void testAtualizarUsuario() {

        /* ========== Montagem do cenario ==========
           Existe um usuário cadastrado e o novo email não está em uso
           ao atualizar os dados desse usuário
           o sistema deve permitir a alteração normalmente
        */
        Usuario existente = novoUsuario(1L, "u1@x.com");
        Usuario atualizado = novoUsuario(1L, "u1novo@x.com");
        atualizado.setNome("Novo Nome");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("u1novo@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.atualizar(1L, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getNome(), is("Novo Nome"));
        assertThat(resultado.getEmail(), is("u1novo@x.com"));
    }

    @Test
    void testAtualizarUsuario_Inexistente() {

        /* ========== Montagem do cenario ==========
           Não existe usuário com o ID informado
           ao tentar atualizar esse cadastro
           o sistema deve bloquear a operação com erro
        */
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> usuarioApplication.atualizar(1L, novoUsuario(1L, "x@x.com")));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Usuario nao encontrado para o id: 1"));
    }

    @Test
    void testAtualizarUsuario_EmailDuplicadoOutroUsuario() {

         /* ========== Montagem do cenario ==========
           O email informado já pertence a outro usuário
           ao tentar atualizar usando esse email
           o sistema deve impedir a alteração por duplicidade
        */
        Usuario existente = novoUsuario(1L, "u1@x.com");
        Usuario outro = novoUsuario(2L, "u2@x.com");
        Usuario atualizado = novoUsuario(1L, "u2@x.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("u2@x.com")).thenReturn(Optional.of(outro));

        /* ========== Execucao ========== */
        BusinessException exception = assertThrows(BusinessException.class, () -> usuarioApplication.atualizar(1L, atualizado));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Ja existe usuario cadastrado com o email informado."));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testAtualizarUsuario_EmailMesmoUsuarioPermitido() {

        /* ========== Montagem do cenario ==========
           O email informado já existe mas pertence ao próprio usuário
           ao atualizar mantendo esse mesmo email
           o sistema deve permitir a operação normalmente
        */
        Usuario existente = novoUsuario(1L, "u1@x.com");
        Usuario atualizado = novoUsuario(1L, "u1@x.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("u1@x.com")).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.atualizar(1L, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getEmail(), is("u1@x.com"));
    }

    @Test
    void testDeletarUsuario() {

        /* ========== Montagem do cenario ==========
           Existe um usuário cadastrado no sistema
           ao solicitar a exclusão desse registro
           o sistema deve deletar normalmente
        */
        Usuario usuario = novoUsuario(1L, "u1@x.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        /* ========== Execucao ========== */
        usuarioApplication.deletar(1L);

        /* ========== Verificacoes ========== */
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void testDeletarUsuario_Inexistente() {

        /* ========== Montagem do cenario ==========
           Não existe usuário com o ID informado
           ao tentar deletar esse registro
           o sistema deve lançar erro e não executar o delete
        */
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        assertThrows(ResourceNotFoundException.class, () -> usuarioApplication.deletar(1L));

        /* ========== Verificacoes ========== */
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void testCriarUsuario_ConsultaEmailAntesSalvar() {

        /* ========== Montagem do cenario ==========
           Um novo usuário está sendo criado com email válido
           antes de salvar esse cadastro
           o sistema deve consultar se o email já existe
        */
        Usuario usuario = novoUsuario(1L, "consulta@x.com");
        when(usuarioRepository.findByEmail("consulta@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        /* ========== Execucao ========== */
        usuarioApplication.criar(usuario);

        /* ========== Verificacoes ========== */
        verify(usuarioRepository).findByEmail(eq("consulta@x.com"));
    }

    @Test
    void testAtualizarUsuario_ConsultaEmailAntesSalvar() {

         /* ========== Montagem do cenario ==========
           Um usuário será atualizado com um novo email
           antes de salvar essa alteração
           o sistema deve verificar se esse email já existe
        */
        Usuario existente = novoUsuario(1L, "a@x.com");
        Usuario atualizado = novoUsuario(1L, "b@x.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("b@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        usuarioApplication.atualizar(1L, atualizado);

        /* ========== Verificacoes ========== */
        verify(usuarioRepository).findByEmail(eq("b@x.com"));
    }

    @Test
    void testAtualizarUsuario_AtualizaCampos() {

        /* ========== Montagem do cenario ==========
           Um usuário existente terá campos específicos alterados
           ao atualizar ativo e primeiroAcesso
           o sistema deve persistir os novos valores corretamente
        */
        Usuario existente = novoUsuario(1L, "a@x.com");
        Usuario atualizado = novoUsuario(1L, "b@x.com");
        atualizado.setAtivo(false);
        atualizado.setPrimeiroAcesso(false);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("b@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        usuarioApplication.atualizar(1L, atualizado);

        /* ========== Verificacoes ========== */
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getAtivo(), is(false));
        assertThat(captor.getValue().getPrimeiroAcesso(), is(false));
    }

    @Test
    void testCriarUsuario_RetornaUsuarioSalvo() {

        /* ========== Montagem do cenario ==========
           A criação do usuário é válida e o save será executado
           ao concluir essa operação
           o sistema deve retornar o usuário salvo com os dados corretos
        */
        Usuario usuario = novoUsuario(1L, "r@x.com");
        when(usuarioRepository.findByEmail("r@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.criar(usuario);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getIdUsuario(), is(1L));
    }

    @Test
    void testDeletarUsuario_ChamaDeleteUmaVez() {

        /* ========== Montagem do cenario ==========
           Existe um usuário válido para exclusão
           ao executar o delete desse registro
           o método delete deve ser chamado exatamente uma vez
        */
        Usuario usuario = novoUsuario(1L, "d@x.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        /* ========== Execucao ========== */
        usuarioApplication.deletar(1L);

        /* ========== Verificacoes ========== */
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void testBuscarUsuarioPorId_MensagemComId() {

        /* ========== Montagem do cenario ==========
           Não existe nenhum usuário com o ID informado
           ao tentar buscar esse registro específico
           a mensagem de erro deve exibir o ID corretamente
        */
        when(usuarioRepository.findById(123L)).thenReturn(Optional.empty());

        /* ========== Execucao ========== */
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> usuarioApplication.buscarPorId(123L));

        /* ========== Verificacoes ========== */
        assertThat(exception.getMessage(), is("Usuario nao encontrado para o id: 123"));
    }

    @Test
    void testCriarUsuario_RepositorioRetornaMesmoObjeto() {

        /* ========== Montagem do cenario ==========
           O repositório retorna o mesmo objeto recebido no save
           ao finalizar a criação do usuário
           o sistema deve devolver esse mesmo objeto sem alterações
        */
        Usuario usuario = novoUsuario(8L, "mesmo@x.com");
        when(usuarioRepository.findByEmail("mesmo@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.criar(usuario);

        /* ========== Verificacoes ========== */
        assertThat(resultado, is(usuario));
    }

    @Test
    void testAtualizarUsuario_RepositorioRetornaAlterado() {

         /* ========== Montagem do cenario ==========
           Um usuário existente será atualizado com novo email válido
           ao concluir essa atualização
           o sistema deve retornar o usuário com os dados alterados
        */
        Usuario existente = novoUsuario(1L, "a@x.com");
        Usuario atualizado = novoUsuario(1L, "novo@x.com");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmail("novo@x.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ========== Execucao ========== */
        Usuario resultado = usuarioApplication.atualizar(1L, atualizado);

        /* ========== Verificacoes ========== */
        assertThat(resultado.getEmail(), is("novo@x.com"));
    }

    @Test
    void testListarTodosUsuarios_ListaVazia() {

        /* ========== Montagem do cenario ==========
           Não existe nenhum usuário cadastrado no sistema
           ao solicitar a listagem
           o sistema deve retornar uma lista vazia
        */
        when(usuarioRepository.findAll()).thenReturn(List.of());

        /* ========== Execucao ========== */
        List<Usuario> resultado = usuarioApplication.listarTodos();

        /* ========== Verificacoes ========== */
        assertThat(resultado.isEmpty(), is(true));
    }

    private Usuario novoUsuario(Long id, String email) {
        return Usuario.builder()
                .idUsuario(id)
                .idInstituicao(1L)
                .idCargo(1)
                .nome("Usuario Teste")
                .email(email)
                .senhaHash("hash")
                .primeiroAcesso(true)
                .tokenDefinicaoSenha("token")
                .tokenExpiracao(LocalDateTime.of(2026, 3, 25, 10, 0))
                .ultimoLoginEm(LocalDateTime.of(2026, 3, 25, 8, 0))
                .ativo(true)
                .build();
    }
}
