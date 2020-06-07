package com.example.store.user;

import com.example.store.user.data.CreateUserDTO;
import com.example.store.user.data.Role;
import com.example.store.user.data.User;
import com.example.store.user.data.UserQueryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    Logger logger;

    @Mock
    UserRepository userRepository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    UserService userService;

    @Test
    void findAll() {
        var expected = Arrays.asList(
                new UserQueryDTO(42L, "stanley", Role.ADMIN),
                new UserQueryDTO(43L, "peter", Role.CUSTOMER));

        doReturn(new PageImpl<>(expected))
                .when(userRepository).findAll(isA(Pageable.class));

        var actual = userService.findAll(Pageable.unpaged());

        verify(userRepository, times(1))
                .findAll(isA(Pageable.class));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void save_shouldStoreNewUser() {
        doAnswer(returnsFirstArg())
                .when(userRepository).save(isA(User.class));

        var dto = new CreateUserDTO("fred", "flintstone", "flintstone", Role.ADMIN);
        var actual = userService.save(dto);

        verify(userRepository, times(1))
                .save(isA(User.class));

        assertThat(actual)
                .isNotNull()
                .isEqualTo(new User("fred", "flintstone", Role.ADMIN));
    }

    @Test
    void save_whenPasswordMismatch_shouldThrowError() {
        var dto = new CreateUserDTO("stanley", "pass", "wrong", Role.ADMIN);

        assertThatThrownBy(() -> userService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Passwords don't match");

        verify(userRepository, never()).save(any());
    }

}