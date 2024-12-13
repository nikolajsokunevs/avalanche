package lv.on.avalanche.controllers;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.BetRequest;
import lv.on.avalanche.dto.StartNewGameRequest;
import lv.on.avalanche.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start-new-game")
    //@PreAuthorize("#request.player == authentication.principal.id")
    public void startNewGame(@RequestBody StartNewGameRequest request) throws Exception {
        log.info("Start a new game: " + request);
        gameService.startNewGame(request.getPlayer(), request.getThreshold());
    }

    @PostMapping("/place-a-bet")
    //@PreAuthorize("#request.userId == authentication.principal.id")
    public void placeBet(@RequestBody BetRequest request) {
        gameService.placeBet(request);
    }
}
