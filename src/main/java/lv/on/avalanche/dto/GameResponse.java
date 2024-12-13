package lv.on.avalanche.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameResponse {
    private Boolean yourMove;
    private Boolean inProgress;
    private Boolean win;
    private Double winAmount;
    private List<BetResponse> betResponseList;
}
