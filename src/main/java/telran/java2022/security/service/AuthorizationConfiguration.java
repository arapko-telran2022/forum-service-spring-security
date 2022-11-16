package telran.java2022.security.service;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;
import telran.java2022.post.dao.PostRepository;
import telran.java2022.post.model.Post;

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
//		-----------------------------------------------------------------------------------------------------------		
		.antMatchers(HttpMethod.DELETE, "/account/user/{user}/role/{role}/**")
			.access("hasRole('ADMINISTRATOR')")
		.antMatchers(HttpMethod.PUT, "/account/user/{user}/role/{role}/**")
			.access("hasRole('ADMINISTRATOR')")	
		.antMatchers(HttpMethod.DELETE, "/account/user/{login}/**")
			.access("hasRole('ADMINISTRATOR') or @ownerChecker.checkUserOwner(authentication,#login)")	
		.antMatchers(HttpMethod.PUT, "/account/user/{login}/**")
			.access("@ownerChecker.checkUserOwner(authentication,#login)")
		.antMatchers(HttpMethod.PUT, "/account/password/**")
			.access("@ownerChecker.checkUserOwner(authentication,#login)")			
//		-----------------------------------------------------------------------------------------------------------
		.antMatchers(HttpMethod.POST, "/forum/post/{author}/**")
			.access("@ownerChecker.checkUserOwner(authentication,#author)")			
		.antMatchers(HttpMethod.DELETE, "/forum/post/{id}/**")
			.access("hasRole('MODERATAOR') or @ownerChecker.checkPostOwner(authentication,#id)")
		.antMatchers(HttpMethod.PUT, "/forum/post/{id}/**")
			.access("hasRole('MODERATAOR') or @ownerChecker.checkPostOwner(authentication,#id)")
		.antMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}/**")
			.access("@ownerChecker.checkUserOwner(authentication,#author)");
//		-----------------------------------------------------------------------------------------------------------		
		http.authorizeRequests().anyRequest().authenticated();
		
	} 
	
	
}

