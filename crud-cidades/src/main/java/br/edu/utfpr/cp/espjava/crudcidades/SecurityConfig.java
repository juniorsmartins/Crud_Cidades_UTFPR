package br.edu.utfpr.cp.espjava.crudcidades;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication().withUser("joo")
									.password(cifrador().encode("123"))
									.authorities("listar")
									.and()
									.withUser("ana")
									.password(cifrador().encode("321"))
									.authorities("admin");
	}
	
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
					.authorizeRequests()
					.antMatchers("/").hasAnyAuthority("listar", "admin")
					.antMatchers("/criar").hasAuthority("admin")
					.antMatchers("/excluir").hasAuthority("admin")
					.antMatchers("/preparaAlterar").hasAuthority("admin")
					.antMatchers("/alterar").hasAuthority("admin")
					.anyRequest().denyAll()
					.and()
					.formLogin()
					.loginPage("/login.html").permitAll()
					.and()
					.logout().permitAll();
	}
	
	@Bean
	public PasswordEncoder cifrador()
	{
		return new BCryptPasswordEncoder();
	}
}
