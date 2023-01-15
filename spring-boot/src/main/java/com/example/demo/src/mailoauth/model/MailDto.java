package com.example.demo.src.mailoauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailDto {
    private String address;
    private String title;
    private String message;
}