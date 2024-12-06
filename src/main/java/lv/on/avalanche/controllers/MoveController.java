package lv.on.avalanche.controllers;

import lv.on.avalanche.dto.BetDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MoveController {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, GameSession> gameSessions = new HashMap<>();

    public MoveController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/start-game")
    public void startGame(String playerId) {
        GameSession gameSession = new GameSession(playerId);
        gameSessions.put(playerId, gameSession);
        messagingTemplate.convertAndSend("/topic/game/" + playerId, "Game Started!");
    }

    @MessageMapping("/make-move")
    public void makeMove(Move move) {
        GameSession gameSession = gameSessions.get(move.getPlayerId());

        if (gameSession != null && gameSession.isCurrentPlayer(move.getPlayer())) {
            gameSession.makeMove(move.getX(), move.getY(), move.getPlayer());
            messagingTemplate.convertAndSend("/topic/game/" + gameSession.getOpponent(move.getPlayer()), gameSession.getGameState());
        }
    }

    private static class GameSession {
        private final String player1;
        private String player2;
        private char[][] board = new char[3][3];
        private char currentPlayer = 'X';

        public GameSession(String player1) {
            this.player1 = player1;
        }

        public void makeMove(int x, int y, char player) {
            if (board[x][y] == '\0') {
                board[x][y] = player;
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }

        public String getGameState() {
            // Возвращаем текущее состояние игры в виде строки
            StringBuilder state = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    state.append(board[i][j] == '\0' ? '.' : board[i][j]);
                }
            }
            return state.toString();
        }

        public boolean isCurrentPlayer(char player) {
            return player == currentPlayer;
        }

        public String getOpponent(char player) {
            return player == 'X' ? player2 : player1;
        }
    }

    // Класс для хода
    private static class Move {
        private String playerId;
        private char player;
        private int x, y;

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public char getPlayer() {
            return player;
        }

        public void setPlayer(char player) {
            this.player = player;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}


