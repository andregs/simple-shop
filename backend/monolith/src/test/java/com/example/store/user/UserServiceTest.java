package com.example.store.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import com.example.store.config.CoreConfiguration;
import com.example.store.product.ProductService;
import com.example.store.user.data.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

@DataJpaTest(properties = "app.product.enabled=false")
@Import({UserService.class, ObjectMapper.class, CoreConfiguration.class})
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired(required=false)
    ProductService productService;
    
    @Test
    @DisplayName("findAll should fetch users from DB")
    void findAll() {
        assertThat(productService).isNull();
        var expected = Arrays.asList(
                new UserQueryDTO(1L, "stanley", Role.ADMIN),
                new UserQueryDTO(2L, "john", Role.CUSTOMER));

        var actual = userService.findAll(Pageable.unpaged());

        assertThat(actual).containsAll(expected);
    }

    @Test
    @DisplayName("when user is valid, save() should persist the user")
    void save1() {
        var dto = new CreateUserDTO("fred", "flintstone", "flintstone", Role.ADMIN);
        var oldCount = userRepository.count();
        
        var actual = userService.save(dto);

        assertThat(userRepository.count()).isEqualTo(oldCount + 1);
        
        assertThat(actual)
            .isNotNull()
            .extracting(User::getUsername, User::getPassword, User::getRole)
            .containsExactly("fred", "flintstone", Role.ADMIN);

        assertThat(actual.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("when passwords don't match, save() should throw error")
    void save2() {
        var dto = new CreateUserDTO("stanley", "pass", "wrong", Role.ADMIN);
        var oldCount = userRepository.count();

        assertThatThrownBy(() -> userService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Passwords don't match");

        assertThat(userRepository.count()).isEqualTo(oldCount);
    }

}
