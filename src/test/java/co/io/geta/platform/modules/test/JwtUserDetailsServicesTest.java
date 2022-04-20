package co.io.geta.platform.modules.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.util.RestConsumerUtil;
import co.io.geta.platform.modules.services.JwtUserDetailsService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JwtUserDetailsServicesTest {

	public final static String MOCK_GAME_KEY_STORAGE = "src" + File.separator + "" + File.separator + "test"
			+ File.separator + "" + File.separator + "resources" + File.separator + "" + File.separator
			+ "MockKeyStorageGames.json";

	@InjectMocks
	private JwtUserDetailsService detailsService;

	@Mock
	private MessageSource messageSource;

	@Mock
	private RestConsumerUtil restConsumerUtil;

	private ObjectMapper mapper;

	private String host;

	@Before
	public void setupMockt() {
		restConsumerUtil = Mockito.mock(RestConsumerUtil.class);
		messageSource = Mockito.mock(MessageSource.class);
		mapper = new ObjectMapper();
		host = "https://hostUri";
	}

	@Test
	public void userDetailTest() throws JsonParseException, JsonMappingException, IOException {

		Object gameStorage = mapper.readValue(new File(MOCK_GAME_KEY_STORAGE), Object.class);

		Mockito.when(restConsumerUtil.restConsumer(null, host + "?keyRule=Games", HttpMethod.GET)).thenReturn(gameStorage);

		ReflectionTestUtils.setField(detailsService, "restConsumerUtil", restConsumerUtil);
		ReflectionTestUtils.setField(detailsService, "host", host);
		ReflectionTestUtils.setField(detailsService, "mapper", mapper);

		UserDetails user = detailsService.loadUserByUsername("Puzzle");
		assertNotNull(user.getUsername());
		assertNotNull(user.getPassword());

	}

}
