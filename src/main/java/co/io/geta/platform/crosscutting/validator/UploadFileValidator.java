package co.io.geta.platform.crosscutting.validator;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.UploadFilePayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class UploadFileValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void payLoadValidator(MultipartFile file, UploadFilePayload payload) throws EBusinessException, IOException {

		if (payload.getGameId() == null || payload.getGameId().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.GAME_ID_NOT_EMPTY));
		}

		if (file == null) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.MULTIPART_NOT_EMPTY));
		}

		if (file.getInputStream().read() <= 0) {
			file.getInputStream().close();
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.MULTIPART_NOT_EMPTY));
		}

	}
}
