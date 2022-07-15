package br.com.alura.forum.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * Classe responsável por criar o token
 */
@Service
public class TokenService {
	
	//Injetando dados do application.properties na classe
	@Value("${forum.jwt.expiration}")
	private String expiration;

	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime()+Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("Nome do projeto que gerou o token") //onde o token foi gerado?
				.setSubject(logado.getId().toString()) //qual a identidade do dono? (pode ser qualquer dado do usuário, desde que seja String)
				.setIssuedAt(hoje) //quando foi criado? PRECISA SER DA CLASSE DATE
				.setExpiration(dataExpiracao) //quando expira o token?
				.signWith(SignatureAlgorithm.HS256, secret) //qual algoritmo de criptografia eu vou usar? E qual a chave eu devo usar para quebrar a criptografia nas autenticações?
				.compact();//gere o token!
	}
}
