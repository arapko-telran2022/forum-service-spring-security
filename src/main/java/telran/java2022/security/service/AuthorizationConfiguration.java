package telran.java2022.security.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class AuthorizationConfiguration {
	
	final ExpiredPasswordFilter expiredPasswordFilter;
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.addFilterAfter(expiredPasswordFilter, SecurityContextPersistenceFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, HeaderWriterFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, CsrfFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, LogoutFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, UsernamePasswordAuthenticationFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, DefaultLoginPageGeneratingFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, DefaultLogoutPageGeneratingFilter.class);
		http.addFilterAfter(expiredPasswordFilter, BasicAuthenticationFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, RequestCacheAwareFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, SecurityContextHolderAwareRequestFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, AnonymousAuthenticationFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, SessionManagementFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, ExceptionTranslationFilter.class);
//		http.addFilterAfter(expiredPasswordFilter, FilterSecurityInterceptor.class);
		
		http.authorizeRequests(authorize -> authorize
				.mvcMatchers("/account/register/**", "/forum/posts/**").permitAll()
				
				.mvcMatchers("/account/user/{user}/role/{role}/**")
					.hasRole("ADMINISTRATOR")
				.mvcMatchers(HttpMethod.PUT, "/account/user/{login}/**")
					.access("authentication.getName().equals(#login)")
				.mvcMatchers(HttpMethod.DELETE, "/account/user/{login}/**")
					.access("authentication.getName().equals(#login) or hasRole('ADMINISTRATOR')")	
				
				.mvcMatchers(HttpMethod.POST, "/forum/post/{author}/**")	
					.access("authentication.getName().equals(#author)")		
				.mvcMatchers(HttpMethod.POST, "/forum/post/{id}/comment/{author}/**")	
					.access("authentication.getName().equals(#author)")
				.mvcMatchers(HttpMethod.PUT, "/forum/post/{id}/like/**")
					.authenticated()
				.mvcMatchers(HttpMethod.PUT, "/forum/post/{id}/**")
					.access("@customWebSecurity.checkPostAuthor(id, authentication.getName())")
				.mvcMatchers(HttpMethod.DELETE, "/forum/post/{id}/**")
					.access("hasRole('MODERATOR') or @customWebSecurity.checkPostAuthor(id, authentication.getName())")	
					
				.anyRequest().authenticated());
		
		return http.build();
	}
	
	
	
}

