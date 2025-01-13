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

import jakarta.validation.Valid;

@RestController // Define ao Spring que essa Classe é uma Controller
@RequestMapping("/postagens") 	 //Define qual endpoint vai ser tratado por essa Classe
@CrossOrigin(origins = "*", allowedHeaders = "*") // Libera o acesso a qualquer fronte que não seja autorizado 
public class PostagemController {

    @Autowired // O spring dá autonomia para a Interface poder invocar os memtodos
    private PostagemRepository postagemRepository;

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
    }
    
    @PostMapping
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
    	return ResponseEntity.status(HttpStatus.CREATED)
    			.body(postagemRepository.save(postagem));
    }
    
    @PutMapping
    public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
    	return postagemRepository.findById(postagem.getId())
    			.map(resposta -> ResponseEntity.status(HttpStatus.OK)
    					.body(postagemRepository.save(postagem)))
    			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
    	Optional<Postagem> postagem = postagemRepository.findById(id);
    	
    	if(postagem.isEmpty())
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	
    	postagemRepository.deleteById(id);
    }
}
