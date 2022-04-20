package co.io.geta.platform.crosscutting.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.GameUserUpdatePayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class GameUserUpdateValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void payLoadValidator(GameUserUpdatePayload payload) throws EBusinessException {

		if (payload.getAplicationVersionId() == null || payload.getAplicationVersionId().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.APLICATION_VERSION_ID_NOT_EMPTY));
		}

		if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.USER_ID_NOT_EMPTY));
		}
	}
}
