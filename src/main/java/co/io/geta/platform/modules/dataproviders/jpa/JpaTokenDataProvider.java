package co.io.geta.platform.modules.dataproviders.jpa;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.JwtResponseDto;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.util.JwtTokenUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.TokenDataProvider;

@Configuration
public class JpaTokenDataProvider implements TokenDataProvider {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MessageUtil messageUtil;

	@Override
	public ApiResponse<JwtResponseDto> responseToken(String username, String password) {

		try {
			authenticate(username, password);

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			String token = jwtTokenUtil.generateToken(userDetails);

			JwtResponseDto responsetoken = new JwtResponseDto(token);
			ApiResponse<JwtResponseDto> response = new ApiResponse<>();
			response.setData(responsetoken);
			response.setStatusCode(HttpStatus.SC_OK);
			response.setSuccess(true);
			return response;

		} catch (Exception ex) {

			ApiResponse<JwtResponseDto> response = new ApiResponse<>();
			response.setData(null);
			response.setStatusCode(HttpStatus.SC_CONFLICT);
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
			return response;

		}
	}

	private void authenticate(String username, String password) throws EBusinessException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new EBusinessException(messageUtil.getMessage(GetaConstants.USER_DISABLED), e);
		} catch (BadCredentialsException e) {
			throw new EBusinessException(messageUtil.getMessage(GetaConstants.INVALID_CREDENTIALS), e);
		}
	}

}
