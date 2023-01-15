package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWishItemsRes {
    private int itemId;
    private String itemName;
    private int discount;
    private int price;
    private int couPrice;
    private int wowPrice;
    private String rocket;
    private String itemImgUrl;
}
