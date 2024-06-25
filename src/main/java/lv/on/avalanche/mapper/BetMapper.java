package lv.on.avalanche.mapper;


import lv.on.avalanche.dto.BetDTO;
import lv.on.avalanche.entities.BetEntity;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {

    public BetDTO toDTO(BetEntity betEntity) {
        BetDTO betDTO = new BetDTO();
        betDTO.setId(betEntity.getId());
        betDTO.setGameId(betEntity.getGameId());
        betDTO.setUserId(betEntity.getUserId());
        betDTO.setAmount(betEntity.getAmount());
        betDTO.setCreatedAt(betEntity.getCreatedAt());
        betDTO.setUpdatedAt(betEntity.getUpdatedAt());
        return betDTO;
    }

    public BetEntity toEntity(BetDTO betDTO) {
        BetEntity betEntity = new BetEntity();
        betEntity.setId(betDTO.getId());
        betEntity.setGameId(betDTO.getGameId());
        betEntity.setUserId(betDTO.getUserId());
        betEntity.setAmount(betDTO.getAmount());
        betEntity.setCreatedAt(betDTO.getCreatedAt());
        betEntity.setUpdatedAt(betDTO.getUpdatedAt());
        return betEntity;
    }
}