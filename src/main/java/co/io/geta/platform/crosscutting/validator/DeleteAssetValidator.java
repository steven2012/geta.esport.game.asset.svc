package co.io.geta.platform.crosscutting.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.DeleteAssetPayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class DeleteAssetValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void deleteAssetValidator(DeleteAssetPayload payload) throws EBusinessException {

		if (payload == null) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.PAYLOAD_NOT_NULL));
		}

		if (payload.getNameAsset() == null || payload.getNameAsset().isEmpty()) {
			throw new EBusinessException(messagesUtil.getMessage(GetaConstants.NAME_ASSET_NOT_EMPTY));
		}
	}
}
