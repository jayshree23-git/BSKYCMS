package com.project.bsky.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.bsky.serviceImpl.CustomUserDetailsService;
import com.project.bsky.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomUserDetailsService service;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		String ipAddress = getClientIpAddr(httpServletRequest);
		String token = null;
		String userName = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			userName = jwtUtil.extractUsername(token);
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = service.loadUserByUsername(userName);

			if (jwtUtil.validateToken(token, userDetails, ipAddress)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
	public String getClientIpAddr(HttpServletRequest request) {
	    String ip = "";
	    try {
	       ip = request.getHeader("X-Forwarded-For");
	       if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
	          ip = request.getHeader("Proxy-Client-IP");

	       if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
	          ip = request.getHeader("WL-Proxy-Client-IP");

	       if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
	          ip = request.getHeader("HTTP_CLIENT_IP");

	       if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
	          ip = request.getHeader("HTTP_X_FORWARDED_FOR");

	       if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
	          ip = request.getRemoteAddr();
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	    return ip;
	}

}
