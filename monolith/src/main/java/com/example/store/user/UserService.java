package com.example.store.user;

import com.example.store.user.data.CreateUserDTO;
import com.example.store.user.data.User;
import com.example.store.user.data.UserQueryDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class UserService {

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
    User save(CreateUserDTO dto) {
        // TODO https://spring.io/guides/tutorials/spring-security-and-angular-js/
        logger.info("saving a new user");
        if (!dto.getPassword().equals(dto.getConfirmation())) {
            throw new IllegalArgumentException("Passwords don't match");
        }

        var user = new User(dto.getUsername(), dto.getPassword(), dto.getRole());
        return repository.save(user);
    }

}
