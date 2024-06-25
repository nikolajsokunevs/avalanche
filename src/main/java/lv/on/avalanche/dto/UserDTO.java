package lv.on.avalanche.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String userName;
    private Long chatId;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", state=" + state + '\'' +
                ", registeredAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt+
                '}';
    }
}
