package co.io.geta.platform.modules.dataproviders.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.ApplicationVersionDto;
import co.io.geta.platform.crosscutting.domain.GameApplicationByUserDto;
import co.io.geta.platform.crosscutting.domain.GameAssetDto;
import co.io.geta.platform.crosscutting.domain.GameDto;
import co.io.geta.platform.crosscutting.domain.GameUserUpdateDto;
import co.io.geta.platform.crosscutting.domain.SupportVersionDto;
import co.io.geta.platform.crosscutting.domain.VerifyUserDto;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionByNamePayload;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionPayload;
import co.io.geta.platform.crosscutting.payload.GamePayload;
import co.io.geta.platform.crosscutting.payload.GameUserUpdatePayload;
import co.io.geta.platform.crosscutting.payload.VerifyUserPayload;
import co.io.geta.platform.crosscutting.persistence.entity.Game;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.ApplicationVersion;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.GameAsset;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.GameUserUpdate;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.SupportVersionAsset;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.AplicationVersionRepository;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.GameAssetRepository;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.GameUserUpdateRepository;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.SupportVersionAssetRepository;
import co.io.geta.platform.crosscutting.persistence.repository.GameRepository;
import co.io.geta.platform.crosscutting.util.DateTimeZoneUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.util.RestConsumerUtil;
import co.io.geta.platform.crosscutting.validator.AplicationVersionByNameValidator;
import co.io.geta.platform.crosscutting.validator.AplicationVersionValidator;
import co.io.geta.platform.crosscutting.validator.GameUserUpdateValidator;
import co.io.geta.platform.crosscutting.validator.VerifyUserPayloadValidator;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.GameDataProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JpaGameDataProvider implements GameDataProvider {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private GameUserUpdateRepository gameUserRepository;

	@Autowired
	private AplicationVersionRepository aplicationVersionRepository;

	@Autowired
	private GameAssetRepository gameAssetRepository;

	@Autowired
	private SupportVersionAssetRepository supportVersionAssetRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private RestConsumerUtil restConsumerUtil;

	@Autowired
	private DateTimeZoneUtil dateTimeZone;

	@Value(GetaConstants.HOST_USER_IDENTITY)
	private String host;

	@Autowired
	private AplicationVersionValidator aplicationVersionValidator;

	@Autowired
	private GameUserUpdateValidator gameUserUpdateValidator;

	@Autowired
	private VerifyUserPayloadValidator verifyUserPayloadValidator;

	@Autowired
	private AplicationVersionByNameValidator aplicationVersionByNameValidator;

	/**
	 * Obtain list of all Games
	 * 
	 * @return ApiResponse<List<GameDto>>
	 */
	@Override
	public ApiResponse<List<GameDto>> listGames() {

		List<GameDto> listGameDto = null;
		ApiResponse<List<GameDto>> response = new ApiResponse<>();

		try {

			// Obtain list Games
			List<Game> listGame = (List<Game>) gameRepository.findAll();

			// Convert entity to GameDto
			listGameDto = modelMapper.map(listGame, new TypeToken<List<GameDto>>() {
			}.getType());

			response.setData(listGameDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);

		} catch (Exception ex) {

			log.info(ex.getMessage());
			response.setData(listGameDto);
			response.setStatusCode(HttpStatus.PRECONDITION_FAILED.value());
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
		}

		return response;
	}

	/**
	 * Obtain List gameAssets
	 * 
	 * @return ApiResponse<List<GameAssetDto>>
	 */
	@Override
	public ApiResponse<List<GameAssetDto>> listGameAssets() {

		List<GameAssetDto> listGameAssetDto = null;
		ApiResponse<List<GameAssetDto>> response = new ApiResponse<>();

		try {

			// Obtain list GameAsset
			List<GameAsset> listGameAssets = (List<GameAsset>) gameAssetRepository.findAll();

			// convert entity to List<GameDto>
			listGameAssetDto = modelMapper.map(listGameAssets, new TypeToken<List<GameAssetDto>>() {
			}.getType());

			response.setData(listGameAssetDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);
		} catch (Exception ex) {

			log.info(ex.getMessage());
			response.setData(null);
			response.setStatusCode(HttpStatus.PRECONDITION_FAILED.value());
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	/**
	 * Obtain list of all GameUserUpdate
	 * 
	 * @return ApiResponse<List<GameUserUpdateDto>>
	 */
	@Override
	public ApiResponse<List<GameUserUpdateDto>> allGameUserUpdate() {

		List<GameUserUpdateDto> listGameUserUpdateDto = null;
		ApiResponse<List<GameUserUpdateDto>> response = new ApiResponse<>();

		try {
			// Gets all the user and indicates which version of the application it is
			// related to
			List<GameUserUpdate> listGameUserUpdate = (List<GameUserUpdate>) gameUserRepository.findAll();

			if (listGameUserUpdate.isEmpty()) {

				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.NO_CONTENT.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.THERE_IS_NOT_INFORMATION_CONSULT));
				return response;
			}

			// Check which assets are supported according to the version of the application
			// in which the user is
			supportAssetByapplicationVersion(listGameUserUpdate);

			// Convert entity t Dto
			listGameUserUpdateDto = modelMapper.map(listGameUserUpdate, new TypeToken<List<GameUserUpdateDto>>() {
			}.getType());

			response.setData(listGameUserUpdateDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);
			response.setMessage("");

		} catch (Exception ex) {

			response.setData(listGameUserUpdateDto);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setSuccess(true);
			response.setMessage(ex.getMessage());
		}

		return response;
	}

	/**
	 * Obtain list all applicationVersion
	 * 
	 * @return ApiResponse<List<ApplicationVersionDto>>
	 */
	@Override
	public ApiResponse<List<ApplicationVersionDto>> listAplicationVersion() {

		ApiResponse<List<ApplicationVersionDto>> response = new ApiResponse<>();
		List<ApplicationVersionDto> listApplicationVersionDto = null;

		try {

			// obtain list applicationVersion
			List<ApplicationVersion> aplicationVersions = (List<ApplicationVersion>) aplicationVersionRepository.findAll();

			if (aplicationVersions.isEmpty()) {

				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.NO_CONTENT.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.THERE_IS_NOT_INFORMATION_CONSULT));
				return response;
			}

			// Check which assets are supported according to the version of the application
			// in which the user is
			supportAssetByapplicationVersionV2(aplicationVersions);

			// Convert entity to Dto
			listApplicationVersionDto = modelMapper.map(aplicationVersions, new TypeToken<List<ApplicationVersionDto>>() {
			}.getType());

			response.setData(listApplicationVersionDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);
			response.setMessage("");

		} catch (Exception ex) {

			response.setData(listApplicationVersionDto);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setSuccess(true);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	/**
	 * Obtain a Game by GameId
	 * 
	 * @param payload
	 * @return ApiResponse<GameDto>
	 */
	@Override
	public ApiResponse<GameDto> getGameById(GamePayload payload) {

		ApiResponse<GameDto> response = new ApiResponse<>();
		GameDto gameDto = null;
		try {

			// Obtain entity Game
			Optional<Game> game = gameRepository.findById(payload.getGameId());

			if (!game.isPresent()) {
				response.setData(null);
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.GAME_ID_NOT_FOUND));
				return response;
			}

			// Convert entity to Dto
			gameDto = modelMapper.map(game.get(), GameDto.class);

			response.setData(gameDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);

		} catch (Exception ex) {
			response.setData(gameDto);
			response.setStatusCode(HttpStatus.PRECONDITION_FAILED.value());
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
		}

		return response;
	}

	/**
	 * Obtain a GameuserUpdate entity by UserId
	 * 
	 * @param payload
	 * @return ApiResponse<GameApplicationByUserDto>
	 */
	@Override
	public ApiResponse<GameApplicationByUserDto> getGameUserUpdateByUserId(VerifyUserPayload payload) {

		ApiResponse<GameApplicationByUserDto> response = new ApiResponse<>();
		GameApplicationByUserDto aplicationByUserDto = null;

		try {

			// Verify payload
			verifyUserPayloadValidator.payLoadValidator(payload);

			// Verify if userId exist
			boolean userExist = verifyUserId(payload);

			if (!userExist) {
				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.USER_GAME_NOT_FOUND));
				return response;
			}

			// Obtain list GameUserUpdate entity
			List<GameUserUpdate> userUpdate = gameUserRepository.getGameListUser(payload.getUserId());

			if (userUpdate.isEmpty()) {
				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.USER_NOT_FOUND));
				return response;
			}

			// Check which assets are supported according to the version of the application
			// in which the user is
			supportAssetByapplicationVersion(userUpdate);

			aplicationByUserDto = new GameApplicationByUserDto();
			List<ApplicationVersion> listAplicationVersion = new ArrayList<>();

			// Build Dto, create list of aplicationVersion where user is related
			for (GameUserUpdate item : userUpdate) {

				aplicationByUserDto.setUserId(item.getUserId());
				listAplicationVersion.add(item.getApplicationVersion());
			}

			// Convert list Entity to list Dto
			List<ApplicationVersionDto> listAplicationVersionDto = modelMapper.map(listAplicationVersion,
					new TypeToken<List<ApplicationVersionDto>>() {
					}.getType());

			aplicationByUserDto.setApplicationVersion(listAplicationVersionDto);

			response.setData(aplicationByUserDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);
			response.setMessage("");

		} catch (Exception ex) {

			response.setData(aplicationByUserDto);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	/**
	 * Obtain ApplicationVersion entity by nameVersion of application
	 * 
	 * @param payload
	 * @return ApiResponse<ApplicationVersionDto>
	 */
	@Override
	public ApiResponse<ApplicationVersionDto> getAplicationVersionByName(ApplicationVersionByNamePayload payload) {

		ApiResponse<ApplicationVersionDto> response = new ApiResponse<>();
		ApplicationVersionDto aplicationDto = null;

		try {

			// Validate Payload
			aplicationVersionByNameValidator.payLoadValidator(payload);

			// Obtain application Version
			ApplicationVersion aplicationversion = aplicationVersionRepository
					.getAplicationVersionByName(payload.getNameVersion());

			if (aplicationversion == null || aplicationversion.getApplicationVersionId() == null) {

				response.setData(null);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.APLICATION_VERSION_NOT_FOUND));
				return response;
			}

			// Obtain List SupportVersionAsset
			List<SupportVersionAsset> listSupport = supportVersionAssetRepository
					.listSupportVersionByAplicationVersionId(aplicationversion.getApplicationVersionId());

			if (listSupport.isEmpty()) {

				response.setData(null);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.OPERATION_NOT_SUCCES));
				return response;
			}

			// Add list SupportVersion asset in Application Version
			aplicationversion.setSupportVersionAssets(listSupport);
			aplicationDto = modelMapper.map(aplicationversion, ApplicationVersionDto.class);

			response.setData(aplicationDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);
			response.setMessage("");

		} catch (Exception ex) {
			response.setData(aplicationDto);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	/**
	 * Allow saved ApplicationVersion in DataBase
	 * 
	 * @param payload
	 * @return ApiResponse<ApplicationVersionDto>
	 */
	@Override
	public ApiResponse<ApplicationVersionDto> saveAplicationVersion(ApplicationVersionPayload payload) {

		ApiResponse<ApplicationVersionDto> response = new ApiResponse<>();

		try {

			// Verification payload
			aplicationVersionValidator.payLoadValidator(payload);

			// Verification if exist GameId
			Optional<Game> game = gameRepository.findById(payload.getGameId());

			if (!game.isPresent()) {

				response.setData(null);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.GAME_ID_NOT_FOUND));
				return response;
			}

			// Verify if exist applicationVersion
			ApplicationVersion aplicationV = aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion());

			if (aplicationV != null && aplicationV.getApplicationVersionId() != null) {

				response.setData(null);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.APLICATION_VERSION_EXIST));
				return response;
			}

			String uuid = UUID.randomUUID().toString().toUpperCase();

			ApplicationVersion entity = new ApplicationVersion();
			entity.setCreatedAt(dateTimeZone.getUTCdatetimeAsDate());
			entity.setUpdatedAt(dateTimeZone.getUTCdatetimeAsDate());
			entity.setNameVersion(payload.getNameVersion());
			entity.setApplicationVersionId(uuid);
			entity.setGameId(game.get().getGameId());

			// Saved ApplicationVersion
			entity = aplicationVersionRepository.save(entity);

			if (entity.getGameId() == null || entity.getGameId().isEmpty()) {
				response.setData(null);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.OPERATION_NOT_SUCCES));
				return response;
			}

			List<SupportVersionAsset> listSuppor = saveListSupportVersion(payload.getAssetsId(), entity);

			// Saved List SupportVersion to related with applicationVersion
			List<SupportVersionDto> saveListSupportVersion = modelMapper.map(listSuppor,
					new TypeToken<List<SupportVersionDto>>() {
					}.getType());

			if (saveListSupportVersion.isEmpty()) {
				response.setData(null);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setSuccess(false);
				response.setMessage(messageUtil.getMessage(GetaConstants.OPERATION_NOT_SUCCES));
				aplicationVersionRepository.deleteById(entity.getApplicationVersionId());
				return response;
			}

			// Add List SupportVersioDto to ApplicationVersionDto
			ApplicationVersionDto aplicationVersionDto = modelMapper.map(entity, ApplicationVersionDto.class);
			aplicationVersionDto.setSupportVersionAssets(saveListSupportVersion);

			response.setData(aplicationVersionDto);
			response.setStatusCode(HttpStatus.OK.value());
			response.setSuccess(true);
			response.setMessage("");

		} catch (Exception ex) {
			response.setData(null);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	/**
	 * Obtain List SupportVersion entity
	 * 
	 * @param gameAssetId, reference id to game
	 * @param entity;      reference to ApplicationVersion entity with related
	 *                     assets
	 * @return
	 * @throws EBusinessException
	 */
	private List<SupportVersionAsset> saveListSupportVersion(List<String> gameAssetId, ApplicationVersion entity)
			throws EBusinessException {
		List<SupportVersionAsset> supportVersion = null;

		if (gameAssetId.isEmpty()) {
			return supportVersion;
		}

		try {
			Iterable<GameAsset> list = gameAssetRepository.findAllById(gameAssetId);
			supportVersion = new ArrayList<>();
			for (GameAsset item : list) {
				String uuid = UUID.randomUUID().toString().toUpperCase();

				SupportVersionAsset supportVersionAsset = new SupportVersionAsset();
				supportVersionAsset.setSupportVersionAssetId(uuid);
				supportVersionAsset.setCreatedAt(dateTimeZone.getUTCdatetimeAsDate());
				supportVersionAsset.setUpdatedAt(dateTimeZone.getUTCdatetimeAsDate());
				supportVersionAsset.setGameAsset(item);
				supportVersionAsset.setApplicationVersion(entity);
				supportVersion.add(supportVersionAsset);
			}

			supportVersion = (List<SupportVersionAsset>) supportVersionAssetRepository.saveAll(supportVersion);

		} catch (Exception ex) {
			supportVersion = null;
			log.error(ex.getMessage());
		}

		return supportVersion;
	}

	/**
	 * Save GameUserUpdate entity
	 * 
	 * @param payload
	 * @return ApiResponse<GameUserUpdateDto>
	 */
	@Override
	public ApiResponse<GameUserUpdateDto> saveGameUserUpdate(GameUserUpdatePayload payload) {

		ApiResponse<GameUserUpdateDto> response = new ApiResponse<>();

		try {
			// Validate Payload
			gameUserUpdateValidator.payLoadValidator(payload);

			VerifyUserPayload payloadUser = new VerifyUserPayload(payload.getUserId());

			// Verify if exist userId and GameId
			boolean userValidate = verifyUserId(payloadUser);

			if (!userValidate) {
				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.USER_GAME_NOT_FOUND));
				return response;
			}

			// Verify if exist AplicationVersion by ID
			Optional<ApplicationVersion> apliAplicationVersion = aplicationVersionRepository
					.findById(payload.getAplicationVersionId());

			if (!apliAplicationVersion.isPresent()) {
				response.setData(null);
				response.setSuccess(true);
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.APLICATION_VERSION_NOT_EXIST));
				return response;
			}

			// Obtain asset support by application version
			List<SupportVersionAsset> listSuporrt = supportVersionAssetRepository
					.listSupportVersionByAplicationVersionId(apliAplicationVersion.get().getApplicationVersionId());

			// if no exist information then create object and saved
			GameUserUpdate entity = gameUserRepository.getGameUser(apliAplicationVersion.get().getGameId(),
					payload.getUserId());

			if (entity == null || entity.getGameUserUpdateId() == null) {

				entity = new GameUserUpdate();
				String uuid = UUID.randomUUID().toString().toUpperCase();

				entity.setGameUserUpdateId(uuid);
				entity.setGameId(apliAplicationVersion.get().getGameId());
				entity.setUserId(payload.getUserId());
				entity.setIsUpdate(true);
				entity.setApplicationVersion(apliAplicationVersion.get());
				entity.setCreatedAt(dateTimeZone.getUTCdatetimeAsDate());
				entity.setUpdatedAt(dateTimeZone.getUTCdatetimeAsDate());

				GameUserUpdate saved = gameUserRepository.save(entity);
				saved.getApplicationVersion().setSupportVersionAssets(listSuporrt);

				GameUserUpdateDto dto = modelMapper.map(saved, GameUserUpdateDto.class);

				response.setData(dto);
				response.setSuccess(true);
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage(messageUtil.getMessage(GetaConstants.RECORD_SAVED));
				return response;
			}

			entity.setIsUpdate(true);
			entity.setApplicationVersion(apliAplicationVersion.get());
			entity.setUpdatedAt(dateTimeZone.getUTCdatetimeAsDate());

			GameUserUpdate saved = gameUserRepository.save(entity);
			saved.getApplicationVersion().setSupportVersionAssets(listSuporrt);

			GameUserUpdateDto dto = modelMapper.map(saved, GameUserUpdateDto.class);

			response = new ApiResponse<>();
			response.setData(dto);
			response.setSuccess(true);
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage(messageUtil.getMessage(GetaConstants.RECORD_UPDATED));

		} catch (Exception ex) {

			response.setData(null);
			response.setSuccess(true);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage(ex.getMessage());
		}

		return response;
	}

	/**
	 * Related SupportVersionAsset with ApplicationVersion correspondent
	 * 
	 * @param listGame gameUserUpdate entity
	 * @throws EBusinessException
	 */
	private void supportAssetByapplicationVersion(List<GameUserUpdate> listGame) throws EBusinessException {

		try {
			List<SupportVersionAsset> list = null;
			for (GameUserUpdate item : listGame) {
				list = supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId(item.getApplicationVersion().getApplicationVersionId());
				item.getApplicationVersion().setSupportVersionAssets(list);
			}

		} catch (Exception ex) {
			throw new EBusinessException(ex.getMessage());
		}

	}

	/**
	 * Related SupportVersionAsset with List ApplicationVersion correspondent
	 * 
	 * @param ApplicationVersion listAplicationVersion
	 * @throws EBusinessException
	 */
	private void supportAssetByapplicationVersionV2(List<ApplicationVersion> listAplicationVersion)
			throws EBusinessException {

		try {
			List<SupportVersionAsset> list = null;
			for (ApplicationVersion item : listAplicationVersion) {

				list = supportVersionAssetRepository.listSupportVersionByAplicationVersionId(item.getApplicationVersionId());
				item.setSupportVersionAssets(list);
			}

		} catch (Exception ex) {
			throw new EBusinessException(ex.getMessage());
		}

	}

	/**
	 * Verify if exist user with userId
	 * 
	 * @param payload
	 * @return
	 */
	private boolean verifyUserId(VerifyUserPayload payload) {
		// Consuming endpoint to get information Games
		boolean isValidate = false;
		Object jsonRules = restConsumerUtil.restConsumer(payload, host, HttpMethod.POST);
		JsonNode jsonRule = mapper.convertValue(jsonRules, JsonNode.class);
		JsonNode user = jsonRule.path(GetaConstants.DATA);
		VerifyUserDto userDto = mapper.convertValue(user, VerifyUserDto.class);

		if (userDto == null || userDto.getUserId() == null) {
			isValidate = false;
		} else {
			isValidate = true;
		}
		return isValidate;
	}
}
