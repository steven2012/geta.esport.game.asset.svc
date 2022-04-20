package co.io.geta.platform.crosscutting.persistence.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "GAME", schema = "core")
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "GameId")
	private String gameId;

	@Basic(optional = false)
	@Column(name = "Name")
	private String name;

	@Basic(optional = true)
	@Column(name = "Description")
	private String description;

	@Basic(optional = false)
	@Column(name = "Url")
	private String url;

	@Basic(optional = true)
	@Column(name = "ProviderTitleId")
	private String providerTitleId;

	@Basic(optional = true)
	@Column(name = "DeeplinkUri")
	private String deeplinkUri;

	@Basic(optional = true)
	@Column(name = "CreatedAt")
	private String createdAt;

	@Basic(optional = true)
	@Column(name = "UpdatedAt")
	private String updatedAt;

}
