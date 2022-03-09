package com.kss.keycloakuserstore.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;



@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity(name = "UserLoginEntity")
@Table(name = "USERLOGIN")
public class UserLoginEntity {
    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "LOGINPWD")
    private String password;
    @Column(name = "STATUS")
    private String status;
}
