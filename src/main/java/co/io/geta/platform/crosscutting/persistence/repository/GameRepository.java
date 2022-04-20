package co.io.geta.platform.crosscutting.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import co.io.geta.platform.crosscutting.persistence.entity.Game;

public interface GameRepository extends CrudRepository<Game, String> {

}
