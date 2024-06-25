package lv.on.avalanche.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "balance")
public class BalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    private UserEntity userEntity;
    private Double balance;
}
