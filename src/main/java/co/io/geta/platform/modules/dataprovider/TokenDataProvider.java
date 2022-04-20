package co.io.geta.platform.modules.dataprovider;

import co.io.geta.platform.crosscutting.domain.JwtResponseDto;
import co.io.geta.platform.modules.common.ApiResponse;

public interface TokenDataProvider {

	public ApiResponse<JwtResponseDto> responseToken(String username, String password);
}
