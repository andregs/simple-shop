package com.example.store.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserDTO {

    private String username;

    private String password;

    private String confirmation;

    private Role role;

}
