package lv.on.avalanche.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Bet {
    private Long player;
    private Double amount;
    private LocalDateTime createdAt;
}
