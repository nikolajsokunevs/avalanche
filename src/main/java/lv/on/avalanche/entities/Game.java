package lv.on.avalanche.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "games")
public class Game {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private Long user1;
    @Column(nullable = false)
    private Long user2;
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
    private Integer counter = 0;
    private Timestamp registeredAt;
}
