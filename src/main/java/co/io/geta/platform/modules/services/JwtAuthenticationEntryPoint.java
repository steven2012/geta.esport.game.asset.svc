package co.io.geta.platform.modules.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.ErrorResponseDto;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.modules.common.ApiResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private MessageUtil messageUtil;

	/**
	 * This Method verify if Token access to expired
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		final String expired = (String) request.getAttribute(messageUtil.getMessage(GetaConstants.ATRIBUTE_EXPIRED));

		// Message Error when token expired
		if (expired != null) {

			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
			errorResponse.setMessage(messageUtil.getMessage(GetaConstants.EXPIRED_TOKEN_MESSAGE));

			sendErrorResponse(response, errorResponse, HttpServletResponse.SC_UNAUTHORIZED, false,
					messageUtil.getMessage(GetaConstants.EXPIRED_TOKEN_MESSAGE));

		}
		// Any other error
		else {

			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
			errorResponse.setMessage(messageUtil.getMessage(GetaConstants.UNAUTHORIZED_MESSAGE));
			sendErrorResponse(response, errorResponse, HttpServletResponse.SC_UNAUTHORIZED, false,
					messageUtil.getMessage(GetaConstants.UNAUTHORIZED_MESSAGE));
		}

	}

	private void sendErrorResponse(HttpServletResponse response, ErrorResponseDto errorResponse, int statusCode,
			boolean success, String message) throws IOException {

		ApiResponse<ErrorResponseDto> respons = new ApiResponse<>();
		respons.setData(errorResponse);
		respons.setStatusCode(statusCode);
		respons.setSuccess(success);
		respons.setMessage(message);

		byte[] responseToSend = restResponseBytes(respons);
		response.setHeader("Content-Type", "application/json");
		response.getOutputStream().write(responseToSend);
		response.flushBuffer();
	}

	private byte[] restResponseBytes(ApiResponse<ErrorResponseDto> eErrorResponse) throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
		return serialized.getBytes();
	}

}
