package br.com.alura.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

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
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository repository;
	
	/*
	 * Na classe "AutenticacaoController", para criar a lógica
	 * da autenticação, eu preciso injetar um objeto do tipo
	 * AuthenticationManager na classe referida.
	 * Contudo, o Spring pro algum motivo não injeta a dependencia automaticamente.
	 * Mas a classe WebSecurityConfigureAdapter tem um método que gera 
	 * um AuthenticationManager, por isso, vamos aproveitar que essa classe
	 * herda da WebSecurityConfigurerAdapter e vamos sobreescrever
	 * o método, anotando-o com @Bean, para que o Spring saiba
	 * que ele ficará disponível para o servidor da aplicação no início do 
	 * em que o projeto começar a rodar.
	 */
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//Configura autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configura autorizações aos endpoints
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		//Neste exemplo, serão aceitos qualquer requisição
		//get, independente de estarem logados, bem como qualquer
		//requisição topicos/qualquer-coisa.
		//O spring já tem uma tela de login dele mesmo, por isso
		//chamei a mesma usando o método formLogin()
		//
		//
		//Após o comentário 'and().formLogin()' se inicia a 
		//fase statless das requisições da api
		http.authorizeRequests().
			antMatchers(HttpMethod.GET, "/topicos").permitAll(). //permite qualquer get em /tópicos
			antMatchers(HttpMethod.GET, "/topicos/*").permitAll(). //permite qualquer get em /tópiccos passando qualquer coisa na url, mesmo sem autenticação
			antMatchers(HttpMethod.GET, "/actuator").permitAll(). //PERMITE O MONITORAMENTO. EM TESTES, DEIXAR O permitAll(), MAS JAMAIS, JAMAIS EM PRODUÇÃO, POIS DADOS SENSÍVEIS SÃO MOSTRADOS
			anyRequest().authenticated(). //qualquer outra requisição precisa ser autenticada
			//and().formLogin(); //exibindo form de login padrão do spring
			and().csrf().disable(). //Desabilitando o csrf, uma vez que o servidor é stateless
			sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Sessão stateless
			.and()
			.addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, repository), UsernamePasswordAuthenticationFilter.class); //Passando o filtro que eu criei para ser executado depois do filtro padrão do spring
	}
	
	//Configura acesso aos arquivos estáticos (html, css, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
		
	}
}
