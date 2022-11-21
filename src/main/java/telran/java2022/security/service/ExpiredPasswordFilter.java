package telran.java2022.security.service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

@Service
public class ExpiredPasswordFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
//		Principal principal = request.getUserPrincipal();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//		if (principal != null && checkEndPoint(request.getMethod(), request.getServletPath())) {
		if (authentication != null && checkEndPoint(request.getMethod(), request.getServletPath())) {
			UserProfile user = (UserProfile) authentication.getPrincipal();
			if (!user.isPasswordNotExpired()) {
				response.sendError(403, "User [" + user.getUsername() + "] password expired");
				System.out.println(
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()) + 
						" - user ["+user.getUsername()+"] try to active with password expired - ACCESS DENIED");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return !("Put".equalsIgnoreCase(method) && path.matches("/account/password/?"));
	}

}