package co.io.geta.platform.crosscutting.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageUtil {

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String key) {
		return messageSource.getMessage(key, null, Locale.getDefault());
	}

	public String getMessage(String key, String[] args) {
		return messageSource.getMessage(key, args, Locale.getDefault());
	}
}
