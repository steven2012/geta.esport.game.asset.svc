package co.io.geta.platform.crosscutting.persistence.game.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.io.geta.platform.crosscutting.persistence.game.asset.entity.SupportVersionAsset;

public interface SupportVersionAssetRepository extends CrudRepository<SupportVersionAsset, String> {

	@Query("SELECT sv FROM SupportVersionAsset sv WHERE sv.applicationVersion.applicationVersionId= :applicationVersionId")
	public List<SupportVersionAsset> listSupportVersionByAplicationVersionId(
			@Param("applicationVersionId") String aplicationVersionId);

	@Query("SELECT sv FROM SupportVersionAsset sv WHERE sv.gameAsset.gameAssetId= :gameAssetId")
	public List<SupportVersionAsset> listSupportVersionByGameAssetId(@Param("gameAssetId") String gameAssetId);
}
