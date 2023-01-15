package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCartRes {
    private int itemId;
    private String itemName;
    private String rocket;
    private String itemImgUrl;
    private int itemCount;
    private int price;
}
