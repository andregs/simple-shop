package com.example.store.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import com.example.store.product.ProductService;
import com.example.store.user.data.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import net.minidev.json.JSONValue;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureTestDatabase
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    Logger logger;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username = "stanley", roles = "ADMIN")
    @DisplayName("GET /users/current should fetch current user info")
    void currentUser1() throws Exception {
        var request = MockMvcRequestBuilders.get("/users/current");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("stanley"))
                .andExpect(jsonPath("$.authorities.*", hasSize(1)))
                .andExpect(jsonPath("$.authorities[0].authority", containsString("ADMIN")));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("GET /users/current when anonymous should return 401")
    void currentUser2() throws Exception {
        var request = MockMvcRequestBuilders.get("/users/current");
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("POST /users should create a user")
    void create() throws Exception {
        var expected = new User(42L, "mary", "pass", Role.ADMIN);
        doReturn(expected).when(userService).save(any());

        var json = JSONValue.toJSONString(
                new CreateUserDTO("mary", "pass", "pass", Role.ADMIN));

        var request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/users/42"))
                .andExpect(content().string(""));

        verify(userService, times(1)).save(any());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /users when authenticated should fetch user list")
    void read1() throws Exception {
        var expected = Arrays.asList(
                new UserQueryDTO(1L, "stanley", Role.ADMIN),
                new UserQueryDTO(2L, "john", Role.CUSTOMER));

        doReturn(expected).when(userService).findAll(any());

        var request = MockMvcRequestBuilders.get("/users");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expected.size())))

                .andExpect(jsonPath("$[0].*", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("stanley"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))

                .andExpect(jsonPath("$[1].*", hasSize(3)))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("john"))
                .andExpect(jsonPath("$[1].role").value("CUSTOMER"));
    }
    
    @Test
    @WithAnonymousUser
    @DisplayName("GET /users when anonymous should give 401")
    void read2() throws Exception {
        var request = MockMvcRequestBuilders.get("/users");
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // TODO add tests for login/logout/register (new class)
    // TODO add tests for csrf and other security configs

}
