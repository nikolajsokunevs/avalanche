package lv.on.avalanche.services;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.UserDTO;
import lv.on.avalanche.entities.BalanceEntity;
import lv.on.avalanche.entities.UserEntity;
import lv.on.avalanche.mapper.UserMapper;
import lv.on.avalanche.repository.BalanceRepository;
import lv.on.avalanche.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BalanceRepository balanceRepository;

    public UserEntity findUserById(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public UserDTO createUser(UserDTO request) {
        UserEntity response=userRepository.findByChatId(request.getChatId());
        if (response==null) {
            try {
                UserEntity userEntity = new UserEntity();
                userEntity.setName(request.getName());
                userEntity.setChatId(request.getChatId());
                userEntity.setUserName(request.getUserName());
                userEntity = userRepository.save(userEntity);
                log.info("User saved");
                log.info("Create user: " + request);
                BalanceEntity balanceEntity = new BalanceEntity();
                balanceEntity.setUserEntity(userEntity);
                balanceEntity.setBalance(1000.00);
                balanceRepository.save(balanceEntity);
                return userMapper.toDTO(userEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userMapper.toDTO(response);
    }

}
