package net.partala.userservice.auth;

import lombok.Getter;
import net.partala.userservice.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SecurityUser implements UserDetails {

    @Getter
    private final Long id;
    private final String username;
    private final String password;
    @Getter
    private final Set<UserRole> roles;
    private final boolean active = true;

    public SecurityUser(Long id, String username, String password, Set<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getAuthority()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override public boolean isAccountNonLocked() {
        return active;
    }

    @Override public boolean isAccountNonExpired() { return true; }


    @Override public boolean isCredentialsNonExpired() { return true; }
}
