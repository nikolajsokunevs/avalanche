package lv.on.avalanche.controllers;

import lv.on.avalanche.dto.CreateUserRequest;
import lv.on.avalanche.entities.User;
import lv.on.avalanche.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User create(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/get/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findUserById(id);
    }
}
