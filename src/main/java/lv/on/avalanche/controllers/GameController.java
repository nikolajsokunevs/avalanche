package lv.on.avalanche.controllers;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.game.create.CreateGameRequest;
import lv.on.avalanche.dto.game.create.CreateGameResponse;
import lv.on.avalanche.dto.game.move.MoveRequest;
import lv.on.avalanche.dto.game.move.MoveResponse;
import lv.on.avalanche.dto.game.waitforgame.WaitForGameRequest;
import lv.on.avalanche.dto.game.waitforgame.WaitForGameResponse;
import lv.on.avalanche.entities.Game;
import lv.on.avalanche.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/wait-for-game")
    public WaitForGameResponse waitForGame(@RequestBody WaitForGameRequest request){
        log.info("Wait for game: "+request);
        WaitForGameResponse waitForGameResponse=new WaitForGameResponse(gameService.waitForGame(request));
        return waitForGameResponse;
    }

    @GetMapping("/game/{gameId}")
    public Game getAGame(@PathVariable Long gameId){
        log.info("Get game: "+gameId);
        return gameService.getGame(gameId);
    }

    @PostMapping("/move")
    public MoveResponse doAMove(@RequestBody MoveRequest request) {
        return gameService.move(request);
    }
}
