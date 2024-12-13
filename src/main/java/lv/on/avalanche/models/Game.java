package lv.on.avalanche.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Game {
    private Long id;
    private Long player1;
    private Long player2;
    private Long nextMoveUser;
    private Boolean inProgress;
    private Long winner;
    private Double threshold;
    private Double bank;
    @Builder.Default
    private List<Bet> betList = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}