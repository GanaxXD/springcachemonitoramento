package br.com.alura.forum.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/*
 * Esta classe representará o perfil de acesso que um usuário pode ter.
 * É uma entidade, onde um usuário
 * poderá ter vários perfís, e um perfil pode
 * ser de vários usuários (MxN).
 * 
 * É preciso informar ao Spring que essa é a classe que representa
 * os perfis de acesso, por isso, é necessário implementar
 * a interface GrantedAuthority
 */
@Entity
public class Perfil implements GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	//indica ao Spring qual o perfil de acesso do usuário
	@Override
	public String getAuthority() {
		return this.nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
}
