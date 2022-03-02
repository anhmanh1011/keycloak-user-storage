package com.kss.keycloakuserstore.model;

import lombok.Data;

@Data
public class UserDto {
    String id;
    String userName;
    String password;
    String email;
    String phone;
    String fullName;
}
