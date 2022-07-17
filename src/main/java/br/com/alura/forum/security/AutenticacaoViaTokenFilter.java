package br.com.alura.forum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/*
 * Classe responsável por pegar o header das requisições feitas pelo cliente
 * e filtrar, pegando apenas a parte do tipo de autenticaçao usado (Bearer) e 
 * o token, para validar se o usuário tem ou não permissão
 * para tal endpoint.
 * 
 * TODA REQUISIÇÃO PRECISA SER AUTENTICADA, por isso, essa classe
 * Herda de OncePerRequestFilter
 * 
 * FILTROS NÃO ACEITAM INJEÇÃO DE DEPENDENCIA, POR ISSO OS PARÂMETROS ESTÃO SENDO
 * PASSADOS VIA CONSTRUTOR
 */
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

	//Como essa classe é um filtro, eu não consigo fazer injeção de dependências nela. Logo, preciso receber tokenService do construtor
	private TokenService tokenService;
	private UsuarioRepository repository;
	
	public AutenticacaoViaTokenFilter(TokenService ts, UsuarioRepository r) {
		this.tokenService = ts;
		this.repository = r;
	}
	
	//Método que verifica se a requisição foi feita por alguém autorizado
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//1 - Recupera o token enviado pelo usuário:
		String token = recuperarToken(request);
		//2 - Valido o token
		boolean tokenValido = tokenService.isTokenValido(token);
		//3 - Autentico o cliente 
		if(tokenValido) {
			autenticarCliente(token);
		}
		//FINAL - crio o filtro passando a requisição e a resposta
		filterChain.doFilter(request, response);
	}

	/*
	 * Método responsável por autenticar o cliente, SEM a necessidade de passar
	 * a senha do usuário, já que o token foi validado anteriormente, 
	 * e o token já contém a senha do usuário.
	 * 
	 * Essa autenticação ocorrerá EM TOA REQUISIÇÃO (STATELESS)
	 */
	private void autenticarCliente(String token) {
		//1 - Recupero o id do usuário para posteriormente pegar o usuário do banco de dados atravez do token (o token tem o subject, que é o dono do token, ou seja, o id do cliente logado)
		Long idUsuario = tokenService.getIdusuario(token);
		Usuario usuario = repository.findById(idUsuario).get();
		//2 - Crio um objeto do tipo usernamePasswordAuthentication para passar junto do usuário para o método setAuthentication(dados-do-usuario, senha-de-acesso-do-usuário, perfil-de-acessos-do-usuário)
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null,usuario.getAuthorities()); //a senha não precisa ser passada porque o token já foi autenticado
		//3 - Libero a autenticação indicando QUEM está autenticado
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || token.isBlank() || !token.startsWith("Bearer ")) {			
			return null;
		}
		return token.substring(7, token.length()); //devolve só o token, sem o tipo de autenticação
	}

}
