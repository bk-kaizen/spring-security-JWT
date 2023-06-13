package com.bk.jwtsecurity.user.service.api;

import com.bk.jwtsecurity.user.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> retrieveUserByEmail(String email);

    User saveUser(User user);
}
