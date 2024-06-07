package lv.on.avalanche.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String userName;

    @Column(unique = true, nullable = false)
    private Long chatId;

    private String state;
    private Timestamp registeredAt;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + id +
                ", name='" + name + '\'' +
                ", state=" + state + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}