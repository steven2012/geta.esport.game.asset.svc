package co.io.geta.platform.modules.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.ApplicationVersionDto;
import co.io.geta.platform.crosscutting.domain.GameApplicationByUserDto;
import co.io.geta.platform.crosscutting.domain.GameAssetDto;
import co.io.geta.platform.crosscutting.domain.GameUserUpdateDto;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionByNamePayload;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionPayload;
import co.io.geta.platform.crosscutting.payload.GameUserUpdatePayload;
import co.io.geta.platform.crosscutting.payload.VerifyUserPayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.usecases.GameProcess;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = GetaConstants.ROOT_API_STORAGE, produces = { MediaType.APPLICATION_JSON_VALUE })
public class GameWebApi {

	@Autowired
	private GameProcess gameProcess;

	@ApiOperation(value = "Get all the gamesAssets from the BD")
	@GetMapping(GetaConstants.GAME_ASSETS)
	public ApiResponse<List<GameAssetDto>> gameAssets() {
		return gameProcess.listGameAssets();
	}

	@ApiOperation(value = "Obtains the information of a user from his ID about the version of the application in which he is and the assets that this version supports")
	@GetMapping(GetaConstants.USER_APLICATION_BY_ID)
	public ApiResponse<GameApplicationByUserDto> getGameUserUpdateByUserId(VerifyUserPayload payload) {
		return gameProcess.getGameUserUpdateByUserId(payload);
	}

	@ApiOperation(value = "Gets all the user and indicates which version of the application it is related to")
	@GetMapping(GetaConstants.USER_APLICATION)
	public ApiResponse<List<GameUserUpdateDto>> gameAllUserUpdate() {
		return gameProcess.listGameUserUpdate();
	}

	@ApiOperation(value = "Saved User Api", notes = "It allows adding or updating a record, where it is specified in which version of the application a user is found")
	@PostMapping(GetaConstants.USER_APLICATION)
	public ApiResponse<GameUserUpdateDto> saveGameUserUpdate(@RequestBody GameUserUpdatePayload payload) {
		return gameProcess.saveGameUserUpdate(payload);
	}

	@ApiOperation(value = "Get all versions of applications stored in DB")
	@GetMapping(GetaConstants.APLICATION_VERSION)
	public ApiResponse<List<ApplicationVersionDto>> listAplicationVersion() {
		return gameProcess.listAplicationVersion();
	}

	@ApiOperation(value = "Get aplication versionsby name")
	@GetMapping(GetaConstants.APLICATION_VERSION_BY_NAME)
	public ApiResponse<ApplicationVersionDto> getAplicationVersionByName(ApplicationVersionByNamePayload payload) {
		return gameProcess.getAplicationVersionByName(payload);
	}

	@ApiOperation(value = "Saved Aplication Version", notes = "It allows adding a record that indicates the version of the application and the assets it supports")
	@PostMapping(GetaConstants.APLICATION_VERSION)
	public ApiResponse<ApplicationVersionDto> saveAplicationVersion(@RequestBody ApplicationVersionPayload payload) {
		return gameProcess.saveAplicationVersion(payload);
	}

}
