package co.io.geta.platform.modules.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.domain.JwtResponseDto;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.TokenDataProvider;

@Component
public class TokenProcess {

	@Autowired
	private TokenDataProvider tokenDataProvider;

	public ApiResponse<JwtResponseDto> responseToken(String username, String password) {

		return tokenDataProvider.responseToken(username, password);
	}

}
