package lv.on.avalanche.mapper;

import lv.on.avalanche.dto.BetRequest;
import lv.on.avalanche.dto.BetResponse;
import lv.on.avalanche.entities.BetEntity;
import lv.on.avalanche.models.Bet;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BetMapper {

    public BetResponse toBetResponse(Bet bet) {
        BetResponse betResponse = new BetResponse();
        betResponse.setAmount(bet.getAmount());
        betResponse.setCreatedAt(bet.getCreatedAt());
        return betResponse;
    }

    public BetEntity toEntity(BetRequest betRequest) {
        BetEntity betEntity = new BetEntity();
        betEntity.setUserId(betRequest.getPlayer());
        betEntity.setAmount(betRequest.getAmount());
        betEntity.setCreatedAt(LocalDateTime.now());
        return betEntity;
    }

    public Bet toBet(BetRequest betRequest) {
        Bet bet = new Bet();
        bet.setPlayer(betRequest.getPlayer());
        bet.setAmount(betRequest.getAmount());
        bet.setCreatedAt(LocalDateTime.now());
        return bet;
    }


}