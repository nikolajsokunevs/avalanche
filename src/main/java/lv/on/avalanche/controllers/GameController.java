package lv.on.avalanche.controllers;

import lv.on.avalanche.dto.game.create.CreateGameRequest;
import lv.on.avalanche.dto.game.create.CreateGameResponse;
import lv.on.avalanche.dto.game.move.MoveRequest;
import lv.on.avalanche.dto.game.move.MoveResponse;
import lv.on.avalanche.dto.game.waitforgame.WaitForGameRequest;
import lv.on.avalanche.dto.game.waitforgame.WaitForGameResponse;
import lv.on.avalanche.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/wait-for-game")
    public WaitForGameResponse waitForGame(@RequestBody WaitForGameRequest request){
        WaitForGameResponse waitForGameResponse=new WaitForGameResponse(gameService.waitForGame(request));
        return waitForGameResponse;
    }

    @PostMapping("/create")
    public CreateGameResponse create(@RequestBody CreateGameRequest request) {
        return gameService.createGame(request);
    }

    @PostMapping("/move")
    public MoveResponse create(@RequestBody MoveRequest request) {
        return gameService.move(request);
    }
}
