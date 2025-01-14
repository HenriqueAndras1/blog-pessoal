package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController // Define ao Spring que essa Classe é uma Controller
@RequestMapping("/postagens") 	 //Define qual endpoint vai ser tratado por essa Classe
@CrossOrigin(origins = "*", allowedHeaders = "*") // Libera o acesso a qualquer fronte que não seja autorizado 
public class PostagemController {

    @Autowired // O spring dá autonomia para a Interface poder invocar os memtodos
    private PostagemRepository postagemRepository;
    
    @Autowired
    private TemaRepository temaRepository;

    @GetMapping // Indica que esse método é chamado em Verbos/Metodos http do tipo get
    public ResponseEntity<List<Postagem>> getAll (){
    	return ResponseEntity.ok(postagemRepository.findAll()); // findAll nada mais é que select * from tb_postagens, trazendo as info salvas nessa tabela.
    }
     
    @GetMapping("/{id}") // Mapeia todas as requisições HTTP GET, para o endpoint
    public ResponseEntity<Postagem> getById(@PathVariable Long id) { //  O Método getById(@PathVariable Long id) será do tipo ResponseEntity porque ele responderá Requisições HTTP (HTTP Request), a variável de caminho (@PathVariable) id. Informe o id que você deseja procurar
    	return postagemRepository.findById(id)// return postagemRepository.findById(Long id): Retorna a execução do Método findById(id), que é um Método padrão da Interface JpaRepository
    			.map(resposta -> ResponseEntity.ok(resposta)) // Estamos usando classe optional(map) para tratar um valor que pode estar ausente, afim de evitar erro null(objeto nulo), caso método findById nao seja encontrado o retorno não pode ser nulo, por isso usamos "Optional(Map)" para que informe se possui dados ou nao.
    			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
    	return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); 
    	// findAllByTituloContainingIgnoreCase = "find" = select, "all" = *, "By" = where, "titulo" = Atributo da classe, "Containing" = LIKE "%titulo%", "IgnoreCase" = Ignorando letras maiúsculas ou minúsculas, "@Param("titulo")" = Define a variável String titulo como um parâmetro da consulta. Esta anotação é obrigatório em consultas do tipo Like, "String titulo" = Parâmetro do Método contendo o título que você deseja procurar.
    }
    
    @PostMapping
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
    	if (temaRepository.existsById(postagem.getTema().getId()))
    		return ResponseEntity.status(HttpStatus.CREATED)
    			.body(postagemRepository.save(postagem));
    	
    	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
    	//@RequestBody, a informação esta vindo do corpo da json, vamos escrever os campos e atualizalos, ele vai salvar e retornar com os valores salvos.
    	// @Valid = Esta anotação valida o Objeto Postagem enviado no Corpo da Requisição (Request Body), conforme as regras definidas na Model Postagem (@NotNull, @NotBlank
    	//@RequestBody Postagem postagem =  Esta anotação recebe o Objeto do tipo Postagem, que foi enviado no Corpo da Requisição (Request Body), no formato JSON e insere no parâmetro postagem do Método post.
    //return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)) = Executa o Método padrão da Interface JpaRepository save(postagem), responsável por persistir (salvar) um Objeto no Banco de dados e retorna o HTTP Status CREATED🡪201 se o Objeto foi persistido no Banco de dados.
    }
    
    @PutMapping
    public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
        if (postagemRepository.existsById(postagem.getId())) {

            if (temaRepository.existsById(postagem.getTema().getId()))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(postagemRepository.save(postagem));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
    	Optional<Postagem> postagem = postagemRepository.findById(id);
    	
    	if(postagem.isEmpty())
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	
    	postagemRepository.deleteById(id);
    	
    	// @ResponseStatus = indica que o Método delete(Long id), terá um Status HTTP específico quando a Requisição for bem sucedida, ou seja, será retornado o HTTP Status NO_CONTENT 🡪 204, ao invés do HTTP Status OK 🡪 200 como resposta padrão do Método.
    	// @PathVariable Long id: = Esta anotação insere o valor enviado no endereço do endpoint, na Variável de Caminho {id}, no parâmetro do Método delete( Long id );
    }
}