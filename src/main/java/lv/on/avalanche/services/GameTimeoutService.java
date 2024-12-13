package lv.on.avalanche.services;

import jakarta.annotation.PostConstruct;
import lv.on.avalanche.dto.GameResponse;
import lv.on.avalanche.mapper.GameMapper;
import lv.on.avalanche.models.Game;
import lv.on.avalanche.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.Duration.between;
import static java.util.concurrent.TimeUnit.SECONDS;
import static lv.on.avalanche.config.enums.WebSocketTopics.GAME_STATUS;

@Service
public class GameTimeoutService {

    private static final long CHECK_INTERVAL = 1;

    @Value("${game.moveTimeoutSeconds}")
    private long moveTimeoutSeconds;

    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    protected GameMapper gameMapper;
    @Autowired
    MessageService messageService;

    @PostConstruct
    public void startTimeoutChecker() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::checkForTimeouts, 0, CHECK_INTERVAL, SECONDS);
    }

    private void checkForTimeouts() {
        LocalDateTime now = LocalDateTime.now();

        for (Iterator<Map.Entry<Long, Game>> iterator = gameService.getGameSessions().entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Long, Game> entry = iterator.next();
            Game game = entry.getValue();

            if (game.getUpdatedAt() != null &&
                    between(game.getUpdatedAt(), now).toMillis() / 1000 > moveTimeoutSeconds &&
                    game.getInProgress()) {

                Long winner = (Objects.equals(game.getNextMoveUser(), game.getPlayer1())) ? game.getPlayer2() : game.getPlayer1();
                game.setWinner(winner);
                game.setInProgress(false);
                gameService.getGameSessions().put(entry.getKey(), game);
                gameRepository.save(gameMapper.toEntity(game));
                messageService.sendGameUpdate(game);
                iterator.remove();
            } else if (game.getUpdatedAt() != null &&
                    between(game.getUpdatedAt(), now).toMillis() / 1000 > moveTimeoutSeconds &&
                    !game.getInProgress()) {
                iterator.remove();
            }
        }
    }
}