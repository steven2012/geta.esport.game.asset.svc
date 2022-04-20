package co.io.geta.platform.crosscutting.persistence.game.asset.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.io.geta.platform.crosscutting.persistence.game.asset.entity.ApplicationVersion;

public interface AplicationVersionRepository extends CrudRepository<ApplicationVersion, String> {

	@Query(value = "SELECT apv FROM ApplicationVersion apv WHERE  apv.nameVersion=:nameVersion")
	public ApplicationVersion getAplicationVersionByName(@Param("nameVersion") String nameVersion);
}
