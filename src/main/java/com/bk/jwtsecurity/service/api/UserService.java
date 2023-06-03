package com.bk.jwtsecurity.service.api;

import com.bk.jwtsecurity.model.User;

public interface UserService {

    public User retrieveUserByEmail(String email);
}
