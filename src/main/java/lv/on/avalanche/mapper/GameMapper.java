package lv.on.avalanche.mapper;

import lv.on.avalanche.dto.GameDTO;
import lv.on.avalanche.entities.GameEntity;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameDTO toDTO(GameEntity gameEntity) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(gameEntity.getId());
        gameDTO.setUser1Id(gameEntity.getUser1Id());
        gameDTO.setUser2Id(gameEntity.getUser2Id());
        gameDTO.setNextMoveUser(gameEntity.getNextMoveUser());
        gameDTO.setInProgress(gameEntity.getInProgress());
        gameDTO.setWinner(gameEntity.getWinner());
        gameDTO.setThreshold(gameEntity.getThreshold());
        gameDTO.setBank(gameEntity.getBank());
        gameDTO.setCreatedAt(gameEntity.getCreatedAt());
        gameDTO.setUpdatedAt(gameEntity.getUpdatedAt());
        return gameDTO;
    }

    public GameEntity toEntity(GameDTO gameDTO) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameDTO.getId());
        gameEntity.setUser1Id(gameDTO.getUser1Id());
        gameEntity.setUser2Id(gameDTO.getUser2Id());
        gameEntity.setNextMoveUser(gameDTO.getNextMoveUser());
        gameEntity.setInProgress(gameDTO.getInProgress());
        gameEntity.setWinner(gameDTO.getWinner());
        gameEntity.setThreshold(gameDTO.getThreshold());
        gameEntity.setBank(gameDTO.getBank());
        gameEntity.setCreatedAt(gameDTO.getCreatedAt());
        gameEntity.setUpdatedAt(gameDTO.getUpdatedAt());
        return gameEntity;
    }
}