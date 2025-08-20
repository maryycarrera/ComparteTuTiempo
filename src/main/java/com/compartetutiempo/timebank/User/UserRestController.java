package com.compartetutiempo.timebank.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = {"http://localhost:4200"}) // TODO: Modificar posteriormente para desplegar en la nube
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(@RequestParam(required = false) Authority authority) {
        List<User> users;

        if (authority != null) {
            users = (List<User>) userService.findAllByAuthority(authority);
        } else {
            users = (List<User>) userService.findAll();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "{userId}")
    public ResponseEntity<User> findById(@PathVariable("userId") Integer userId) {
        User user = userService.findUser(userId);
        return ResponseEntity.ok(user);
    }

}