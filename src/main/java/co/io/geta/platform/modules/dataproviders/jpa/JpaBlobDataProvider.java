package co.io.geta.platform.modules.dataproviders.jpa;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.BlobTokenDto;
import co.io.geta.platform.crosscutting.domain.DeleteAssetDto;
import co.io.geta.platform.crosscutting.domain.UploadFileDto;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.BlobPayload;
import co.io.geta.platform.crosscutting.payload.BlobTokenPayload;
import co.io.geta.platform.crosscutting.payload.DeleteAssetPayload;
import co.io.geta.platform.crosscutting.payload.UploadFilePayload;
import co.io.geta.platform.crosscutting.persistence.entity.Game;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.GameAsset;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.SupportVersionAsset;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.GameAssetRepository;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.SupportVersionAssetRepository;
import co.io.geta.platform.crosscutting.persistence.repository.GameRepository;
import co.io.geta.platform.crosscutting.util.DateTimeZoneUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.util.RestConsumerUtil;
import co.io.geta.platform.crosscutting.validator.BlobTokenValidator;
import co.io.geta.platform.crosscutting.validator.DeleteAssetValidator;
import co.io.geta.platform.crosscutting.validator.UploadFileValidator;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.BlobDataProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Component
public class JpaBlobDataProvider implements BlobDataProvider {

	@Value(GetaConstants.SEED_ENCRYPTOR)
	private String seedEncriptor;

	@Value(GetaConstants.ACCOUNT_NAME_ENCRYPT)
	private String storageAccountNameEncript;

	@Value(GetaConstants.ACCOUNT_KEY_STORAGE)
	private String storageAccountKeyEncript;

	@Value(GetaConstants.CONECTION_ACCOUNT_STORAGE)
	private String connectionStringStorage;

	@Autowired
	private MessageUtil messageUtil;

	@Value(GetaConstants.LIMIT_TOKEN)
	private String limitToken;

	@Autowired
	private GameAssetRepository gameAssetRepository;

	@Autowired
	private SupportVersionAssetRepository supportVersionAssetRepository;

	@Autowired
	private DateTimeZoneUtil dateTimeZone;

	@Autowired
	private RestConsumerUtil restConsumerUtil;

	@Autowired
	private UploadFileValidator uploadFileValidator;

	@Autowired
	private DeleteAssetValidator deleteAssetValidator;

	@Autowired
	private BlobTokenValidator blobTokenValidator;

	@Autowired
	private GameRepository gameRepository;

	@Value(GetaConstants.HOST_BLOB_STORAGE_SVCL)
	private String hostBlobStorage;

	@Autowired
	private ObjectMapper mapper;

	/**
	 * This method generate SAS token to access file in blob storage
	 * 
	 * @param BlobTokenPayload payload with information query
	 * @return ApiResponse<BlobTokenDto>
	 */
	@Override
	public ApiResponse<BlobTokenDto> callblobRestAPIWithSas(BlobTokenPayload payload) {
		ApiResponse<BlobTokenDto> apiResponse;

		try {

			// Validate Payload
			blobTokenValidator.payLoadValidator(payload);

			// Obtain assets of dataBase
			GameAsset gameAsset = gameAssetRepository.obtainAssetByName(payload.getNameAsset());

			if (gameAsset == null || gameAsset.getGameId() == null) {

				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(messageUtil.getMessage(GetaConstants.NOT_RESOURCES_NAME));
				return apiResponse;
			}

			// Construct Uri method GET for genereate SasToken
			String uri = hostBlobStorage + GetaConstants.URI_SAS_TOKEN_BLOB_STORAGE + "?NameContainer="
					+ GetaConstants.NAME_BLOB_CONTAINER + "&PahtFile=" + payload.getNameAsset();

			// Generate sas Token
			Object object = restConsumerUtil.restConsumer(null, uri, HttpMethod.GET);

			// Convert Json to object
			JsonNode jsonNode = mapper.convertValue(object, JsonNode.class);
			BlobTokenDto sasTokenDto = mapper.convertValue(jsonNode.path(GetaConstants.DATA), BlobTokenDto.class);

			if (sasTokenDto == null) {

				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(mapper.convertValue(jsonNode.path(GetaConstants.MESSAGE), String.class));
				return apiResponse;
			}

			String sasUrl = sasTokenDto.getUrlToken();

			// blob
			// 2. test the sas token
			URL url = new URL(sasUrl);
			OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder().url(url).method("GET", null).build();
			okhttp3.Response response = httpClient.newCall(request).execute();

			if (!response.isSuccessful()) {
				BlobTokenDto blobToken = new BlobTokenDto();
				blobToken.setUrlToken(url.toString());

				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_CONFLICT);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(response.message());
				return apiResponse;
			}

			BlobTokenDto blobToken = new BlobTokenDto();
			blobToken.setUrlToken(url.toString());

			apiResponse = new ApiResponse<>();
			apiResponse.setData(blobToken);
			apiResponse.setStatusCode(HttpStatus.SC_OK);
			apiResponse.setSuccess(true);
			apiResponse.setMessage("");

		} catch (Exception ex) {

			apiResponse = new ApiResponse<>();
			apiResponse.setData(null);
			apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			apiResponse.setSuccess(false);
			apiResponse.setMessage(ex.getMessage());

		}

		return apiResponse;

	}

	/**
	 * upload file to blob storage in specific container
	 * 
	 * @param file    to upload to Blob Storage
	 * @param payload
	 * @return ApiResponse<UploadFileDto>
	 */
	@Override
	public ApiResponse<UploadFileDto> uploadFileBlobStorage(MultipartFile file, UploadFilePayload payload) {

		ApiResponse<UploadFileDto> apiResponse;

		try {

			// Validate Payload
			uploadFileValidator.payLoadValidator(file, payload);

			// Validate if gameId Exist
			Optional<Game> game = gameRepository.findById(payload.getGameId());

			if (!game.isPresent()) {
				throw new EBusinessException(messageUtil.getMessage(GetaConstants.GAME_ID_NOT_FOUND));
			}

			// Specifc structure folder where will save file
			String nameFolder = payload.getGameId() + GetaConstants.NAME_FOLDER_ASSET;

			// Upload file blob storage and Obtain Object response of consumer Api
			Object object = restConsumerUtil.multipartConsumerUploadFile(file,
					hostBlobStorage + GetaConstants.URI_UPLOAD_BLOB_STORAGE, GetaConstants.NAME_BLOB_CONTAINER, nameFolder);

			// Convert Json to object
			JsonNode jsonNode = mapper.convertValue(object, JsonNode.class);
			UploadFileDto fileDto = mapper.convertValue(jsonNode.path(GetaConstants.DATA), UploadFileDto.class);

			/// If the Dto is null, it means that the file could not be uploaded
			if (fileDto == null) {
				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(mapper.convertValue(jsonNode.path(GetaConstants.MESSAGE), String.class));
				return apiResponse;
			}

			// Verify if exist Asset wiht same name in BD
			GameAsset gameAssetExist = gameAssetRepository.obtainAssetByName(fileDto.getNameFile());

			// if exist Assets in BD then Update Asset
			if (gameAssetExist != null) {
				gameAssetExist.setUpdatedAt(dateTimeZone.getUTCdatetimeAsDate());
				gameAssetExist.setUrlBlob(fileDto.getUrlBlob());
				gameAssetExist.setNameAsset(fileDto.getNameFile());

				gameAssetRepository.save(gameAssetExist);

				apiResponse = new ApiResponse<>();
				apiResponse.setData(fileDto);
				apiResponse.setStatusCode(HttpStatus.SC_OK);
				apiResponse.setSuccess(true);
				apiResponse.setMessage(messageUtil.getMessage(GetaConstants.ASSET_HAS_BEEN_UPDATED));
				return apiResponse;
			}

			// Saved New Asset
			GameAsset gameAsset = saveGameAsset(fileDto, payload.getGameId());

			if (gameAsset == null) {
				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(messageUtil.getMessage(GetaConstants.GAME_ASSET_NOT_SAVED));
				return apiResponse;
			}

			fileDto.setGameId(gameAsset.getGameId());
			fileDto.setNameFile(gameAsset.getNameAsset());
			fileDto.setUrlBlob(gameAsset.getUrlBlob());

			apiResponse = new ApiResponse<>();
			apiResponse.setData(fileDto);
			apiResponse.setMessage(messageUtil.getMessage(GetaConstants.UPLOAD_FILED_SUCCES));
			apiResponse.setStatusCode(HttpStatus.SC_OK);
			apiResponse.setSuccess(true);

		} catch (Exception ex) {
			apiResponse = new ApiResponse<>();
			apiResponse.setData(null);
			apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			apiResponse.setSuccess(false);
			apiResponse.setMessage(ex.getMessage());
			return apiResponse;
		}
		return apiResponse;
	}

	/**
	 * Delete file asset in blob storage and table GameAsset
	 * 
	 * @param payload
	 * @return ApiResponse<DeleteAssetDto>
	 */
	@Override
	public ApiResponse<DeleteAssetDto> deleteAsset(DeleteAssetPayload payload) {

		ApiResponse<DeleteAssetDto> apiResponse;

		try {

			// Validate Payload
			deleteAssetValidator.deleteAssetValidator(payload);

			// Verify if assets exist
			GameAsset entity = gameAssetRepository.obtainAssetByName(payload.getNameAsset());

			if (entity == null || entity.getGameId() == null || entity.getGameId().isEmpty()) {
				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(messageUtil.getMessage(GetaConstants.NOT_RESOURCES_NAME));
				return apiResponse;
			}

			// build object to send to api
			BlobPayload blobPayload = new BlobPayload();
			blobPayload.setNameContainer(GetaConstants.NAME_BLOB_CONTAINER);
			blobPayload.setPahtFile(payload.getNameAsset());

			// Delete File Blob Storage, consumer api blob.storage.svc
			Object object = restConsumerUtil.restConsumer(blobPayload, hostBlobStorage, HttpMethod.DELETE);

			// Convert Json to object
			JsonNode jsonNode = mapper.convertValue(object, JsonNode.class);

			// Obtain response data and Convert to Object boolean that indicate if file was
			// delete
			Boolean isDelete = mapper.convertValue(jsonNode.path(GetaConstants.DATA).path(GetaConstants.IS_DELETE_KEY),
					Boolean.class);

			if (isDelete == null) {
				apiResponse = new ApiResponse<>();
				apiResponse.setData(null);
				apiResponse.setStatusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
				apiResponse.setSuccess(false);
				apiResponse.setMessage(mapper.convertValue(jsonNode.path(GetaConstants.MESSAGE), String.class));
				return apiResponse;
			}

			// First, the assets in the SupportVersionAsset table are deleted
			List<SupportVersionAsset> listSupport = supportVersionAssetRepository
					.listSupportVersionByGameAssetId(entity.getGameAssetId());
			supportVersionAssetRepository.deleteAll(listSupport);

			// Now delete Asset in the Table GameAsset
			gameAssetRepository.delete(entity);

			DeleteAssetDto deleteAssetDto = new DeleteAssetDto();
			deleteAssetDto.setAssetIsDelete(true);

			apiResponse = new ApiResponse<>();
			apiResponse.setData(deleteAssetDto);
			apiResponse.setStatusCode(HttpStatus.SC_OK);
			apiResponse.setSuccess(true);
			apiResponse.setMessage(messageUtil.getMessage(GetaConstants.DELETE_ASSET_SUCCES));

		} catch (Exception ex) {
			apiResponse = new ApiResponse<>();
			apiResponse.setData(null);
			apiResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			apiResponse.setSuccess(false);
			apiResponse.setMessage(ex.getMessage());
		}
		return apiResponse;
	}

	/**
	 * Saved information GameAsset
	 * 
	 * @param fileDto: Dto with the necessary information
	 * @param gameId,  gameid of Game
	 * @return entiry GameAsset
	 * @throws EBusinessException
	 */
	private GameAsset saveGameAsset(UploadFileDto fileDto, String gameId) throws EBusinessException {

		String uuid = UUID.randomUUID().toString().toUpperCase();

		GameAsset gameAsset = new GameAsset();
		gameAsset.setGameId(gameId);
		gameAsset.setNameAsset(fileDto.getNameFile());
		gameAsset.setUrlBlob(fileDto.getUrlBlob());
		gameAsset.setGameAssetId(uuid);
		gameAsset.setCreatedAt(dateTimeZone.getUTCdatetimeAsDate());
		gameAsset.setUpdatedAt(dateTimeZone.getUTCdatetimeAsDate());

		GameAsset entity = gameAssetRepository.save(gameAsset);

		if (entity.getGameId() == null || entity.getGameId().isEmpty()) {

			return null;
		}

		return entity;
	}
}
