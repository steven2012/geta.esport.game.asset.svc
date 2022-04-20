package co.io.geta.platform.modules.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.BlobTokenDto;
import co.io.geta.platform.crosscutting.domain.UploadFileDto;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.BlobTokenPayload;
import co.io.geta.platform.crosscutting.payload.UploadFilePayload;
import co.io.geta.platform.crosscutting.persistence.entity.Game;
import co.io.geta.platform.crosscutting.persistence.game.asset.entity.GameAsset;
import co.io.geta.platform.crosscutting.persistence.game.asset.repository.GameAssetRepository;
import co.io.geta.platform.crosscutting.persistence.repository.GameRepository;
import co.io.geta.platform.crosscutting.util.BlobStorageUtil;
import co.io.geta.platform.crosscutting.util.DateTimeZoneUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.util.RestConsumerUtil;
import co.io.geta.platform.crosscutting.validator.BlobTokenValidator;
import co.io.geta.platform.crosscutting.validator.DeleteAssetValidator;
import co.io.geta.platform.crosscutting.validator.UploadFileValidator;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataproviders.jpa.JpaBlobDataProvider;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BlobDataProviderTest {

	public final static String MOCK_GAME_ASSET = "src" + File.separator + "" + File.separator + "test" + File.separator
			+ "" + File.separator + "resources" + File.separator + "" + File.separator + "MockGameAsset.json";

	public final static String MOCK_RESPONSE_SAS_TOKEN = "src" + File.separator + "" + File.separator + "test"
			+ File.separator + "" + File.separator + "resources" + File.separator + "" + File.separator
			+ "MockResponseSasToken.json";

	public final static String PATH = "src" + File.separator + "" + File.separator + "test" + File.separator + ""
			+ File.separator + "resources" + File.separator + "" + File.separator + "MockGame.json";

	public final static String URL = "https://genesysdevstorage.blob.core.windows.net";
	public final static String URL_ACCES = "https://jsonplaceholder.typicode.com/todos/1";
	public final static String URL_CONSUMER_SAS = "http://netTest/sas-token?NameContainer=game-assets&PahtFile=Puzzle";

	@InjectMocks
	private JpaBlobDataProvider blobDataProvider;

	@Mock
	private MessageSource messageSource;

	@Mock
	private BlobStorageUtil blobStorageUtil;

	@Mock
	private GameAssetRepository gameAssetRepository;

	@Mock
	private DateTimeZoneUtil dateTimeZone;

	@Mock
	private UploadFileValidator uploadFileValidator;

	@Mock
	private DeleteAssetValidator deleteAssetValidator;

	@Mock
	private RestConsumerUtil restConsumerUtil;

	@Mock
	private GameRepository gameRepository;

	@Mock
	private BlobTokenValidator blobTokenValidator;

	@Mock
	private MessageUtil messageUtil;

	private ObjectMapper mapper;

	@Mock
	private ObjectMapper mapperr;

	private String hostBlobStorage;

	private BlobTokenPayload blobTokenPayload;

	@Before
	public void setupMockt() {
		gameRepository = Mockito.mock(GameRepository.class);
		gameAssetRepository = Mockito.mock(GameAssetRepository.class);
		messageSource = Mockito.mock(MessageSource.class);
		blobStorageUtil = Mockito.mock(BlobStorageUtil.class);
		blobTokenValidator = Mockito.mock(BlobTokenValidator.class);
		mapperr = Mockito.mock(ObjectMapper.class);

		mapper = new ObjectMapper();
		hostBlobStorage = "http://netTest";
		blobTokenPayload = new BlobTokenPayload();
		blobTokenPayload.setNameAsset("Puzzle");
	}

	@Test
	public void callblobRestAPIWithSasNotAssets()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});

		GameAsset asset = listGameAsset.get(0);
		messageUtil.setMessageSource(messageSource);
		blobTokenValidator.setMessagesUtil(messageUtil);

		ReflectionTestUtils.setField(blobDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(blobDataProvider, "blobTokenValidator", blobTokenValidator);

		ApiResponse<BlobTokenDto> response = blobDataProvider.callblobRestAPIWithSas(blobTokenPayload);
		assertNull(response.getData());
		assertFalse(response.isSuccess());
		assertNotEquals(200, response.getStatusCode());
	}

	@Test
	public void callblobRestAPIWithSasException()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});

		Object object = mapper.readValue(new File(MOCK_RESPONSE_SAS_TOKEN), Object.class);

		GameAsset asset = listGameAsset.get(0);
		messageUtil.setMessageSource(messageSource);
		blobTokenValidator.setMessagesUtil(messageUtil);

		ReflectionTestUtils.setField(blobDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(blobDataProvider, "blobTokenValidator", blobTokenValidator);
		ReflectionTestUtils.setField(blobDataProvider, "hostBlobStorage", hostBlobStorage);
		ReflectionTestUtils.setField(blobDataProvider, "restConsumerUtil", restConsumerUtil);

		Mockito.when(gameAssetRepository.obtainAssetByName(blobTokenPayload.getNameAsset())).thenReturn(asset);

		Mockito.when(restConsumerUtil.restConsumer(null, URL_CONSUMER_SAS, HttpMethod.GET)).thenReturn(object);

		ApiResponse<BlobTokenDto> response = blobDataProvider.callblobRestAPIWithSas(blobTokenPayload);
		assertNull(response.getData());
		assertFalse(response.isSuccess());
		assertNotEquals(200, response.getStatusCode());
	}

	@Test
	public void callblobRestAPIWithDtoNullTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});

		Object object = mapper.readValue(new File(MOCK_RESPONSE_SAS_TOKEN), Object.class);

		GameAsset asset = listGameAsset.get(0);
		messageUtil.setMessageSource(messageSource);
		blobTokenValidator.setMessagesUtil(messageUtil);

		ReflectionTestUtils.setField(blobDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(blobDataProvider, "blobTokenValidator", blobTokenValidator);
		ReflectionTestUtils.setField(blobDataProvider, "hostBlobStorage", hostBlobStorage);
		ReflectionTestUtils.setField(blobDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(blobDataProvider, "mapper", mapperr);

		Mockito.when(gameAssetRepository.obtainAssetByName(blobTokenPayload.getNameAsset())).thenReturn(asset);

		Mockito.when(restConsumerUtil.restConsumer(null, URL_CONSUMER_SAS, HttpMethod.GET)).thenReturn(object);

		JsonNode jsonNode = mapper.convertValue(object, JsonNode.class);

		Mockito.when(mapperr.convertValue(object, JsonNode.class)).thenReturn(jsonNode);

		Mockito.when(mapperr.convertValue(jsonNode.path(GetaConstants.DATA), BlobTokenDto.class)).thenReturn(null);

		ApiResponse<BlobTokenDto> response = blobDataProvider.callblobRestAPIWithSas(blobTokenPayload);
		assertNull(response.getData());
		assertFalse(response.isSuccess());
		assertNotEquals(200, response.getStatusCode());
	}

	@Test
	public void callblobRestAPIWithResponseNotSuccesTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});

		Object object = mapper.readValue(new File(MOCK_RESPONSE_SAS_TOKEN), Object.class);

		GameAsset asset = listGameAsset.get(0);
		messageUtil.setMessageSource(messageSource);
		blobTokenValidator.setMessagesUtil(messageUtil);

		ReflectionTestUtils.setField(blobDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(blobDataProvider, "blobTokenValidator", blobTokenValidator);
		ReflectionTestUtils.setField(blobDataProvider, "hostBlobStorage", hostBlobStorage);
		ReflectionTestUtils.setField(blobDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(blobDataProvider, "mapper", mapperr);

		Mockito.when(gameAssetRepository.obtainAssetByName(blobTokenPayload.getNameAsset())).thenReturn(asset);

		Mockito.when(restConsumerUtil.restConsumer(null, URL_CONSUMER_SAS, HttpMethod.GET)).thenReturn(object);

		JsonNode jsonNode = mapper.convertValue(object, JsonNode.class);
		BlobTokenDto sasTokenDto = mapper.convertValue(jsonNode.path(GetaConstants.DATA), BlobTokenDto.class);
		sasTokenDto.setUrlToken(URL_ACCES);
		Mockito.when(mapperr.convertValue(object, JsonNode.class)).thenReturn(jsonNode);

		Mockito.when(mapperr.convertValue(jsonNode.path(GetaConstants.DATA), BlobTokenDto.class)).thenReturn(sasTokenDto);

		ApiResponse<BlobTokenDto> response = blobDataProvider.callblobRestAPIWithSas(blobTokenPayload);

		assertNotNull(response.getData());
		assertTrue(response.isSuccess());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void callblobRestAPIWithResponseSuccesTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});

		Object object = mapper.readValue(new File(MOCK_RESPONSE_SAS_TOKEN), Object.class);

		GameAsset asset = listGameAsset.get(0);
		messageUtil.setMessageSource(messageSource);
		blobTokenValidator.setMessagesUtil(messageUtil);

		ReflectionTestUtils.setField(blobDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(blobDataProvider, "blobTokenValidator", blobTokenValidator);
		ReflectionTestUtils.setField(blobDataProvider, "hostBlobStorage", hostBlobStorage);
		ReflectionTestUtils.setField(blobDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(blobDataProvider, "mapper", mapperr);

		Mockito.when(gameAssetRepository.obtainAssetByName(blobTokenPayload.getNameAsset())).thenReturn(asset);

		Mockito.when(restConsumerUtil.restConsumer(null, URL_CONSUMER_SAS, HttpMethod.GET)).thenReturn(object);

		JsonNode jsonNode = mapper.convertValue(object, JsonNode.class);
		BlobTokenDto sasTokenDto = mapper.convertValue(jsonNode.path(GetaConstants.DATA), BlobTokenDto.class);

		Mockito.when(mapperr.convertValue(object, JsonNode.class)).thenReturn(jsonNode);

		Mockito.when(mapperr.convertValue(jsonNode.path(GetaConstants.DATA), BlobTokenDto.class)).thenReturn(sasTokenDto);

		ApiResponse<BlobTokenDto> response = blobDataProvider.callblobRestAPIWithSas(blobTokenPayload);
		assertNull(response.getData());
		assertFalse(response.isSuccess());
		assertNotEquals(200, response.getStatusCode());
	}

	@Test
	public void uploadFileGameNotExist() throws JsonParseException, JsonMappingException, IOException {

		MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
		UploadFilePayload payload = new UploadFilePayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		Optional<Game> gameOptional = Optional.of(listGame.get(0));

		ReflectionTestUtils.setField(blobDataProvider, "blobTokenValidator", blobTokenValidator);
		ReflectionTestUtils.setField(blobDataProvider, "hostBlobStorage", hostBlobStorage);
		ReflectionTestUtils.setField(blobDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(blobDataProvider, "gameRepository", gameRepository);

		String nameFolder = payload.getGameId() + GetaConstants.NAME_FOLDER_ASSET;

		Mockito.when(gameRepository.findById(payload.getGameId())).thenReturn(gameOptional);
		Mockito
				.when(restConsumerUtil.multipartConsumerUploadFile(firstFile,
						hostBlobStorage + GetaConstants.URI_UPLOAD_BLOB_STORAGE, GetaConstants.NAME_BLOB_CONTAINER, nameFolder))
				.thenReturn(null);

		ApiResponse<UploadFileDto> apiResponse = blobDataProvider.uploadFileBlobStorage(firstFile, payload);
		assertNull(apiResponse.getData());
		assertFalse(apiResponse.isSuccess());
		assertNotEquals(200, apiResponse.getStatusCode());
	}
}
