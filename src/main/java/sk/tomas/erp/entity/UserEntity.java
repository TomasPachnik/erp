package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_table")
public class UserEntity extends BaseEntity {

    private String name;
    private String login;
    private String phone;
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    PasswordEntity password;

}
