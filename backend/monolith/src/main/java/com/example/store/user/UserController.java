package com.example.store.user;

import java.security.Principal;
import java.util.List;

import com.example.store.user.data.CreateUserDTO;
import com.example.store.user.data.UserQueryDTO;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
@ConditionalOnProperty(prefix = "app.user", name = "enabled", havingValue = "true", matchIfMissing = true)
public class UserController {

    private final Logger logger;
    private final UserService userService;

    @GetMapping("current")
    public Object currentUser(Principal user) {
        var authToken = (UsernamePasswordAuthenticationToken) user;
        return authToken.getPrincipal();
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateUserDTO createUserDTO) {
        logger.info("POST a new user {}", createUserDTO);
        var userId = userService.save(createUserDTO).getId();
        logger.info("User {} created", userId);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<UserQueryDTO> read(Pageable pageable) {
        logger.info("GET a list of users");
        return userService.findAll(pageable);
    }

}
