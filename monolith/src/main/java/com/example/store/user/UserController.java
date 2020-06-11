package com.example.store.user;

import com.example.store.user.data.CreateUserDTO;
import com.example.store.user.data.UserQueryDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
class UserController {

    private final Logger logger;
    private final UserService userService;

    @GetMapping("current")
    Principal currentUser(Principal user) {
        return user;
    }

    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreateUserDTO createUserDTO) {
        logger.info("POST a new user {}", createUserDTO);
        var userId = userService.save(createUserDTO).getId();
        logger.info("User {} created", userId);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    List<UserQueryDTO> read(Pageable pageable) {
        logger.info("GET a list of users");
        return userService.findAll(pageable);
    }

}
