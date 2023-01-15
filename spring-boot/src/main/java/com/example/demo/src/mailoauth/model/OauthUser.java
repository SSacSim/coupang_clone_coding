package com.example.demo.src.mailoauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OauthUser {
    private int userId;
    private int userOauthId;
    private String oauthNum;
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
