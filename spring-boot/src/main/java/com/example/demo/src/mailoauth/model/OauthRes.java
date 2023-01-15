package com.example.demo.src.mailoauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OauthRes{

    private String resOauthNum;
    private int ouathId;

    // 변수가 하나만 있으면 안됨.
}
