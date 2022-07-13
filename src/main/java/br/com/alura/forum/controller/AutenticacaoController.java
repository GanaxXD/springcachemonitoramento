package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.modelo.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	//Método responsável pelo tratamento de autenticação do usuário
	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid LoginForm login){
		//Iniciando a lógica de autenticação
		
		return ResponseEntity.ok().build();
	}
}
