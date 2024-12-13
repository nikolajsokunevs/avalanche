package lv.on.avalanche.mapper;

import lv.on.avalanche.entities.GameEntity;
import lv.on.avalanche.models.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameEntity toEntity(Game game) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(game.getId());
        gameEntity.setUser1Id(game.getPlayer1());
        gameEntity.setUser2Id(game.getPlayer2());
        gameEntity.setNextMoveUser(game.getNextMoveUser());
        gameEntity.setInProgress(game.getInProgress());
        gameEntity.setWinner(game.getWinner());
        gameEntity.setThreshold(game.getThreshold());
        gameEntity.setBank(game.getBank());
        gameEntity.setCreatedAt(game.getCreatedAt());
        gameEntity.setUpdatedAt(game.getUpdatedAt());
        return gameEntity;
    }
}