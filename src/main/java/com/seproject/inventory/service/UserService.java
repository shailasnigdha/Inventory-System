package com.seproject.inventory.service;

import com.seproject.inventory.dto.UserRegisterDto;
import com.seproject.inventory.entity.User;

public interface UserService {
    User registerUser(UserRegisterDto dto, String roleName);
}
