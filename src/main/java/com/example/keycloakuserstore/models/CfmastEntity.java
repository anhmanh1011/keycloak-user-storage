package com.example.keycloakuserstore.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CFMAST")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CfmastEntity {

    @Basic
    @Id
    @Column(name = "CUSTID")
    private String cusId;

    @Basic
    @Column(name = "CUSTODYCD")
    private String cusToCD;

    @Basic
    @Column(name = "EMAIL")
    private String email;

    @Basic
    @Column(name = "MOBILE")
    private String mobile;
    @Basic
    @Column(name = "FULLNAME")
    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CfmastEntity that = (CfmastEntity) o;
        return cusId != null && Objects.equals(cusId, that.cusId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
