package co.io.geta.platform.crosscutting.payload;

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
public class ApplicationVersionByNamePayload {

	/**
	 * Describe the name of the application version
	 */
	private String nameVersion;

}
