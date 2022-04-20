package co.io.geta.platform.crosscutting.persistence.game.asset.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "GameAsset", schema = "game")
public class GameAsset {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "GameAssetId")
	private String gameAssetId;

	@Basic(optional = false)
	@Column(name = "GameId")
	private String gameId;

	@Basic(optional = false)
	@Column(name = "NameAsset")
	private String nameAsset;

	@Basic(optional = false)
	@Column(name = "UrlBlob")
	private String urlBlob;

	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	@Basic(optional = true)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	@Basic(optional = true)
	@Column(name = "UpdatedAt")
	private Date updatedAt;

	@OneToMany(mappedBy = "gameAsset")
	private List<SupportVersionAsset> supportVersionAssets;
}
