package co.io.geta.platform.crosscutting.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
public class GameTokenDto {

	/**
	 * Refers to username as part of authentication credentials
	 */
	@JsonProperty("User")
	private String user;

	/**
	 * Refers to Password as part of authentication credentials
	 */
	@JsonProperty("Password")
	private String password;

}
