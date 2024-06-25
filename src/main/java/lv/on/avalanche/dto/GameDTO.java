package lv.on.avalanche.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameDTO {
    private Long id;
    private Long user1Id;
    private Long user2Id;
    private Long nextMoveUser;
    private Boolean inProgress;
    private Long winner;
    private Double threshold;
    private Double bank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}