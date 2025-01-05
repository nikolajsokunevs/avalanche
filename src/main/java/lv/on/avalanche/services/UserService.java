package lv.on.avalanche.services;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.GameHistoryResponseDTO;
import lv.on.avalanche.dto.UserDTO;
import lv.on.avalanche.entities.BalanceEntity;
import lv.on.avalanche.entities.GameEntity;
import lv.on.avalanche.entities.UserEntity;
import lv.on.avalanche.mapper.GameMapper;
import lv.on.avalanche.mapper.UserMapper;
import lv.on.avalanche.repository.BalanceRepository;
import lv.on.avalanche.repository.GameRepository;
import lv.on.avalanche.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private GameRepository gameRepository;

    public UserEntity findUserById(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public List<GameHistoryResponseDTO> getHistory(Long chatId) {
        List<GameEntity> games = gameRepository.findByInProgressFalseAndUser1IdOrUser2Id(chatId);
        return games.stream().map(game->gameMapper.toGameHistoryResponseDTO(game, chatId)).collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO request) {
        UserEntity response = userRepository.findByChatId(request.getChatId());
        if (response == null) {
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
