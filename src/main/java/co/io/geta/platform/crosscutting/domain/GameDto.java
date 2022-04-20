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
public class GameDto {

	/**
	 * Refers to the gameId of a game
	 */
	@JsonProperty("GameId")
	private String gameId;

	/**
	 * Refers to the name of a game
	 */
	@JsonProperty("Name")
	private String name;

}
