package co.io.geta.platform.infrastructure.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import co.io.geta.platform.crosscutting.util.BlobStorageUtil;
import co.io.geta.platform.crosscutting.util.DateTimeZoneUtil;
import co.io.geta.platform.crosscutting.util.JwtTokenUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.util.RestConsumerUtil;

@SpringBootApplication(scanBasePackages = { "co.io.geta.platform.infrastructure", "co.io.geta.platform.modules",
		"co.io.geta.platform.crosscutting" })
public class GettaSportKeyGameAssetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GettaSportKeyGameAssetsApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.addBasenames("properties/manager-messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public MessageUtil messagesUtil() {
		return new MessageUtil();
	}

	@Bean
	public DateTimeZoneUtil dateTimeZone() {
		return new DateTimeZoneUtil();
	}

	@Bean
	public JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil();
	}

	@Bean
	public RestConsumerUtil restTemplateUtil() {
		return new RestConsumerUtil();
	}

	@Bean
	public BlobStorageUtil blobStorageUtil() {
		return new BlobStorageUtil();
	}
}
