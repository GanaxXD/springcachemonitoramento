package br.com.alura.forum.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/*
 * Preciso indicar ao Spring qual é a classe que representa
 * o usuário cliente da aplicação. Para isso, preciso
 * implementar a interface UserDetails
 */
@Entity
public class Usuario implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String senha;
	
	//Lista de perfis de acesso do usuário
	private List<Perfil> perfil = new ArrayList<Perfil>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	//Informe a lista de perfis de acesso que o usuário terá
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfil;
	}

	//Informa o password para o Spring
	@Override
	public String getPassword() {
		return this.senha;
	}

	//Informa o usrname para o Spring
	@Override
	public String getUsername() {
		return this.email;
	}

	//Informa se a conta do usuário não expira para o Spring
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//Informa se a conta do usuário está bloqueada para o Spring
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//Informa se as credenciais do usuário não expira para o Spring
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//Informa se a conta do usuário está ativa para o Spring
	@Override
	public boolean isEnabled() {
		return true;
	}

}
