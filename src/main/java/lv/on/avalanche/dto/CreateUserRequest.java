package lv.on.avalanche.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CreateUserRequest {
    @NotNull
    private String name;
    @NotNull
    private String userName;
    @NotNull
    private Long chatId;
    private String state;
    private Timestamp registeredAt;
}
