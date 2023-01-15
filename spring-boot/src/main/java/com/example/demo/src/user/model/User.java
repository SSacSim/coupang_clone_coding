package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String wowMem;
    private int couPay;
    private int couCash;
    private String profileImgUrl;
    private String agreeInfo;
    private String adEmail;
    private String adSms;
    private String adAppPush;
}
