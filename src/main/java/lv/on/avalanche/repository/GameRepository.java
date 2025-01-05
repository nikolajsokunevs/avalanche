package lv.on.avalanche.repository;

import lv.on.avalanche.entities.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends CrudRepository<GameEntity, Long> {

    @Query("SELECT g FROM GameEntity g WHERE g.user1Id = :userId OR g.user2Id = :userId")
    List<GameEntity> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT g FROM GameEntity g WHERE g.inProgress = false AND (g.user1Id = :userId OR g.user2Id = :userId)")
    List<GameEntity> findByInProgressFalseAndUser1IdOrUser2Id(Long userId);
}