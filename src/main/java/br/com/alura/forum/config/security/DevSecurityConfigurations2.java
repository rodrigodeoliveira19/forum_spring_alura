package br.com.alura.forum.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Profile("dev")// Ambiente de desenvolvimento,  libebera todas as solictações
public class DevSecurityConfigurations2 extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Liberando acesso 
		http.authorizeRequests()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/**").permitAll()
		.and().csrf().disable(); 
	}
	
	
}
