package lv.on.avalanche.dto;

import lombok.Data;

@Data
public class StartNewGameRequest {
    private Long player;
    private Double threshold;
}
