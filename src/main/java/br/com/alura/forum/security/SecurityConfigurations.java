package br.com.alura.forum.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 * A classe WebSecurityConfigureAdapter possui um método
 * de configuração de segurança com 3 sobrecargas:
 * 
 * 1 - parâmetro AuthenticationManagerBuilder auth:
 * 		> configura a autenticação do usuário. É onde
 * 		> configuramos o login, usuário, senha de acesso, etc.
 * 2 - parâmetro:HttpSecurity http:
 * 		> onde configuramos a autorização aos endpoints.
 * 		> É aqui onde falamos quais endpoints estaram acessíveis a 
 * 		> qualquer pessoa, ou só a quem estiver logado na aplicação.
 * 3 - parâmetro:WebSecurity web:
 * 		> onde configuramos o acesso à recursos estáticos,
 * 		> como, por exemplo, o html, css e js da aplicação, 
 * 		> imagens e etc. Como a api não tem view, não é necessário
 * 		> realizar essa configuração, mas caso o projeto tenha
 * 		> view, é necessário configurar, ou os acessos aos recursos 
 * 		> da página como o html e o css não ficarão disponíveis a quem não estiver logado.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{

	//Configura autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
	}
	
	//Configura autorizações aos endpoints
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Neste exemplo, serão aceitos qualquer requisição
		//get, independente de estarem logados, bem como qualquer
		//requisição topicos/qualquer-coisa.
		//O spring já tem uma tela de login dele mesmo, por isso
		//chamei a mesma usando o método formLogin()
		http.authorizeRequests().
			antMatchers(HttpMethod.GET, "/topicos").permitAll().
			antMatchers(HttpMethod.GET, "topicos/*").permitAll().
			anyRequest().authenticated().
			and().formLogin();
	}
	
	//Configura acesso aos arquivos estáticos (html, css, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
		
	}
}
