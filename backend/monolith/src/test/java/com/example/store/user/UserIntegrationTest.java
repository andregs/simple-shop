package com.example.store.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.net.HttpCookie;
import java.net.URI;
import java.util.Arrays;

import com.example.store.config.CoreConfiguration;
import com.example.store.config.SecurityConfiguration;
import com.example.store.user.data.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = UserController.class)
class TestConfiguration {
}

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {TestConfiguration.class, CoreConfiguration.class, SecurityConfiguration.class})
class UserIntegrationTest {

    @LocalServerPort
    int port;

    String baseUrl;
    String cookie;

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

        var headers = getCsrfHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>(user, headers);
        var response = restTemplate.postForEntity("/users", entity, String.class);

        assertThat(userRepository.count()).isEqualTo(count + 1);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create(baseUrl + "/users/4"));
        assertThat(response.getBody()).isNull();
    }

    private HttpHeaders getCsrfHeader() {
        var headers = new HttpHeaders();

        var csrfResponse = restTemplate.getForEntity("/users/current", String.class);
        cookie = csrfResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        var csrfToken = HttpCookie.parse(cookie)
            .stream()
            .filter(c -> c.getName().equals("XSRF-TOKEN"))
            .map(HttpCookie::getValue)
            .findFirst()
            .get();
        
        headers.set("Cookie", cookie);
        headers.set("X-XSRF-TOKEN", csrfToken);
        return headers;
    }

    void listAllUsers() {
        var expected = Arrays.asList(
                new UserQueryDTO(1L, "stanley", Role.ADMIN),
                new UserQueryDTO(2L, "john", Role.CUSTOMER),
                new UserQueryDTO(3L, "indiana", Role.CUSTOMER),
                new UserQueryDTO(4L, "george", Role.CUSTOMER));

        // FIXME this endpoint requires authentication
        var response = restTemplate.getForEntity("/users", UserQueryDTO[].class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .hasSize(expected.size())
                .containsAll(expected);
    }

}