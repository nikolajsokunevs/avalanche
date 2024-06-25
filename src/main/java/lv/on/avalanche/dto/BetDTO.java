package lv.on.avalanche.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BetDTO {

    private Long id;
    private Long gameId;
    private Long userId;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
