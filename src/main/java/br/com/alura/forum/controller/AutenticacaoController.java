package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.modelo.LoginForm;
import br.com.alura.forum.security.TokenService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	//Necessário para criar a lógica de autenticação
	@Autowired
	private AuthenticationManager auth;
	
	@Autowired
	private TokenService tokenService;
	
	//Método responsável pelo tratamento de autenticação do usuário
	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid LoginForm form){
		//Iniciando a lógica de autenticação
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		try {
			Authentication authentication = auth.authenticate(dadosLogin);
			//Token usando a lib jjwt
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer")); //passando o token e o tipo de autenticação exigido pelo protocolo HTTP
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
