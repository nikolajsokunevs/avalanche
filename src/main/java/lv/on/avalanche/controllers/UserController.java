package lv.on.avalanche.controllers;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.UserDTO;
import lv.on.avalanche.mapper.UserMapper;
import lv.on.avalanche.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/create")
    public UserDTO create(@RequestBody UserDTO request) {
        log.info("Create user: " + request);
        return userService.createUser(request);
    }

    @GetMapping("/get/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userMapper.toDTO(userService.findUserById(id));
    }
}
