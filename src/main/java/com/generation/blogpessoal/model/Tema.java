package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_temas")
public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O Atributo Descrição é obrigatório")
	private String descricao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // Estamos criando o relacionamento entre as classes Tema e Postagem, sendo o lado Tema 1:n com muitos objetos e Postagem se relacionando com 1 objeto
	@JsonIgnoreProperties("tema")
	private List<Postagem> postagem;
	// Fetch, define estragegia de busca e carregamento dos dados das entidades durante a busca, podemos classificar com os tipos: "Eager" ansiosa e "Lazy" Preguiçosa
	// FetchType.LAZY, Por padrao vem configurado Lazy, diferença de Lazy e Eager, lazy usamos para pequenas aplicações como nesse caso nosso banco de dados é pequeno, ele tras a informações somente do que foi requisitado. Já o Eager antes de pedirmos informaçoes ele ja salva em memoria e nos "poupa" tempo de
	// mappedBy, Usado quando você tem duas entidades ou seja relação bidirecional, Indicando que "tema" é o lado proprietário Na classe Tema, é a responsável por gerenciar o relacionamento no banco, criar a chave estrangeira e relacionamento entre elas.
	// cascade, Relacionamentos de entidade geralmente dependem da existência de outra entidade, Sem o tema a entidade postagens não tem significado proprio.
	// CascadeType.REMOVE, Quando um Objeto da Classe Tema for apagado, todos os Objetos da Classe Postagem associados ao Tema apagado, também serão apagados. O Inverso não é verdadeiro.
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

	
}