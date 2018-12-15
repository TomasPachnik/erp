package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_table")
public class UserEntity extends BaseEntity {

    private String name;
    private String surname;
    private String login;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    PasswordEntity password;

}
