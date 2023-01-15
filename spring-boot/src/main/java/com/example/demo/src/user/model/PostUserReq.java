package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq { // 회원가입 할때 사용 
    private String UserName;
    private String email;
    private String password;
    private String phone;
    private String agreeInfo;
    private String cEmail;
    private String cSms;
    private String cAppPush;
    
}
