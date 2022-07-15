package br.com.alura.forum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/*
 * Classe responsável por pegar o header das requisições feitas pelo cliente
 * e filtrar, pegando apenas a parte do tipo de autenticaçao usado (Bearer) e 
 * o token, para validar se o usuário tem ou não permissão
 * para tal endpoint.
 * 
 * TODA REQUISIÇÃO PRECISA SER AUTENTICADA, por isso, essa classe
 * Herda de OncePerRequestFilter
 */
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

	//Método que verifica se a requisição foi feita por alguém autorizado
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//1 - Recupera o token enviado pelo usuário:
		String token = recuperarToken(request);
		//2 - Valido o token
		
		filterChain.doFilter(request, response);
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || token.isBlank() || !token.startsWith("Bearer ")) {			
			return null;
		}
		return token.substring(7, token.length()); //devolve s´o toke, sem o tipo de autenticação
	}

}
