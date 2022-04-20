package co.io.geta.platform.crosscutting.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.ApplicationVersionByNamePayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class AplicationVersionByNameValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void payLoadValidator(ApplicationVersionByNamePayload payload) throws EBusinessException {

		if (payload.getNameVersion().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.NAME_VERSION_APP_NOT_EMPTY));
		}
	}
}
