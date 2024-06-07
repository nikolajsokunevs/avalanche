package lv.on.avalanche.dto.game.move;

public record MoveResponse(Long gameId, Boolean playerWon, Integer counter) {
}
