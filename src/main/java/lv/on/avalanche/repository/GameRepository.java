package lv.on.avalanche.repository;

import lv.on.avalanche.entities.GameEntity;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameEntity, Long> {
}