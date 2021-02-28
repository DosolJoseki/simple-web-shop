package com.zemliak.simplewebshop.repositories;

import com.zemliak.simplewebshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;

    @Override
    public Iterable<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User getUser(String username) {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return null;
        }

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public boolean isUserExists(User user) {
        return getUser(user.getUsername()) != null;
    }

    @Override
    public void saveUser(User user) {
        repository.save(user);
    }
}
