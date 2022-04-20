package co.io.geta.platform.modules.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.BlobTokenDto;
import co.io.geta.platform.crosscutting.domain.JwtResponseDto;
import co.io.geta.platform.crosscutting.payload.BlobTokenPayload;
import co.io.geta.platform.crosscutting.payload.JwtPayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.usecases.BlobProcess;
import co.io.geta.platform.modules.usecases.TokenProcess;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = GetaConstants.ROOT_API_TOKEN, produces = { MediaType.APPLICATION_JSON_VALUE })
public class TokenWebApi {

	@Autowired
	private TokenProcess tokenProcess;

	@Autowired
	private BlobProcess blobProcess;

	@ApiOperation(value = "Obtain Token Acces", notes = "It allows to have an access token, the token has a time limit of one hour")
	@PostMapping(GetaConstants.AUTHENTICATE)
	public ApiResponse<JwtResponseDto> createAuthenticationToken(@RequestBody JwtPayload authenticationRequest) {
		return tokenProcess.responseToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
	}

	@ApiOperation(value = "Obtain a token for acces resources", notes = "Gets the game specified by its existing GameId")
	@GetMapping(GetaConstants.BLOB)
	public ApiResponse<BlobTokenDto> blobToken(BlobTokenPayload payload) {
		return blobProcess.callblobRestAPIWithSas(payload);
	}
}
