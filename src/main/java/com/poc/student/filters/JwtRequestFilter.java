package com.poc.student.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poc.student.utils.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	InMemoryUserDetailsManager userDetailsManager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwtToken=null;
		String userName=null;
		final String authorization = request.getHeader("Authorization");
		
		if(authorization !=null && authorization.startsWith("Bearer ")) {
			jwtToken = authorization.substring(7);
			userName = jwtUtil.getUsernameFromToken(jwtToken);
		}
		
		if(userName !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetail = userDetailsManager.loadUserByUsername(userName);
			
			if(jwtUtil.validateToken(jwtToken, userDetail))
			{
				UsernamePasswordAuthenticationToken authToken = jwtUtil.getAuthenticationToken(jwtToken, SecurityContextHolder.getContext().getAuthentication(), userDetail);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
			}
		}
		
		filterChain.doFilter(request, response);
		
	}

}
