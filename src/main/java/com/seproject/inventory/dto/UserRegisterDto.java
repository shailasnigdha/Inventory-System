package com.seproject.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    private String username;
    private String email;
    private String password;
}
