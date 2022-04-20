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
public class GamePayload {

	/**
	 * Refers to the gameId of a game
	 */
	@JsonProperty("GameId")
	private String gameId;

}
