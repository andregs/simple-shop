package com.example.store.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserQueryDTO {

    private long id;

    private String username;

    private Role role;

}
