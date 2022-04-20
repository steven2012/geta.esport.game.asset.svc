package co.io.geta.platform.modules.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.domain.ApplicationVersionDto;
import co.io.geta.platform.crosscutting.domain.GameApplicationByUserDto;
import co.io.geta.platform.crosscutting.domain.GameAssetDto;
import co.io.geta.platform.crosscutting.domain.GameUserUpdateDto;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionByNamePayload;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionPayload;
import co.io.geta.platform.crosscutting.payload.GameUserUpdatePayload;
import co.io.geta.platform.crosscutting.payload.VerifyUserPayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.GameDataProvider;

@Component
public class GameProcess {

	@Autowired
	private GameDataProvider gameDataProvider;

	public ApiResponse<GameUserUpdateDto> saveGameUserUpdate(GameUserUpdatePayload payload) {
		return gameDataProvider.saveGameUserUpdate(payload);
	}

	public ApiResponse<GameApplicationByUserDto> getGameUserUpdateByUserId(VerifyUserPayload payload) {
		return gameDataProvider.getGameUserUpdateByUserId(payload);
	}

	public ApiResponse<ApplicationVersionDto> getAplicationVersionByName(ApplicationVersionByNamePayload payload) {
		return gameDataProvider.getAplicationVersionByName(payload);
	}

	public ApiResponse<List<GameAssetDto>> listGameAssets() {
		return gameDataProvider.listGameAssets();
	}

	public ApiResponse<List<GameUserUpdateDto>> listGameUserUpdate() {
		return gameDataProvider.allGameUserUpdate();
	}

	public ApiResponse<List<ApplicationVersionDto>> listAplicationVersion() {
		return gameDataProvider.listAplicationVersion();
	}

	public ApiResponse<ApplicationVersionDto> saveAplicationVersion(ApplicationVersionPayload payload) {
		return gameDataProvider.saveAplicationVersion(payload);
	}

}
