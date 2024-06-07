package lv.on.avalanche.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;
    private Double balance;
}
