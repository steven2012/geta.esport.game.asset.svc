package co.io.geta.platform.infrastructure.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring boot configuration for Datasource runtime
 *
 * @author geta
 * @version 1.0
 * @since 2020-04-21
 */

@Configuration
public class MapperConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
