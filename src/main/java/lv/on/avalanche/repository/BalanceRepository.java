package lv.on.avalanche.repository;

import lv.on.avalanche.entities.BalanceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BalanceRepository extends CrudRepository<BalanceEntity, Long> {

    @Query("SELECT b FROM BalanceEntity b WHERE b.userEntity.id = :userId")
    BalanceEntity findByUserId(@Param("userId") Long userId);

    @Query("SELECT b FROM BalanceEntity b WHERE b.userEntity.chatId = :chatId")
    BalanceEntity findByUserChatId(@Param("chatId") Long chatId);
}