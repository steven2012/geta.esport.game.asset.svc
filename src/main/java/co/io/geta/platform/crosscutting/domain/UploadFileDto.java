package co.io.geta.platform.crosscutting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties
public class UploadFileDto {

	/**
	 * Refers to the gameId of a game
	 */
	@JsonProperty("gameId")
	private String gameId;

	/**
	 * Refers to the name file in blob storage
	 */
	@JsonProperty("nameFile")
	private String nameFile;

	/**
	 * Refers to the url in blob storage
	 */
	@JsonProperty("uriBlobFile")
	private String urlBlob;
}
