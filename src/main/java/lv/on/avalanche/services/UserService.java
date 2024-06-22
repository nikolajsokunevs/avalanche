package lv.on.avalanche.services;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.CreateUserRequest;
import lv.on.avalanche.entities.Balance;
import lv.on.avalanche.entities.User;
import lv.on.avalanche.repository.BalanceRepository;
import lv.on.avalanche.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    public User findUserById(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public User createUser(CreateUserRequest request) {
        User response=userRepository.findByChatId(request.getChatId());
        if (response==null) {
            try {
                User user = new User();
                user.setName(request.getName());
                user.setChatId(request.getChatId());
                user.setUserName(request.getUserName());
                user.setRegisteredAt(Timestamp.valueOf(LocalDateTime.now()));
                user = userRepository.save(user);
                log.info("User saved");
                log.info("Create user: " + request);
                Balance balance = new Balance();
                balance.setUser(user);
                balance.setBalance(1000.00);
                balanceRepository.save(balance);
                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

}
