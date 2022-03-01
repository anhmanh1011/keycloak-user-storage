package com.example.keycloakuserstore.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name="getUserByUsername", query="select u from CfmastEntity u where u.userName = :username and u.status = 'A' order by u.openTime desc "),
        @NamedQuery(name="getUserByEmail", query="select u from CfmastEntity u where u.email = :email and u.status = 'A' order by u.openTime desc "),
        @NamedQuery(name="getUserCount", query="select count(u) from CfmastEntity u  WHERE u.status = 'A' order by u.openTime desc "),
        @NamedQuery(name="searchForUser", query="select u from CfmastEntity u WHERE " +
                "( lower(u.userName) like :search or u.email like :search or u.phone like :search) and u.status = 'A' order by u.openTime desc "),
})

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Table(name="CFMAST") // table name
@Entity(name = "CfmastEntity")
public class CfmastEntity {
    @Id
    @Column(name = "CUSTID", nullable = false)
    private String id;

    @Column(name = "CUSTODYCD")
    private String userName;


    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "FULLNAME")
    private String fullName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "OPENTIME")
    private LocalDateTime openTime;

}
