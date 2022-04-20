package co.io.geta.platform.crosscutting.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.VerifyUserPayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class VerifyUserPayloadValidator {

	@Autowired
	private MessageUtil messageUtil;

	public void payLoadValidator(VerifyUserPayload payload) throws EBusinessException {

		if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
			throw new EBusinessException(messageUtil.getMessage(GetaConstants.USER_ID_NOT_EMPTY));
		}
	}

}
