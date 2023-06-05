package com.bk.jwtsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bk.jwtsecurity.user.model.CustomUserDetails;
import com.bk.jwtsecurity.user.model.User;
import com.bk.jwtsecurity.user.service.api.UserService;

/**
 * CustomUserDetailsService class implements the UserDetailsService interface
 * and is responsible for loading user details from the UserService.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    /**
     * Loads user details for the given username from the UserService.
     * @param  username                  the username of the user to load details
     *                                   for.
     * @return                           UserDetails object representing the user
     *                                   details.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.retrieveUserByEmail(username);
        return new CustomUserDetails(user);
    }
}
