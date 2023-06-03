package com.bk.jwtsecurity.user.service.api;

import com.bk.jwtsecurity.user.model.User;

public interface UserService {

    public User retrieveUserByEmail(String email);
}
