package co.io.geta.platform.crosscutting.domain;

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
public class DeleteAssetDto {

	/**
	 * Boolean is true or false if assets was deleted
	 */
	@JsonProperty("assetIsDelete")
	private boolean assetIsDelete;
}
