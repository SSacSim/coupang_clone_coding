package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPassword {
    private int userId;
    private String nowPassword;
    private String newPassword;
    private String checkPassword;
}
