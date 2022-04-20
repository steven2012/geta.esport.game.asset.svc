package co.io.geta.platform.modules.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.GameTokenDto;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.util.RestConsumerUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private RestConsumerUtil restConsumerUtil;

	@Autowired
	private ObjectMapper mapper;

	@Value(GetaConstants.HOST_KEY_STORAGE)
	private String host;

	/**
	 * This method verify credential to access Api wiht token, before consumer
	 * endpoints
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		JsonNode gameDto = toUserDetails(username);

		if (!gameDto.isMissingNode()) {
			GameTokenDto game = mapper.convertValue(gameDto, GameTokenDto.class);
			return new User(game.getUser(), game.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException(messageUtil.getMessage(GetaConstants.USER_NOT_FOUND));
		}
	}

	/**
	 * This method obtain Credential to access Api with token, this method consumer
	 * Api key store
	 * 
	 * @param userName, userName credential
	 * @return Jsonode whit credential
	 */
	private JsonNode toUserDetails(String userName) {
		// Consuming endpoint to get information Games
		Object jsonRules = restConsumerUtil.restConsumer(null, host + "?keyRule=Games", HttpMethod.GET);
		JsonNode jsonRule = mapper.convertValue(jsonRules, JsonNode.class);
		return jsonRule.path(GetaConstants.DATA).path(GetaConstants.KEY_RULES).path(GetaConstants.DETAILS)
				.findPath(userName);
	}

}
