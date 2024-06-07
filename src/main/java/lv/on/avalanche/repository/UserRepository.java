package lv.on.avalanche.repository;

import lv.on.avalanche.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByChatId(Long chatId);
}