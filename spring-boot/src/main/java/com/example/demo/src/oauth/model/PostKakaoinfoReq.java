package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostKakaoinfoReq{

    private String accsessToken;
    private String accsessToken2;
    private String accsessToken3;

    // 변수가 하나만 있으면 안됨.
}
