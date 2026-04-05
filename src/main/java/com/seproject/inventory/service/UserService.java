package com.seproject.inventory.service;

import com.seproject.inventory.dto.UserRegisterDto;
import com.seproject.inventory.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(UserRegisterDto dto, String roleName);

    User findByUsername(String username);

    List<User> getAllUsers();
}
