package lv.on.avalanche.services;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.BetRequest;
import lv.on.avalanche.entities.BalanceEntity;
import lv.on.avalanche.exceptions.GameException;
import lv.on.avalanche.mapper.BetMapper;
import lv.on.avalanche.mapper.GameMapper;
import lv.on.avalanche.models.Game;
import lv.on.avalanche.repository.BalanceRepository;
import lv.on.avalanche.repository.BetRepository;
import lv.on.avalanche.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class GameService {

    private final static Map<Long, Game> gameSessions = new HashMap<>();
    private final static Map<Double, Queue<Long>> playersQueue = new HashMap<>();

    @Autowired
    MessageService messageService;
    @Autowired
    protected GameMapper gameMapper;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private BetRepository betRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private BetMapper betMapper;

    static {
        List<Double> boardSizes = Arrays.asList(2.0, 5.0, 10.0);
        for (Double size : boardSizes) {
            playersQueue.put(size, new LinkedList<>());
        }
    }

    public synchronized void startNewGame(Long player1, Double threshold) throws Exception {
        log.info("Start new game!");
        if (!playersQueue.containsKey(threshold)) {
            throw new GameException(500, "There's no such threshold");
        }
        if (playersQueue.get(threshold).contains(player1)) {
            throw new GameException(500, "User already in a queue");
        }
        Long player2 = playersQueue.get(threshold).poll();
        if (player2 == null) {
            playersQueue.get(threshold).add(player1);
        } else {
            Game game = Game.builder()
                    .player1(player1)
                    .player2(player2)
                    .nextMoveUser(player1)
                    .threshold(threshold)
                    .inProgress(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build();
            gameSessions.put(player1, game);
            gameSessions.put(player2, game);
            messageService.sendGameUpdate(game);
            gameRepository.save(gameMapper.toEntity(game));
        }
    }

    public void placeBet(BetRequest request) {
        log.info("Place a bet!");
        BalanceEntity balanceEntity = balanceRepository.findByUserId(request.getPlayer());
        log.info("Balance: " + balanceEntity.getBalance());
        if (balanceEntity == null) {
            throw new GameException(500, "There's no balance");
        }
        if (balanceEntity.getBalance() < request.getAmount()) {
            throw new GameException(500, "There are insufficient funds in the account");
        }
        if (!gameSessions.containsKey(request.getPlayer())) {
            throw new GameException(500, "Game not found");
        }
        Game game = gameSessions.get(request.getPlayer());
        if (!game.getInProgress()) {
            throw new GameException(500, "Game was finished");
        }
        if (!game.getNextMoveUser().equals(request.getPlayer())) {
            throw new GameException(500, "Wait for your move");
        }
        if (game.getThreshold() * 0.15 < request.getAmount()) {
            throw new GameException(500, "The bid should be between 0.01 and " + game.getThreshold() * 0.15);
        }
        balanceEntity.setBalance(balanceEntity.getBalance() - request.getAmount());
        game.setBank(game.getBank() + request.getAmount());
        game.getBetList().add(betMapper.toBet(request));
        Long nextMovePlayer = request.getPlayer().equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
        if (game.getBank() >= game.getThreshold()) {
            game.setInProgress(false);
            game.setWinner(request.getPlayer());
            balanceEntity.setBalance(balanceEntity.getBalance() + game.getBank());
        } else {
            game.setNextMoveUser(nextMovePlayer);
        }
        betRepository.save(betMapper.toEntity(request));
        gameRepository.save(gameMapper.toEntity(game));
        balanceRepository.save(balanceEntity);
        messageService.sendGameUpdate(game);
    }

    public Map<Long, Game> getGameSessions() {
        return gameSessions;
    }
}
