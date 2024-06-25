package lv.on.avalanche.mapper;

import lv.on.avalanche.dto.UserDTO;
import lv.on.avalanche.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity game) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(game.getId());
        userDTO.setName(game.getName());
        userDTO.setUserName(game.getUserName());
        userDTO.setChatId(game.getChatId());
        userDTO.setState(game.getState());
        userDTO.setCreatedAt(game.getCreatedAt());
        userDTO.setUpdatedAt(game.getUpdatedAt());
        return userDTO;
    }

    public UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setName(userDTO.getName());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setChatId(userDTO.getChatId());
        userEntity.setState(userDTO.getState());
        userEntity.setCreatedAt(userDTO.getCreatedAt());
        userEntity.setUpdatedAt(userDTO.getUpdatedAt());
        return userEntity;
    }
}
