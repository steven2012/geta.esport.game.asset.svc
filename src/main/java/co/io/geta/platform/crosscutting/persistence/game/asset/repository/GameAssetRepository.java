package co.io.geta.platform.crosscutting.persistence.game.asset.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.io.geta.platform.crosscutting.persistence.game.asset.entity.GameAsset;

public interface GameAssetRepository extends CrudRepository<GameAsset, String> {

	@Query(value = "SELECT cl FROM GameAsset cl WHERE cl.createdAt IN (SELECT Max(cl.createdAt) FROM GameAsset cl WHERE cl.nameAsset =:nameAsset)")
	public GameAsset obtainAssetByName(@Param("nameAsset") String nameAsset);
}
