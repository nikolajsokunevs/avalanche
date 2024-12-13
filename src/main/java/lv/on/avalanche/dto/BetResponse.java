package lv.on.avalanche.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BetResponse {
    private Double amount;
    private LocalDateTime createdAt;

}
