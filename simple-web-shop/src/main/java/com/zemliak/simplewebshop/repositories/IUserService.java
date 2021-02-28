package com.zemliak.simplewebshop.repositories;

import com.zemliak.simplewebshop.models.User;

public interface IUserService {
    Iterable<User> getAllUsers();
    User getUser(Long id);
    User getUser(String username);
    boolean isUserExists(User user);
    void saveUser(User user);
}
