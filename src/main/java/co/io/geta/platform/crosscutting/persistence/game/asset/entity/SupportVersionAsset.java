package co.io.geta.platform.crosscutting.persistence.game.asset.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "SupportVersionAsset", schema = "game")
public class SupportVersionAsset {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "SupportVersionAssetId")
	private String supportVersionAssetId;

	@Basic(optional = false)
	@Column(name = "CreatedAt")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	private Date createdAt;

	@Basic(optional = false)
	@Column(name = "UpdatedAt")
	@JsonFormat(pattern = GetaConstants.PATTERN_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ApplicationVersionId")
	private ApplicationVersion applicationVersion;

	@ManyToOne(optional = false)
	@JoinColumn(name = "GameAssetId")
	private GameAsset gameAsset;

}
