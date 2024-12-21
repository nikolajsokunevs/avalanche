package lv.on.avalanche.services;

import lv.on.avalanche.dto.BetResponse;
import lv.on.avalanche.dto.GameResponse;
import lv.on.avalanche.mapper.BetMapper;
import lv.on.avalanche.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lv.on.avalanche.config.enums.WebSocketTopics.GAME_STATUS;

@Service
public class MessageService {

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private BetMapper betMapper;

    @Autowired
    public MessageService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String topic, Object message) {
        messagingTemplate.convertAndSend(topic, message);
    }

    public void sendGameUpdate(Game game) {
        Long player1 = game.getPlayer1();
        Long player2 = game.getPlayer2();
        messagingTemplate.convertAndSend(GAME_STATUS.get()+player1, prepareResponseForPlayer(game, player1));
        messagingTemplate.convertAndSend(GAME_STATUS.get()+player2, prepareResponseForPlayer(game, player2));
    }

    private GameResponse prepareResponseForPlayer(Game game, Long player) {
        Boolean gameInProgress = game.getInProgress();
        Boolean yourMove = gameInProgress && Objects.equals(player, game.getNextMoveUser());
        Boolean win = !gameInProgress && Objects.equals(player, game.getWinner());
        Double winAmount = win ? game.getBank() : null;
        List<BetResponse> betResponseList = win ? game.getBetList().stream().filter(e -> player.equals(e.getPlayer())).map(e -> betMapper.toBetResponse(e)).toList() : new ArrayList<>();
        return GameResponse.builder()
                .gameId(game.getId())
                .yourMove(yourMove)
                .inProgress(gameInProgress)
                .win(win)
                .threshold(game.getThreshold())
                .winAmount(winAmount)
                .betResponseList(betResponseList)
                .build();
    }
}