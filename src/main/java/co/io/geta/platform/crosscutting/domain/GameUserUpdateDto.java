package co.io.geta.platform.crosscutting.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
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
public class GameUserUpdateDto {

	/**
	 * Refers to the gameUserUpdateId of user register
	 */
	private String gameUserUpdateId;

	/**
	 * Refers to the gameId of a game
	 */
	private String gameId;

	/**
	 * Refers to the Userid of the user
	 */
	private String userId;

	/**
	 * Refers to the applicationVersionId of the application that the user is
	 * associated with.
	 */
	private ApplicationVersionDto applicationVersion;

	/**
	 * It is true or false if the user is active in the application..
	 */
	private boolean isUpdate;

	/**
	 * Date was created
	 */
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	private Date createdAt;

	/**
	 * Date was updated
	 */
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	private Date updatedAt;
}
