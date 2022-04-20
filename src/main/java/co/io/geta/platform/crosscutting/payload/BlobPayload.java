package co.io.geta.platform.crosscutting.payload;

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
public class BlobPayload {

	/**
	 * Refers to the name of the storage blob container
	 */
	@JsonProperty("NameContainer")
	private String nameContainer;

	/**
	 * Refers to the name of the file in blob container
	 */
	@JsonProperty("PahtFile")
	private String pahtFile;
}
