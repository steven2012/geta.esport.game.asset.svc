package co.io.geta.platform.modules.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import co.io.geta.platform.crosscutting.domain.JwtResponseDto;
import co.io.geta.platform.crosscutting.util.JwtTokenUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataproviders.jpa.JpaTokenDataProvider;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TokenDataProviderTest {

	@InjectMocks
	private JpaTokenDataProvider jpaTokenDataProvider;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@Mock
	private MessageUtil messageUtil;

	@Before
	public void setupMockt() {
		authenticationManager = Mockito.mock(AuthenticationManager.class);
		userDetailsService = Mockito.mock(UserDetailsService.class);
		jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
		messageUtil = Mockito.mock(MessageUtil.class);
	}

	@Test
	public void responseTokenSuccesTest() {

		Mockito.when(userDetailsService.loadUserByUsername("Puzzle"))
				.thenReturn(new User("Puzzle", "Password", new ArrayList<>()));

		Mockito.when(jwtTokenUtil.generateToken(new User("Puzzle", "Password", new ArrayList<>())))
				.thenReturn("ACDSG_SGYW");

		ReflectionTestUtils.setField(jpaTokenDataProvider, "jwtTokenUtil", jwtTokenUtil);
		ReflectionTestUtils.setField(jpaTokenDataProvider, "userDetailsService", userDetailsService);
		ReflectionTestUtils.setField(jpaTokenDataProvider, "authenticationManager", authenticationManager);

		ApiResponse<JwtResponseDto> response = jpaTokenDataProvider.responseToken("Puzzle", "Password");
		assertNotNull(response.getData());
		assertTrue(response.isSuccess());
		assertEquals(200, response.getStatusCode());
	}

}
