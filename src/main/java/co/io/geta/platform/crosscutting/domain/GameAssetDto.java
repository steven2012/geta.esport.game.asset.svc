package co.io.geta.platform.crosscutting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties
public class GameAssetDto {

	/**
	 * Refers to the gameAssetId of a asset
	 */
	private String gameAssetId;

	/**
	 * Refers to the gameId of a game
	 */
	private String gameId;

	/**
	 * Refers to the name file in blob storage
	 */
	private String nameAsset;

	/**
	 * Refers to the url in blob storage
	 */
	private String urlBlob;

	/**
	 * Date was created
	 */
	private String createdAt;

	/**
	 * Date was updated
	 */
	private String updatedAt;
}
