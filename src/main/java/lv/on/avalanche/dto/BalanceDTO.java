package lv.on.avalanche.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceDTO {
    private Long id;
    private Long userId;
    private Double balance;
    @Override
    public String toString() {
        return "BalanceDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}