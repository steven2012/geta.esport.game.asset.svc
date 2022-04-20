package co.io.geta.platform.crosscutting.payload;

import java.util.List;

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
public class ApplicationVersionPayload {

	/**
	 * Refers to the gameId of a game
	 */
	private String gameId;

	/**
	 * Describe the name of the application version
	 */
	private String nameVersion;

	/**
	 * Refers to the assetId list
	 */
	private List<String> assetsId;
}
