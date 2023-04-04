package com.dans.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dans.models.entities.User;
import com.dans.models.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
 
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return password.equals(user.getPassword());
    }
}
