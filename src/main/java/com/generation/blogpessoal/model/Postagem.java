package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity // indicar que a classe é uma tabela
@Table(name = "tb_postagens") // indica o nome da tabela no banco de dados
public class Postagem {

	
	@Id // Ele é a chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
	private Long id; // long é igual o int, sendo que int so guarda 8 caracteres e long bem mais
	
	@NotBlank(message = "Esse campo é obrigatório!") // Parecida com regra not null, porem NotBlank não aceita espaços em branco ou seja tem que ter algum texto.
	@Size(min = 5, max = 100, message  = "Digite no minimo 5 e no maximo 100 caracteres")
	private String titulo;
	
	@NotBlank(message = "Esse campo é obrigatório!") // Parecida com regra not null, porem NotBlank não aceita espaços em branco ou seja tem que ter algum texto.
	@Size(min = 10, max = 1000, message  = "Digite no minimo 10 e no maximo 1000 caracteres")
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	@ManyToOne // Estamos criando relacionamento entra as tabelas Postagem e tema, estamos indicando que a classe Postagem é o lado N:1 Muitos para 1
	@JsonIgnoreProperties("postagem") //@JsonIgnoreProperties, funciona como uma excepetion em que não deixa o nosso código entrar em loop por causa da relação entre as tabelas.
	private Tema tema;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}
