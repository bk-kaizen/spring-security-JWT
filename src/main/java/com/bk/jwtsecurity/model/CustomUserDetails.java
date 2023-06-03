package com.bk.jwtsecurity.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomUserDetails class implements the UserDetails interface and represents
 * custom user details for security.
 */
@Data
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    /**
     * Constructs a CustomUserDetails object using the provided User object.
     * @param user the User object representing the user.
     */
    public CustomUserDetails(User user) {
        userName = user.getFirstName();
        password = user.getPassword();
        authorities = List.of(new SimpleGrantedAuthority(user.getRoles().name()));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
        return true;
    }
}
