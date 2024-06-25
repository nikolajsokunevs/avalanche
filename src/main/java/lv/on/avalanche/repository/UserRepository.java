package lv.on.avalanche.repository;

import lv.on.avalanche.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByChatId(Long chatId);
}