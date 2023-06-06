package com.bk.jwtsecurity.user.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bk.jwtsecurity.user.model.User;
import com.bk.jwtsecurity.user.repo.UserRepository;
import com.bk.jwtsecurity.user.service.api.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User retrieveUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
        return user;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
