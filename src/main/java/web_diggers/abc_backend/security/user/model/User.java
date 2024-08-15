package web_diggers.abc_backend.security.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_users")
public class User implements UserDetails {

    @Schema(
            description = "User id (auto-generated)",
            name = "id",
            type = "integer",
            example = "101")
    @TableGenerator(
            name = "usersSeq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(
            strategy= GenerationType.TABLE,
            generator="usersSeq")
    @Id
    @Column(name = "userId")
    private Integer id;

    @Schema(
            description = "User first name",
            name = "firstName",
            type = "string",
            example = "Nick")
    private String firstName;
    @Schema(
            description = "User last name",
            name = "lastName",
            type = "string",
            example = "Smith")
    private String lastName;

    @Schema(
            description = "User password",
            name = "password",
            type = "string",
            example = "password123")
    private String password;

    @Schema(
            description = "User email",
            name = "email",
            type = "string",
            example = "text@gmail.com")
    @Column(unique=true)
    private String email;

    @Schema(
            description = "User role",
            name = "role",
            type = "string",
            example = "ADMIN / USER / ARCHAEOLOGIST / BIOLOGIST")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Schema(
            description = "If the user wants to use 2FA for authentication or not (default is false)",
            name = "enabled2FA",
            type = "boolean",
            example = "true / false")
    private boolean enabled2FA = false;
    @Schema(
            description = "Code used by user for 2FA (default is null string)",
            name = "codeFor2FA",
            type = "string",
            example = "123456 / 2FA code")
    private String codeFor2FA = "";

    @Schema(
            description = "If the user account is locked / blocked (default is false)",
            name = "locked",
            type = "boolean",
            example = "true / false")
    private boolean locked = false;
    @Schema(
            description = "If the user account is enabled, done by email verification (default is false)",
            name = "enabled",
            type = "boolean",
            example = "true / false")
    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role == null)
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return switch (role.getValue()) {
            case "ROLE_ADMIN" ->
                    List.of(new SimpleGrantedAuthority("ROLE_USER"),
                            new SimpleGrantedAuthority("ROLE_ARCHAEOLOGIST"),
                            new SimpleGrantedAuthority("ROLE_BIOLOGIST"),
                            new SimpleGrantedAuthority("ROLE_ADMIN"));
            case "ROLE_ARCHAEOLOGIST" ->
                    List.of(new SimpleGrantedAuthority("ROLE_USER"),
                            new SimpleGrantedAuthority("ROLE_ARCHAEOLOGIST"));
            case "ROLE_BIOLOGIST" ->
                    List.of(new SimpleGrantedAuthority("ROLE_USER"),
                            new SimpleGrantedAuthority("ROLE_BIOLOGIST"));
            default -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
        };
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return email;
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
}
