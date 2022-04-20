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
public class GameUserUpdatePayload {

	/**
	 * Refers to the Userid of the user
	 */
	private String userId;

	/**
	 * Refers to the aplicationVersionId of the application
	 */
	private String aplicationVersionId;
}
