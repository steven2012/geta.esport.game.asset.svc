package co.io.geta.platform.crosscutting.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionPayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class AplicationVersionValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void payLoadValidator(ApplicationVersionPayload payload) throws EBusinessException {

		if (payload.getGameId() == null || payload.getGameId().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.GAME_ID_NOT_EMPTY));
		}

		if (payload.getNameVersion() == null || payload.getNameVersion().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.NAME_VERSION_APP_NOT_EMPTY));
		}

		if (payload.getAssetsId().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.LIST_CANNOT_EMPTY));
		}
	}

}
