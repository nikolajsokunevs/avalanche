package lv.on.avalanche.services;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.BetDTO;
import lv.on.avalanche.dto.GameDTO;
import lv.on.avalanche.entities.BalanceEntity;
import lv.on.avalanche.entities.BetEntity;
import lv.on.avalanche.entities.GameEntity;
import lv.on.avalanche.exceptions.GameException;
import lv.on.avalanche.mapper.BetMapper;
import lv.on.avalanche.mapper.GameMapper;
import lv.on.avalanche.repository.BalanceRepository;
import lv.on.avalanche.repository.BetRepository;
import lv.on.avalanche.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameService {

    private static List<GameDTO> PENDING_GAMES = new ArrayList<>();
    private static List<GameDTO> ACTIVE_GAMES = new ArrayList<>();
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    protected GameMapper gameMapper;
    @Autowired
    protected BetMapper betMapper;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private BetRepository betRepository;

    public synchronized GameDTO startNewGame(GameDTO request) {
        log.info("Start new game!");
        Long userId = request.getUser1Id();
        Double threshold = request.getThreshold();
        Optional<GameDTO> gameDTOOptional = PENDING_GAMES.stream().filter(e -> e.getThreshold().equals(request.getThreshold())
                //&& !e.getUser1Id().equals(request.getUser1Id())
        ).findFirst();
        if (gameDTOOptional.isPresent()) {
            gameDTOOptional.get().setUser2Id(request.getUser1Id());
            gameRepository.save(gameMapper.toEntity(gameDTOOptional.get()));
            PENDING_GAMES.remove(gameDTOOptional.get());
            ACTIVE_GAMES.add(gameDTOOptional.get());
            return gameDTOOptional.get();
        }
        GameDTO gameDTO = createGame(userId, threshold);

        PENDING_GAMES.add(gameDTO);
        return gameDTO;
    }

    public GameDTO getGame(Long gameId) {
        Optional<GameDTO> gameDTO = ACTIVE_GAMES.stream().filter(e -> e.getId().equals(gameId)).findFirst();
        if (gameDTO.isPresent()) {
            return gameDTO.get();
        }
        gameDTO = PENDING_GAMES.stream().filter(e -> e.getId().equals(gameId)).findFirst();
        if (gameDTO.isPresent()) {
            return gameDTO.get();
        }
        return null;
    }

    private GameDTO createGame(Long user1, Double threshold) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setUser1Id(user1);
        gameEntity.setNextMoveUser(user1);
        gameEntity.setThreshold(threshold);
        gameEntity = gameRepository.save(gameEntity);
        return gameMapper.toDTO(gameEntity);
    }

    public List<BetDTO> placeBet(BetDTO request) {
        log.info("Place a bet!");
        BalanceEntity balanceEntity = balanceRepository.findByUserId(request.getUserId());
        log.info("Balance: "+balanceEntity.getBalance());
        if (balanceEntity == null) {
            throw new GameException(500, "There's no balance");
        }
        if (balanceEntity.getBalance() < request.getAmount()) {
            throw new GameException(500, "There are insufficient funds in the account");
        }
        if (!gameRepository.findById(request.getGameId()).isPresent()) {
            throw new GameException(500, "Game not found");
        }
        GameDTO game = getGame(request.getGameId());
        if (!game.getInProgress()) {
            throw new GameException(500, "Game was finished");
        }
        if (!game.getNextMoveUser().equals(request.getUserId())) {
            throw new GameException(500, "Wait for your move");
        }
        if (game.getThreshold() * 0.15 < request.getAmount()) {
            throw new GameException(500, "The bid should be between 0.01 and " + game.getThreshold() * 0.15);
        }
        game.setBank(game.getBank() + request.getAmount());
        balanceEntity.setBalance(balanceEntity.getBalance() - request.getAmount());
        if (game.getBank() > game.getThreshold()) {
            game.setInProgress(false);
            game.setWinner(request.getUserId());
        } else {
            game.setNextMoveUser(request.getUserId().equals(game.getUser1Id()) ? game.getUser2Id() : game.getUser1Id());
        }
        gameRepository.save(gameMapper.toEntity(game));
        balanceRepository.save(balanceEntity);
        betRepository.save(betMapper.toEntity(request));
        List<BetEntity> betEntities = betRepository.findByGameIdAndUserId(request.getGameId(), request.getUserId());
        return betEntities.stream()
                .map(betMapper::toDTO)
                .collect(Collectors.toList());
    }
}

