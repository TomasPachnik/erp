package sk.tomas.erp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.userdetails.UserDetails;
import sk.tomas.erp.bo.SimpleGrantedAuthorityImpl;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_table")
@JsonIgnoreProperties(value = {"accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String username;
    private String name;
    private String phone;
    private String email;
    private boolean enabled;
    private String password;

    @LazyCollection(LazyCollectionOption.FALSE)
    @Builder.Default
    @ElementCollection(targetClass = String.class)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends SimpleGrantedAuthorityImpl> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthorityImpl::new).collect(toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", uuid=" + uuid +
                '}';
    }
}
