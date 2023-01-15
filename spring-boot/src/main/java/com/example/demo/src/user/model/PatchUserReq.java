package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private int userIdx;
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String wowMem;
    private String couPay;
    private String couCash;
    private String profileImgUrl;
    private char agreeInfo;
    private char cEmail;
    private char cSms;
    private char cAppPush;
}
