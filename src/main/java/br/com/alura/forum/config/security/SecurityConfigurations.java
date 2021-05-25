package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

@EnableWebSecurity
@Configuration
@Profile(value={"prod", "test"})
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{

	@Autowired
	private AutenticacaoService autenticacaoService; 
	
	@Autowired
	private TokenService tokenService; 
	@Autowired
	private UsuarioRepository usuarioRepository; 
	
	
	//Configura ações de autenticação. 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 /*Diz ao spring qual a classe que contém a lógica para 
		  * autenticação no sistema*/ 
		// BCryptPasswordEncoder -  Classe do spring
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configurações de Autorização.  Acesso a URL e etc
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Liberando acesso 
		http.authorizeRequests()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers(HttpMethod.GET, "/topicos").permitAll()
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
		.antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR")
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
		.anyRequest().authenticated() //Qualquer outra requisição tem que se autenticar. 
		//.and().formLogin(); // Formulário de autenticação do spring.
		
		.and().csrf().disable() 
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Não guarde Sessão.
		.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService,usuarioRepository), UsernamePasswordAuthenticationFilter.class); //UsernamePasswordAuthenticationFilter - Classe padrão do spring
	}
	
	//Configuração de recursos estáticos (js,css, imagens)
	@Override
	public void configure(WebSecurity web) throws Exception {
		//Libera acesso a documentação criada pelo swagger
		web.ignoring().antMatchers("/**.html","/v2/api-docs","/webjars/**","/configuration/**","/swagger-resources/**"); 
	}
	
	
	//Método sobrescrito para realizar authenticação
	@Override
	@Bean // Adiona o AuthenticationManager no controller.
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	/*Teste para criptograr uma senha, adicionar ao usuário e fazer login na aplicação
	 * public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}*/
	
	
}
