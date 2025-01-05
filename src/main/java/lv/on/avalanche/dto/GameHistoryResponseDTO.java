package lv.on.avalanche.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GameHistoryResponseDTO {
    private LocalDateTime date;
    private Double threshold;
    private Double bank;
    private boolean isWinner;
}