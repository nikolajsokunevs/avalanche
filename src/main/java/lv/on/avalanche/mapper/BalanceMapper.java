package lv.on.avalanche.mapper;

import lv.on.avalanche.dto.BalanceDTO;
import lv.on.avalanche.entities.BalanceEntity;
import lv.on.avalanche.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {

    public BalanceDTO toDTO(BalanceEntity balanceEntity) {
        if (balanceEntity == null) {
            return null;
        }

        return new BalanceDTO(
                balanceEntity.getId(),
                balanceEntity.getUserEntity().getId(),
                balanceEntity.getBalance()
        );
    }

    public BalanceEntity toEntity(BalanceDTO balanceDTO, UserEntity userEntity) {
        if (balanceDTO == null) {
            return null;
        }

        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setId(balanceDTO.getId());
        balanceEntity.setUserEntity(userEntity);
        balanceEntity.setBalance(balanceDTO.getBalance());

        return balanceEntity;
    }
}
