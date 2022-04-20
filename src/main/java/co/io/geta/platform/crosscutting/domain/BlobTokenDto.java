package co.io.geta.platform.crosscutting.domain;

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
public class BlobTokenDto {

	/**
	 * reference Url with token access
	 */
	private String urlToken;
}
