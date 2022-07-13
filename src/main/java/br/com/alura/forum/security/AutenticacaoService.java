package br.com.alura.forum.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/*
 * Classe responsável pelo serviço de implementação
 * dos detalhes de acesso do usuário.
 */
@Service
public class AutenticacaoService implements UserDetailsService{

	@Autowired
	private UsuarioRepository repository;
	
	/*
	 * No Spring, a busca de usuário no banco de dados
	 * é feita usando somente o username.
	 * O password ele faz a validação em memória, trazendo
	 * esses dados para a memória no momento da busca, por isso
	 * ele recebe apenas o username como parâmetro.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if(usuario.isPresent()) {
			return usuario.get();
		} else {
			throw new UsernameNotFoundException("Dados inválidos. Usuário não encontrado no banco de dados.");
		}
	}

}
