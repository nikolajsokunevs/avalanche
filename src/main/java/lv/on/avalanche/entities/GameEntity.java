package lv.on.avalanche.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "games")
public class GameEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private Long user1Id;
    private Long user2Id;
    @Column(nullable = false)
    private Long nextMoveUser;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean inProgress = true;
    @Column(nullable = true, columnDefinition = "varchar(255) default null")
    private Long winner = null;
    @Column(nullable = false)
    private Double threshold;
    @Column(nullable = false, columnDefinition = "double default 0")
    private Double bank = 0.0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bank == null) {
            bank = 0.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (bank == null) {
            bank = 0.0;
        }
    }
}
