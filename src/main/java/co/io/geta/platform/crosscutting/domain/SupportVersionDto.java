package co.io.geta.platform.crosscutting.domain;

import java.util.Date;

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
public class SupportVersionDto {

	/**
	 * Reference to supportVersionAssetId of table support asset
	 */
	private String supportVersionAssetId;

	/**
	 * Reference to Object GameAsset
	 */
	private GameAssetDto gameAsset;

	/**
	 * Date was created
	 */
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	private Date createdAt;

	/**
	 * Date was updated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

}
