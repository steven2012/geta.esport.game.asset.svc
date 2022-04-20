package co.io.geta.platform.modules.dataprovider;

import java.util.List;

import co.io.geta.platform.crosscutting.domain.ApplicationVersionDto;
import co.io.geta.platform.crosscutting.domain.GameApplicationByUserDto;
import co.io.geta.platform.crosscutting.domain.GameAssetDto;
import co.io.geta.platform.crosscutting.domain.GameDto;
import co.io.geta.platform.crosscutting.domain.GameUserUpdateDto;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionByNamePayload;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionPayload;
import co.io.geta.platform.crosscutting.payload.GamePayload;
import co.io.geta.platform.crosscutting.payload.GameUserUpdatePayload;
import co.io.geta.platform.crosscutting.payload.VerifyUserPayload;
import co.io.geta.platform.modules.common.ApiResponse;

public interface GameDataProvider {

	public ApiResponse<List<GameDto>> listGames();

	public ApiResponse<GameDto> getGameById(GamePayload payload);

	public ApiResponse<GameApplicationByUserDto> getGameUserUpdateByUserId(VerifyUserPayload payload);

	public ApiResponse<ApplicationVersionDto> getAplicationVersionByName(ApplicationVersionByNamePayload payload);

	public ApiResponse<List<GameUserUpdateDto>> allGameUserUpdate();

	public ApiResponse<List<GameAssetDto>> listGameAssets();

	public ApiResponse<List<ApplicationVersionDto>> listAplicationVersion();

	public ApiResponse<GameUserUpdateDto> saveGameUserUpdate(GameUserUpdatePayload payload);

	public ApiResponse<ApplicationVersionDto> saveAplicationVersion(ApplicationVersionPayload payload);

}
