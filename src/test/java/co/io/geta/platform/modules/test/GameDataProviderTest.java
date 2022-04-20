package co.io.geta.platform.modules.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.domain.ApplicationVersionDto;
import co.io.geta.platform.crosscutting.domain.GameApplicationByUserDto;
import co.io.geta.platform.crosscutting.domain.GameAssetDto;
import co.io.geta.platform.crosscutting.domain.GameDto;
import co.io.geta.platform.crosscutting.domain.GameUserUpdateDto;
import co.io.geta.platform.crosscutting.domain.SupportVersionDto;
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
import co.io.geta.platform.modules.dataproviders.jpa.JpaGameDataProvider;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GameDataProviderTest {

	public final static String PATH = "src" + File.separator + "" + File.separator + "test" + File.separator + ""
			+ File.separator + "resources" + File.separator + "" + File.separator + "MockGame.json";

	public final static String MOCK_GAME_ASSET = "src" + File.separator + "" + File.separator + "test" + File.separator
			+ "" + File.separator + "resources" + File.separator + "" + File.separator + "MockGameAsset.json";

	public final static String MOCK_USER_BY_DATA = "src" + File.separator + "" + File.separator + "test" + File.separator
			+ "" + File.separator + "resources" + File.separator + "" + File.separator + "MockUserDatabyId.json";

	public final static String MOCK_USER_UPDATE = "src" + File.separator + "" + File.separator + "test" + File.separator
			+ "" + File.separator + "resources" + File.separator + "" + File.separator + "MockGameUserUpdate.json";

	public final static String MOCK_SUPPORT_VERSION_ASSET = "src" + File.separator + "" + File.separator + "test"
			+ File.separator + "" + File.separator + "resources" + File.separator + "" + File.separator
			+ "MockSupportVersionAsset.json";

	public final static String MOCK_APLICATION_VERSION = "src" + File.separator + "" + File.separator + "test"
			+ File.separator + "" + File.separator + "resources" + File.separator + "" + File.separator
			+ "MockApplicationVersion.json";

	@InjectMocks
	private JpaGameDataProvider jpaGameDataProvider;

	@Mock
	private GameRepository gameRepository;

	@Mock
	private GameUserUpdateRepository gameUserRepository;

	@Mock
	private GameAssetRepository gameAssetRepository;

	@Mock
	private SupportVersionAssetRepository supportVersionAssetRepository;

	@Mock
	private AplicationVersionRepository aplicationVersionRepository;

	@Mock
	private RestConsumerUtil restConsumerUtil;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private MessageSource messageResource;

	@Mock
	private MessageUtil messageUtil;

	@Mock
	private DateTimeZoneUtil dateTimeZone;

	@Mock
	private VerifyUserPayloadValidator verifyUserPayloadValidator;

	@Mock
	private GameUserUpdateValidator gameUserUpdateValidator;

	@Mock
	private AplicationVersionByNameValidator aplicationVersionByNameValidator;

	@Mock
	private AplicationVersionValidator aplicationVersionValidator;

	private ObjectMapper mapper;

	private String host;

	@Before
	public void setupMockt() {
		gameRepository = Mockito.mock(GameRepository.class);
		gameUserRepository = Mockito.mock(GameUserUpdateRepository.class);
		gameAssetRepository = Mockito.mock(GameAssetRepository.class);
		supportVersionAssetRepository = Mockito.mock(SupportVersionAssetRepository.class);
		aplicationVersionRepository = Mockito.mock(AplicationVersionRepository.class);
		verifyUserPayloadValidator = Mockito.mock(VerifyUserPayloadValidator.class);
		aplicationVersionByNameValidator = Mockito.mock(AplicationVersionByNameValidator.class);
		aplicationVersionValidator = Mockito.mock(AplicationVersionValidator.class);
		gameUserUpdateValidator = Mockito.mock(GameUserUpdateValidator.class);
		restConsumerUtil = Mockito.mock(RestConsumerUtil.class);
		dateTimeZone = Mockito.mock(DateTimeZoneUtil.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		messageUtil = Mockito.mock(MessageUtil.class);
		mapper = new ObjectMapper();
		host = "https://genesys-security-svc-dev.azurewebsites.net/Security/GetUserDataById";
	}

	@Test
	public void listGamesSuccesTest() throws JsonParseException, JsonMappingException, IOException {

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		Mockito.when(gameRepository.findAll()).thenReturn(listGame);
		Mockito.when(modelMapper.map(listGame, new TypeToken<List<GameDto>>() {
		}.getType())).thenReturn(listGame);

		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);

		ApiResponse<List<GameDto>> response = jpaGameDataProvider.listGames();
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());

	}

	@Test
	public void listGameByGameIdSuccesTest() throws JsonParseException, JsonMappingException, IOException {

		GamePayload payload = new GamePayload();
		payload.setGameId("ASCD_GSHSCT");

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		Game game = null;

		for (Game gameItem : listGame) {
			if (gameItem.getGameId().equals(payload.getGameId())) {
				game = gameItem;
				break;
			}
		}

		Optional<Game> optional = Optional.of(game);
		GameDto gameDto = new GameDto();
		gameDto.setGameId("ASCD_GSHSCT");
		gameDto.setName("PuzzleAsset");

		Mockito.when(gameRepository.findById(payload.getGameId())).thenReturn(optional);
		Mockito.when(modelMapper.map(optional.get(), GameDto.class)).thenReturn(gameDto);

		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);

		ApiResponse<GameDto> response = jpaGameDataProvider.getGameById(payload);

		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void listGamesAssetSuccesTest() throws JsonParseException, JsonMappingException, IOException {

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});

		Mockito.when(gameAssetRepository.findAll()).thenReturn(listGameAsset);

		Mockito.when(modelMapper.map(listGameAsset, new TypeToken<List<GameAssetDto>>() {
		}.getType())).thenReturn(listGameAsset);

		ReflectionTestUtils.setField(jpaGameDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);

		ApiResponse<List<GameAssetDto>> response = jpaGameDataProvider.listGameAssets();
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void allGameUserUpdateEmptyTest() throws JsonParseException, JsonMappingException, IOException {

		List<GameUserUpdate> listGameUser = new ArrayList<>();

		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserRepository", gameUserRepository);

		Mockito.when(gameUserRepository.findAll()).thenReturn(listGameUser);

		ApiResponse<List<GameUserUpdateDto>> response = jpaGameDataProvider.allGameUserUpdate();
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void allGameUserUpdateSuccesTest() throws JsonParseException, JsonMappingException, IOException {

		List<GameUserUpdate> listGameUser = mapper.readValue(new File(MOCK_USER_UPDATE),
				new TypeReference<List<GameUserUpdate>>() {
				});

		List<SupportVersionAsset> listSupportversion = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserRepository", gameUserRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);

		Mockito.when(gameUserRepository.findAll()).thenReturn(listGameUser);

		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317"))
				.thenReturn(listSupportversion);

		Mockito.when(modelMapper.map(listGameUser, new TypeToken<List<GameUserUpdateDto>>() {
		}.getType())).thenReturn(listGameUser);

		ApiResponse<List<GameUserUpdateDto>> response = jpaGameDataProvider.allGameUserUpdate();
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void listApplicationVersionEmtyTest() throws JsonParseException, JsonMappingException, IOException {

		List<ApplicationVersion> listApplicationVersion = new ArrayList<>();

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);

		Mockito.when(aplicationVersionRepository.findAll()).thenReturn(listApplicationVersion);

		ApiResponse<List<ApplicationVersionDto>> response = jpaGameDataProvider.listAplicationVersion();
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void listApplicationVersionSuccesTest() throws JsonParseException, JsonMappingException, IOException {

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<SupportVersionAsset> listSupportversion = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);

		Mockito.when(aplicationVersionRepository.findAll()).thenReturn(listApplicationVersion);
		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317"))
				.thenReturn(listSupportversion);

		Mockito.when(modelMapper.map(listApplicationVersion, new TypeToken<List<ApplicationVersionDto>>() {
		}.getType())).thenReturn(listApplicationVersion);

		ApiResponse<List<ApplicationVersionDto>> response = jpaGameDataProvider.listAplicationVersion();
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void getGameUserUpdateByUserIdNotExistTest() throws JsonParseException, JsonMappingException, IOException {

		messageUtil.setMessageSource(messageResource);
		verifyUserPayloadValidator.setMessageUtil(messageUtil);

		VerifyUserPayload payload = new VerifyUserPayload();
		payload.setUserId("a736ccb6-2477-4945-b805-d8141bd50b90");

		Object jsonRules = (Object) mapper.readValue(new File(MOCK_USER_BY_DATA), Object.class);

		ReflectionTestUtils.setField(jpaGameDataProvider, "verifyUserPayloadValidator", verifyUserPayloadValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "restConsumerUtil", restConsumerUtil);

		Mockito.when(restConsumerUtil.restConsumer(payload, host, HttpMethod.POST)).thenReturn(jsonRules);
		ReflectionTestUtils.setField(jpaGameDataProvider, "mapper", mapper);

		ApiResponse<GameApplicationByUserDto> response = jpaGameDataProvider.getGameUserUpdateByUserId(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void getGameUserUpdateByUserIdEmptyTest() throws JsonParseException, JsonMappingException, IOException {

		messageUtil.setMessageSource(messageResource);
		verifyUserPayloadValidator.setMessageUtil(messageUtil);

		VerifyUserPayload payload = new VerifyUserPayload();
		payload.setUserId("a736ccb6-2477-4945-b805-d8141bd50b90");

		Object jsonRules = (Object) mapper.readValue(new File(MOCK_USER_BY_DATA), Object.class);

		List<GameUserUpdate> listGameUserUpdate = new ArrayList<>();

		ReflectionTestUtils.setField(jpaGameDataProvider, "verifyUserPayloadValidator", verifyUserPayloadValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "host", host);
		ReflectionTestUtils.setField(jpaGameDataProvider, "mapper", mapper);

		Mockito.when(restConsumerUtil.restConsumer(payload, host, HttpMethod.POST)).thenReturn(jsonRules);
		Mockito.when(gameUserRepository.getGameListUser(payload.getUserId())).thenReturn(listGameUserUpdate);

		ApiResponse<GameApplicationByUserDto> response = jpaGameDataProvider.getGameUserUpdateByUserId(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void getGameUserUpdateByUserSuccesTest() throws JsonParseException, JsonMappingException, IOException {

		messageUtil.setMessageSource(messageResource);
		verifyUserPayloadValidator.setMessageUtil(messageUtil);

		VerifyUserPayload payload = new VerifyUserPayload();
		payload.setUserId("a736ccb6-2477-4945-b805-d8141bd50b90");

		Object jsonRules = (Object) mapper.readValue(new File(MOCK_USER_BY_DATA), Object.class);

		// Obtain List Entity GameUserUpdate
		List<GameUserUpdate> listGameUserUpdate = mapper.readValue(new File(MOCK_USER_UPDATE),
				new TypeReference<List<GameUserUpdate>>() {
				});

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<SupportVersionAsset> listSupportversion = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		ReflectionTestUtils.setField(jpaGameDataProvider, "verifyUserPayloadValidator", verifyUserPayloadValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "host", host);
		ReflectionTestUtils.setField(jpaGameDataProvider, "mapper", mapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserRepository", gameUserRepository);

		Mockito.when(restConsumerUtil.restConsumer(payload, host, HttpMethod.POST)).thenReturn(jsonRules);
		Mockito.when(gameUserRepository.getGameListUser(payload.getUserId())).thenReturn(listGameUserUpdate);

		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317"))
				.thenReturn(listSupportversion);

		Mockito.when(modelMapper.map(listApplicationVersion, new TypeToken<List<ApplicationVersionDto>>() {
		}.getType())).thenReturn(listApplicationVersion);

		ApiResponse<GameApplicationByUserDto> response = jpaGameDataProvider.getGameUserUpdateByUserId(payload);
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void getAplicationVersionByNameNull() throws JsonParseException, JsonMappingException, IOException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		ApplicationVersionByNamePayload payload = new ApplicationVersionByNamePayload();
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionByNameValidator",
				aplicationVersionByNameValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);

		Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion())).thenReturn(null);
		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.getAplicationVersionByName(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void getAplicationVersionByEmptyName() throws JsonParseException, JsonMappingException, IOException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		ApplicationVersionByNamePayload payload = new ApplicationVersionByNamePayload();
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<SupportVersionAsset> listSupportversion = new ArrayList<>();

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionByNameValidator",
				aplicationVersionByNameValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);

		Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion()))
				.thenReturn(listApplicationVersion.get(0));

		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317"))
				.thenReturn(listSupportversion);

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.getAplicationVersionByName(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void getAplicationVersionByName() throws JsonParseException, JsonMappingException, IOException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		ApplicationVersionByNamePayload payload = new ApplicationVersionByNamePayload();
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<SupportVersionAsset> listSupportversion = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionByNameValidator",
				aplicationVersionByNameValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);

		Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion()))
				.thenReturn(listApplicationVersion.get(0));

		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317"))
				.thenReturn(listSupportversion);

		ModelMapper modelMap = new ModelMapper();
		ApplicationVersion ap = listApplicationVersion.get(0);
		ApplicationVersionDto dto = modelMap.map(ap, ApplicationVersionDto.class);
		Mockito.when(modelMapper.map(listApplicationVersion.get(0), ApplicationVersionDto.class)).thenReturn(dto);

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.getAplicationVersionByName(payload);
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void saveAplicationVersionGameNotExistTest() throws JsonParseException, JsonMappingException, IOException {

		List<String> listAssetId = new ArrayList<>();
		listAssetId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		ApplicationVersionPayload payload = new ApplicationVersionPayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		payload.setAssetsId(listAssetId);
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionValidator", aplicationVersionValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.saveAplicationVersion(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void saveAplicationVersionExceptionTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<String> listAssetId = new ArrayList<>();
		listAssetId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		ApplicationVersionPayload payload = new ApplicationVersionPayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		payload.setAssetsId(listAssetId);
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		Game game = listGame.get(0);

		ApplicationVersion aplication = listApplicationVersion.get(0);

		Optional<Game> optional = Optional.of(game);
		GameDto gameDto = new GameDto();
		gameDto.setGameId(game.getGameId());
		gameDto.setName(game.getName());

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionValidator", aplicationVersionValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "dateTimeZone", dateTimeZone);

		Mockito.when(gameRepository.findById("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40")).thenReturn(optional);
		Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion()))
				.thenReturn(aplication);

		Mockito.when(dateTimeZone.getUTCdatetimeAsDate()).thenReturn(new Date());

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.saveAplicationVersion(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void saveAplicationVersionExistTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<String> listAssetId = new ArrayList<>();
		listAssetId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		ApplicationVersionPayload payload = new ApplicationVersionPayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		payload.setAssetsId(listAssetId);
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		Game game = listGame.get(0);

		ApplicationVersion aplication = listApplicationVersion.get(0);

		Optional<Game> optional = Optional.of(game);
		GameDto gameDto = new GameDto();
		gameDto.setGameId(game.getGameId());
		gameDto.setName(game.getName());

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionValidator", aplicationVersionValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "dateTimeZone", dateTimeZone);

		Mockito.when(gameRepository.findById("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40")).thenReturn(optional);
		Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion()))
				.thenReturn(aplication);

		Mockito.when(dateTimeZone.getUTCdatetimeAsDate()).thenReturn(new Date());

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.saveAplicationVersion(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void saveAplicationVersionNotSavedTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<String> listAssetId = new ArrayList<>();
		listAssetId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		ApplicationVersionPayload payload = new ApplicationVersionPayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		payload.setAssetsId(listAssetId);
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		Game game = listGame.get(0);

		ApplicationVersion aplication = listApplicationVersion.get(0);

		Optional<Game> optional = Optional.of(game);
		GameDto gameDto = new GameDto();
		gameDto.setGameId(game.getGameId());
		gameDto.setName(game.getName());

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionValidator", aplicationVersionValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "dateTimeZone", dateTimeZone);

		Mockito.when(gameRepository.findById("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40")).thenReturn(optional);
		Mockito.when(dateTimeZone.getUTCdatetimeAsDate()).thenReturn(new Date());
		aplication.setGameId("");
		Mockito.when(aplicationVersionRepository.save(Mockito.any(ApplicationVersion.class))).thenReturn(aplication);
//		Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion()))
//				.thenReturn(aplication);

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.saveAplicationVersion(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void saveAplicationVersionSupportVersionEmptyTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<SupportVersionAsset> listSupportversion = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});
		List<String> listAssetId = new ArrayList<>();
		listAssetId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		ApplicationVersionPayload payload = new ApplicationVersionPayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		payload.setAssetsId(listAssetId);
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		Game game = listGame.get(0);

		ApplicationVersion aplication = listApplicationVersion.get(0);

		Optional<Game> optional = Optional.of(game);
		GameDto gameDto = new GameDto();
		gameDto.setGameId(game.getGameId());
		gameDto.setName(game.getName());

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionValidator", aplicationVersionValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "dateTimeZone", dateTimeZone);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);

		List<String> listAssetsId = new ArrayList<>();
		listAssetsId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		Mockito.when(gameRepository.findById("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40")).thenReturn(optional);
		Mockito.when(dateTimeZone.getUTCdatetimeAsDate()).thenReturn(new Date());
		Mockito.when(aplicationVersionRepository.save(Mockito.any(ApplicationVersion.class))).thenReturn(aplication);
		Mockito.when(gameAssetRepository.findAllById(listAssetsId)).thenReturn(listGameAsset);
		Mockito.when(supportVersionAssetRepository.saveAll(Mockito.anyList())).thenReturn(listSupportversion);

		listSupportversion.clear();
		Mockito.when(modelMapper.map(listSupportversion, new TypeToken<List<SupportVersionDto>>() {
		}.getType())).thenReturn(listSupportversion);

		// Mockito.when(aplicationVersionRepository.getAplicationVersionByName(payload.getNameVersion()))
//				.thenReturn(aplication);

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.saveAplicationVersion(payload);
		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());
	}

	@Test
	public void saveAplicationVersionTest()
			throws JsonParseException, JsonMappingException, IOException, EBusinessException {

		messageUtil.setMessageSource(messageResource);

		aplicationVersionByNameValidator.setMessagesUtil(messageUtil);

		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		List<SupportVersionAsset> listSupportversion = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		List<GameAsset> listGameAsset = mapper.readValue(new File(MOCK_GAME_ASSET), new TypeReference<List<GameAsset>>() {
		});
		List<String> listAssetId = new ArrayList<>();
		listAssetId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		ApplicationVersionPayload payload = new ApplicationVersionPayload();
		payload.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		payload.setAssetsId(listAssetId);
		payload.setNameVersion("puzzle_v0.0.0.40_standarda2v2_dev");

		Game game = listGame.get(0);
		ModelMapper modelmapp = new ModelMapper();
		ApplicationVersion aplication = listApplicationVersion.get(0);
		ApplicationVersionDto dto = modelmapp.map(aplication, ApplicationVersionDto.class);

		Optional<Game> optional = Optional.of(game);
		GameDto gameDto = new GameDto();
		gameDto.setGameId(game.getGameId());
		gameDto.setName(game.getName());

		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionValidator", aplicationVersionValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "dateTimeZone", dateTimeZone);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameAssetRepository", gameAssetRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);

		List<String> listAssetsId = new ArrayList<>();
		listAssetsId.add("F297D956-2C1C-4046-9E23-AFAA7743FA5D");

		Mockito.when(gameRepository.findById("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40")).thenReturn(optional);
		Mockito.when(dateTimeZone.getUTCdatetimeAsDate()).thenReturn(new Date());
		Mockito.when(aplicationVersionRepository.save(Mockito.any(ApplicationVersion.class))).thenReturn(aplication);
		Mockito.when(gameAssetRepository.findAllById(listAssetsId)).thenReturn(listGameAsset);
		Mockito.when(supportVersionAssetRepository.saveAll(Mockito.anyList())).thenReturn(listSupportversion);
		Mockito.when(modelMapper.map(listSupportversion, new TypeToken<List<SupportVersionDto>>() {
		}.getType())).thenReturn(listSupportversion);

		ApplicationVersion aplicV = aplicationVersionRepository.save(aplication);
		Mockito.when(modelMapper.map(aplicV, ApplicationVersionDto.class)).thenReturn(dto);

		ApiResponse<ApplicationVersionDto> response = jpaGameDataProvider.saveAplicationVersion(payload);
		assertNotNull(response.getData());
		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void saveGameUserUpdateNotExitsInBd() throws JsonParseException, JsonMappingException, IOException {

		GameUserUpdatePayload payloadg = new GameUserUpdatePayload();
		payloadg.setAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317");
		payloadg.setUserId("a736ccb6-2477-4945-b805-d8141bd50b90");

		Object jsonRules = (Object) mapper.readValue(new File(MOCK_USER_BY_DATA), Object.class);

		VerifyUserPayload payload = new VerifyUserPayload(payloadg.getUserId());

		// Obtain List Entity GameUserUpdate
		List<GameUserUpdate> listGameUserUpdate = mapper.readValue(new File(MOCK_USER_UPDATE),
				new TypeReference<List<GameUserUpdate>>() {
				});

		// Obtain List Entity GameUserUpdate
		List<SupportVersionAsset> listSupportVersionAssets = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		// Obtain List Entity GameUserUpdate
		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		// Obtain List Entity Game
		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		messageUtil.setMessageSource(messageResource);
		gameUserUpdateValidator.setMessagesUtil(messageUtil);

		ModelMapper modelMaper = new ModelMapper();
		ApplicationVersion application = listApplicationVersion.get(0);
		ApplicationVersionDto aplicationDto = modelMaper.map(application, ApplicationVersionDto.class);

		Game game = listGame.get(0);
		ApplicationVersion applicationVersion = listApplicationVersion.get(0);
		Optional<Game> optional = Optional.of(game);
		Optional<ApplicationVersion> optionalAplication = Optional.of(applicationVersion);
		GameDto gameDto = new GameDto();
		gameDto.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		gameDto.setName("Puzzle");

		GameUserUpdate entity = listGameUserUpdate.get(0);

		GameUserUpdateDto dto = new GameUserUpdateDto();
		dto.setApplicationVersion(aplicationDto);
		dto.setGameId(entity.getGameId());
		dto.setUserId(entity.getUserId());

		ReflectionTestUtils.setField(jpaGameDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "host", host);
		ReflectionTestUtils.setField(jpaGameDataProvider, "mapper", mapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserRepository", gameUserRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserUpdateValidator", gameUserUpdateValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);

		Mockito.when(aplicationVersionRepository.findById(payloadg.getAplicationVersionId()))
				.thenReturn(optionalAplication);
		Mockito.when(restConsumerUtil.restConsumer(payload, host, HttpMethod.POST)).thenReturn(jsonRules);
		Mockito.when(gameUserRepository.save(Mockito.any(GameUserUpdate.class))).thenReturn(entity);
		Mockito.when(modelMapper.map(entity, GameUserUpdateDto.class)).thenReturn(dto);

		Optional<ApplicationVersion> optionalAplicationV = aplicationVersionRepository
				.findById(payloadg.getAplicationVersionId());

		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId(optionalAplicationV.get().getApplicationVersionId()))
				.thenReturn(listSupportVersionAssets);
		Mockito.when(gameRepository.findById(optionalAplicationV.get().getGameId())).thenReturn(optional);

		ApiResponse<GameUserUpdateDto> response = jpaGameDataProvider.saveGameUserUpdate(payloadg);

		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());
	}

	@Test
	public void saveGameUserUpdateNoValidate() throws JsonParseException, JsonMappingException, IOException {

		GameUserUpdatePayload payloadg = new GameUserUpdatePayload();
		payloadg.setAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317");
		payloadg.setUserId("a736ccb6-2477-4945-b805-d8141bd50b90");

		messageUtil.setMessageSource(messageResource);
		gameUserUpdateValidator.setMessagesUtil(messageUtil);

		ReflectionTestUtils.setField(jpaGameDataProvider, "host", host);
		ReflectionTestUtils.setField(jpaGameDataProvider, "mapper", mapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);

		ApiResponse<GameUserUpdateDto> response = jpaGameDataProvider.saveGameUserUpdate(payloadg);

		assertNull(response.getData());
		assertNotEquals(200, response.getStatusCode());
		assertFalse(response.isSuccess());

	}

	@Test
	public void saveGameUserUpdateExistInBdTest() throws JsonParseException, JsonMappingException, IOException {

		GameUserUpdatePayload payloadg = new GameUserUpdatePayload();
		payloadg.setAplicationVersionId("2C1E0D09-31E7-4573-B67C-6F3C79133317");
		payloadg.setUserId("a736ccb6-2477-4945-b805-d8141bd50b90");

		Object jsonRules = (Object) mapper.readValue(new File(MOCK_USER_BY_DATA), Object.class);

		VerifyUserPayload payload = new VerifyUserPayload(payloadg.getUserId());

		// Obtain List Entity GameUserUpdate
		List<GameUserUpdate> listGameUserUpdate = mapper.readValue(new File(MOCK_USER_UPDATE),
				new TypeReference<List<GameUserUpdate>>() {
				});

		// Obtain List Entity GameUserUpdate
		List<SupportVersionAsset> listSupportVersionAssets = mapper.readValue(new File(MOCK_SUPPORT_VERSION_ASSET),
				new TypeReference<List<SupportVersionAsset>>() {
				});

		// Obtain List Entity GameUserUpdate
		List<ApplicationVersion> listApplicationVersion = mapper.readValue(new File(MOCK_APLICATION_VERSION),
				new TypeReference<List<ApplicationVersion>>() {
				});

		// Obtain List Entity Game
		List<Game> listGame = mapper.readValue(new File(PATH), new TypeReference<List<Game>>() {
		});

		messageUtil.setMessageSource(messageResource);
		gameUserUpdateValidator.setMessagesUtil(messageUtil);

		ModelMapper modelMaper = new ModelMapper();
		ApplicationVersion application = listApplicationVersion.get(0);
		ApplicationVersionDto aplicationDto = modelMaper.map(application, ApplicationVersionDto.class);

		Game game = listGame.get(0);
		ApplicationVersion applicationVersion = listApplicationVersion.get(0);
		Optional<Game> optional = Optional.of(game);
		Optional<ApplicationVersion> optionalAplication = Optional.of(applicationVersion);
		GameDto gameDto = new GameDto();
		gameDto.setGameId("0C4B4D9D-0351-45D7-87D5-F4ED377D3A40");
		gameDto.setName("Puzzle");

		GameUserUpdate entity = listGameUserUpdate.get(0);

		GameUserUpdateDto dto = new GameUserUpdateDto();
		dto.setApplicationVersion(aplicationDto);
		dto.setGameId(entity.getGameId());
		dto.setUserId(entity.getUserId());

		ReflectionTestUtils.setField(jpaGameDataProvider, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "host", host);
		ReflectionTestUtils.setField(jpaGameDataProvider, "mapper", mapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameRepository", gameRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserRepository", gameUserRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(jpaGameDataProvider, "gameUserUpdateValidator", gameUserUpdateValidator);
		ReflectionTestUtils.setField(jpaGameDataProvider, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(jpaGameDataProvider, "aplicationVersionRepository", aplicationVersionRepository);
		ReflectionTestUtils.setField(jpaGameDataProvider, "supportVersionAssetRepository", supportVersionAssetRepository);

		Mockito.when(aplicationVersionRepository.findById(payloadg.getAplicationVersionId()))
				.thenReturn(optionalAplication);
		Mockito.when(restConsumerUtil.restConsumer(payload, host, HttpMethod.POST)).thenReturn(jsonRules);
		Mockito.when(gameUserRepository.save(Mockito.any(GameUserUpdate.class))).thenReturn(entity);
		Mockito.when(modelMapper.map(entity, GameUserUpdateDto.class)).thenReturn(dto);

		Optional<ApplicationVersion> optionalAplicationV = aplicationVersionRepository
				.findById(payloadg.getAplicationVersionId());

		Mockito
				.when(supportVersionAssetRepository
						.listSupportVersionByAplicationVersionId(optionalAplicationV.get().getApplicationVersionId()))
				.thenReturn(listSupportVersionAssets);
		Mockito.when(gameRepository.findById(optionalAplicationV.get().getGameId())).thenReturn(optional);

		Mockito.when(gameUserRepository.getGameUser(optionalAplicationV.get().getGameId(), payload.getUserId()))
				.thenReturn(entity);

		ApiResponse<GameUserUpdateDto> response = jpaGameDataProvider.saveGameUserUpdate(payloadg);

		assertEquals(200, response.getStatusCode());
		assertTrue(response.isSuccess());

	}

	/////////////////////////////////// Test Mock
	/////////////////////////////////// failed//////////////////////////////////////
	@Test
	public void listGamesFailedTest() throws JsonParseException, JsonMappingException, IOException {

		ApiResponse<List<GameDto>> response = jpaGameDataProvider.listGames();
		assertNull(response.getData());
	}

	@Test
	public void listGamesAssetFailedTest() throws JsonParseException, JsonMappingException, IOException {

		ApiResponse<List<GameAssetDto>> response = jpaGameDataProvider.listGameAssets();
		assertNull(response.getData());
	}

	@Test
	public void listGameByGameIdFailedExceptionTest() throws JsonParseException, JsonMappingException, IOException {

		GamePayload payload = new GamePayload();
		payload.setGameId("ASCD_GSHSCT");

		ApiResponse<GameDto> response = jpaGameDataProvider.getGameById(payload);

		assertNull(response.getData());
	}

}
