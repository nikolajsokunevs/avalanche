package lv.on.avalanche.repository;

import lv.on.avalanche.entities.BetEntity;
import lv.on.avalanche.entities.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BetRepository extends CrudRepository<BetEntity, Long> {

    @Query("SELECT b FROM BetEntity b WHERE b.gameId = :gameId AND b.userId = :userId")
    List<BetEntity> findByGameIdAndUserId(@Param("gameId") Long gameId, @Param("userId") Long userId);
}
