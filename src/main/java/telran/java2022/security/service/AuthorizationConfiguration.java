package telran.java2022.security.service;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http.httpBasic();
		http.csrf().disable();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeRequests()
		.antMatchers("/account/register/**").permitAll()
		.antMatchers("/forum/posts/**").permitAll();
		
		http.authorizeRequests()
		.regexMatchers(HttpMethod.DELETE, "/account/user/\\w+/role/\\w+/?").access("hasRole('ADMINISTRATOR')")
		.regexMatchers(HttpMethod.PUT, "/account/user/\\w+/role/\\w+/?").hasRole("ADMINISTRATOR")
		.regexMatchers(HttpMethod.DELETE, "/forum/post/\\w+/?").hasAnyRole("MODERATAOR", "ADMINISTRATOR");
		
		http.authorizeRequests().anyRequest().authenticated();
		
	} 
	
}
