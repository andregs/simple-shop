package com.example.store.user;

import java.util.List;

import com.example.store.user.data.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "app.user", name = "enabled", havingValue = "true", matchIfMissing = true)
public class UserService {

    private final Logger logger;
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    List<UserQueryDTO> findAll(Pageable pageable) {
        logger.info("fetching page");
        var users = repository.findAll(pageable).getContent();

        var type = new TypeToken<List<UserQueryDTO>>(){}.getType();
        return modelMapper.map(users, type);
    }

    @Transactional
    public User save(CreateUserDTO dto) {
        // TODO https://spring.io/guides/tutorials/spring-security-and-angular-js/
        logger.info("saving a new user");
        if (!dto.getPassword().equals(dto.getConfirmation())) {
            throw new IllegalArgumentException("Passwords don't match");
        }

        var user = new User(dto.getUsername(), dto.getPassword(), dto.getRole());
        return repository.save(user);
    }

}
