package co.io.geta.platform.crosscutting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class JwtResponseDto {

	/**
	 * reference token to access to Api
	 */
	private final String jwttoken;
}
