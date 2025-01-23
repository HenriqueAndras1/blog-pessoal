package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

// Define que esta classe é um teste de Spring Boot, com ambiente aleatório para evitar conflitos de portas
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Define que os testes compartilham uma única instância da classe
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate; // Ferramenta para simular requisições HTTP

    @Autowired
    private UsuarioService usuarioService; // Serviço para gerenciar operações com usuários

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositório para gerenciar dados no banco

    // Configuração inicial que será executada antes de todos os testes
    @BeforeAll
    void start() {
        usuarioRepository.deleteAll(); // Limpa todos os dados do repositório

        // Cria um usuário administrativo (root)
        usuarioService.cadastrarUsuario(new Usuario(0L,
            "Root", "root@root.com", "rootroot", " "));
    }
    
    @Test
    @DisplayName("Cadastrar Um Usuário") // Exibe um nome descritivo para o teste, simulando o insomnia.
    public void deveCriarUmUsuario() {
        // Criação do corpo da requisição HTTP com os dados do usuário
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
            "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "-"));

        // Envia uma requisição POST para cadastrar o usuário
        ResponseEntity<Usuario> corpoResposta = testRestTemplate
            .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        // Verifica se a resposta possui o status HTTP 201 (CREATED)
        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Não deve permitir duplicação do Usuário") // Teste para verificar duplicação de usuários
    public void naoDeveDuplicarUsuario() {
        // Cadastra um usuário
        usuarioService.cadastrarUsuario(new Usuario(0L,
            "Maria da Silva", "maria_silva@email.com.br", "13465278", "-"));

        // Criação do corpo da requisição com dados duplicados
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
            "Maria da Silva", "maria_silva@email.com.br", "13465278", "-"));

        // Envia uma requisição POST para tentar cadastrar o usuário novamente
        ResponseEntity<Usuario> corpoResposta = testRestTemplate
            .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        // Verifica se a resposta possui o status HTTP 400 (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar um Usuário") // Teste para atualizar os dados de um usuário
    public void deveAtualizarUmUsuario() {
        // Cadastra um novo usuário
        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
            "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "-"));

        // Cria um objeto com os dados atualizados do usuário
        Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
            "Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123", "-");

        // Criação do corpo da requisição HTTP para atualização
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

        // Envia uma requisição PUT para atualizar o usuário
        ResponseEntity<Usuario> corpoResposta = testRestTemplate
            .withBasicAuth("root@root.com", "rootroot") // Autenticação básica
            .exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        // Verifica se a resposta possui o status HTTP 200 (OK)
        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Listar todos os Usuários") // Teste para listar todos os usuários
    public void deveMostrarTodosUsuarios() {
        // Cadastra dois usuários
        usuarioService.cadastrarUsuario(new Usuario(0L, 
            "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "-"));

        usuarioService.cadastrarUsuario(new Usuario(0L, 
            "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "-"));

        // Envia uma requisição GET para listar todos os usuários
        ResponseEntity<String> resposta = testRestTemplate
            .withBasicAuth("root@root.com", "rootroot") // Autenticação básica
            .exchange("/usuarios/all", HttpMethod.GET, null, String.class);

        // Verifica se a resposta possui o status HTTP 200 (OK)
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

}
