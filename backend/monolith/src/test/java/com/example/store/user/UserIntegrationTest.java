package com.example.store.user;

import com.example.store.config.CoreConfiguration;
import com.example.store.user.data.CreateUserDTO;
import com.example.store.user.data.Role;
import com.example.store.user.data.UserQueryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = UserController.class)
class TestConfiguration {
}

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {TestConfiguration.class, CoreConfiguration.class})
class UserIntegrationTest {

    @LocalServerPort
    int port;

    String baseUrl;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void users() {
        createOneUser();
        listAllUsers();
    }

    void createOneUser() {
        var count = userRepository.count();

        var user = new CreateUserDTO("george", "jetson", "jetson", Role.CUSTOMER);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateUserDTO> entity = new HttpEntity<>(user, headers);
        var response = restTemplate.postForEntity("/users", entity, String.class);

        assertThat(userRepository.count()).isEqualTo(count + 1);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create(baseUrl + "/users/4"));
        assertThat(response.getBody()).isNull();
    }

    void listAllUsers() {
        var expected = Arrays.asList(
                new UserQueryDTO(1L, "stanley", Role.ADMIN),
                new UserQueryDTO(2L, "john", Role.CUSTOMER),
                new UserQueryDTO(3L, "indiana", Role.CUSTOMER),
                new UserQueryDTO(4L, "george", Role.CUSTOMER));

        var response = restTemplate.getForEntity("/users", UserQueryDTO[].class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .hasSize(expected.size())
                .containsAll(expected);
    }

}