package lv.on.avalanche.repository;

import lv.on.avalanche.entities.Balance;
import lv.on.avalanche.entities.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
}