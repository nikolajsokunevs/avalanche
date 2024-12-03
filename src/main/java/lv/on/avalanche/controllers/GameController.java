package lv.on.avalanche.controllers;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.BetDTO;
import lv.on.avalanche.dto.GameDTO;
import lv.on.avalanche.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start-new-game")
    @PreAuthorize("#request.user1Id == authentication.principal.id || #request.user2Id == authentication.principal.id")
    public GameDTO startNewGame(@RequestBody GameDTO request) {
        log.info("Start a new game: " + request);
        return gameService.startNewGame(request);
    }

    @GetMapping("/game/{gameId}")
    public GameDTO getGame(@PathVariable Long gameId) {
        log.info("Get game: " + gameId);
        return gameService.getGame(gameId);
    }

    @PostMapping("/place-a-bet")
    @PreAuthorize("#request.userId == authentication.principal.id")
    public List<BetDTO> placeBet(@RequestBody BetDTO request) {
        return gameService.placeBet(request);
    }
}
