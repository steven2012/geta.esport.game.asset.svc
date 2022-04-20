package co.io.geta.platform.crosscutting.persistence.game.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.io.geta.platform.crosscutting.persistence.game.asset.entity.GameUserUpdate;

public interface GameUserUpdateRepository extends CrudRepository<GameUserUpdate, String> {

	@Query(value = "SELECT cl FROM GameUserUpdate cl WHERE cl.createdAt IN (SELECT Max(cl.createdAt) FROM GameUserUpdate cl WHERE cl.gameId =:gameId AND cl.userId=:userId)")
	public GameUserUpdate getGameUser(@Param("gameId") String gameId, @Param("userId") String userId);

	@Query(value = "SELECT cl FROM GameUserUpdate cl WHERE  cl.userId=:userId")
	public List<GameUserUpdate> getGameListUser(@Param("userId") String userId);

}
