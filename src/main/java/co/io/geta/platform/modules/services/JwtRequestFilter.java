package co.io.geta.platform.modules.services;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.util.JwtTokenUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MessageUtil messageUtil;

	/**
	 * This method very if Api require of a token
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {

			final String requestTokenHeader = request.getHeader(GetaConstants.AUTHORIZATION_HEADER);

			String username = null;
			String jwtToken = null;
			// JWT Token is in the form "Bearer token". Remove Bearer word and get
			// only the Token
			if (requestTokenHeader != null && requestTokenHeader.startsWith(GetaConstants.BEARER_HEADER)) {
				jwtToken = requestTokenHeader.substring(7);
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);

			}

			else {
				logger.warn(messageUtil.getMessage(GetaConstants.NOT_BEARER_CONTAIN_MESSAGE));
			}

			// Once we get the token validate it.
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

				// if token is valid configure Spring Security to manually set
				// authentication
				boolean validateToken = jwtTokenUtil.validateToken(jwtToken, userDetails);

				if (validateToken) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			chain.doFilter(request, response);
		} catch (IllegalArgumentException ex) {
			chain.doFilter(request, response);
			log.info(messageUtil.getMessage(GetaConstants.UNABLE_GET_TOKEN));

		} catch (ExpiredJwtException ex) {

			request.setAttribute(messageUtil.getMessage(GetaConstants.ATRIBUTE_EXPIRED), ex.getMessage());
			log.info(messageUtil.getMessage(GetaConstants.EXPIRED_TOKEN_MESSAGE));
			chain.doFilter(request, response);
		}

	}
}
