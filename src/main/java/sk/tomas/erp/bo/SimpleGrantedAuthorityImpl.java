package sk.tomas.erp.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * created for make possible JSON deserialization
 */
@Data
@JsonIgnoreProperties(value = {"authority"})
public class SimpleGrantedAuthorityImpl implements GrantedAuthority {
    private static final long serialVersionUID = 510L;
    private String role;

    public SimpleGrantedAuthorityImpl() {
    }

    public SimpleGrantedAuthorityImpl(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
