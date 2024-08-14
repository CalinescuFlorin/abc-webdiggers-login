package web_diggers.abc_backend.security.user.model;

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

    private String firstName;
    private String lastName;

    private String password;

    @Column(unique=true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled2FA = false;
    private String codeFor2FA = "";

    private boolean locked = false;
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
