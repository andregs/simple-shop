package com.example.store.user;

import com.example.store.user.data.CreateUserDTO;
import com.example.store.user.data.Role;
import com.example.store.user.data.User;
import com.example.store.user.data.UserQueryDTO;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    Logger logger;

    @MockBean
    UserService userService;

    @Test
    void create() throws Exception {
        var expected = new User(42L, "mary", "pass", Role.ADMIN);
        doReturn(expected).when(userService).save(any());

        var json = JSONValue.toJSONString(
                new CreateUserDTO("mary", "pass", "pass", Role.ADMIN));

        var request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/users/42"))
                .andExpect(content().string(""));

        verify(userService, times(1)).save(any());
    }

    @Test
    void read() throws Exception {
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

}
