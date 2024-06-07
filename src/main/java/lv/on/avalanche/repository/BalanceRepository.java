package lv.on.avalanche.repository;

import lv.on.avalanche.entities.Balance;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepository extends CrudRepository<Balance, Long> {

    Balance findByUserChatId(Long chatId);
}