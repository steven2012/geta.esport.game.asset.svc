package co.io.geta.platform.crosscutting.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class ApplicationVersionDto {

	/**
	 * Refers to the aplicationVersionId of a application
	 */
	private String applicationVersionId;

	/**
	 * Refers to the gameId of a game
	 */
	private String gameId;

	/**
	 * Describe the name of the application version
	 */
	private String nameVersion;

	/**
	 * Refers to date created register
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	private Date createdAt;

	/**
	 * Refers to date updated register
	 */
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	/**
	 * Refers to list of supportVersionAsset
	 */
	private List<SupportVersionDto> supportVersionAssets;

}
