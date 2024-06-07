package lv.on.avalanche.services;

import lv.on.avalanche.dto.game.create.CreateGameRequest;
import lv.on.avalanche.dto.game.create.CreateGameResponse;
import lv.on.avalanche.dto.game.move.MoveRequest;
import lv.on.avalanche.dto.game.move.MoveResponse;
import lv.on.avalanche.entities.Balance;
import lv.on.avalanche.entities.Game;
import lv.on.avalanche.exceptions.GameException;
import lv.on.avalanche.repository.BalanceRepository;
import lv.on.avalanche.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    public CreateGameResponse createGame(CreateGameRequest request) {
        Game game = new Game();
        game.setUser1(request.user1());
        game.setUser2(request.user2());
        game.setNextMoveUser(request.user1());
        game.setThreshold(request.threshold());
        game.setRegisteredAt(Timestamp.valueOf(LocalDateTime.now()));
        game = gameRepository.save(game);
        return new CreateGameResponse(game.getId(), game.getNextMoveUser());
    }

    public MoveResponse move(MoveRequest request) {
        Balance balance = balanceRepository.findByUserChatId(request.userId());
        if(balance==null){
            throw new GameException(500, "There's no balance");
        }
        if (balance.getBalance() < request.bid()) {
            throw new GameException(500, "There are insufficient funds in the account");
        }
        if (!gameRepository.findById(request.gameId()).isPresent()) {
            throw new GameException(500, "Game not found");
        }
        Game game = gameRepository.findById(request.gameId()).get();
        if (!game.getInProgress()) {
            throw new GameException(500, "Game was finished");
        }
        if (!game.getNextMoveUser().equals(request.userId())) {
            throw new GameException(500, "Wait for your move");
        }
        if (game.getThreshold()*0.15< request.bid()){
            throw new GameException(500, "The bid should be between 0.01 and "+game.getThreshold()*0.15);
        }
        game.setBank(game.getBank() + request.bid());
        balance.setBalance(balance.getBalance()- request.bid());
        if (game.getBank() > game.getThreshold()) {
            game.setInProgress(false);
            game.setWinner(request.userId());
        } else {
            game.setNextMoveUser(request.userId().equals(game.getUser1()) ? game.getUser2() : game.getUser1());
        }
        game.setCounter(game.getCounter()+1);
        gameRepository.save(game);
        balanceRepository.save(balance);
        return new MoveResponse(request.gameId(), game.getWinner() != null ? true : false, game.getCounter());
    }
}

