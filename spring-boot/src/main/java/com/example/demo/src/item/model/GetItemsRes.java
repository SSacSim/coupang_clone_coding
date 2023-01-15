package com.example.demo.src.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetItemsRes {
    private int itemId;
    private String itemName;
    private String itemImgUrl;
    private int categoryId;
    private String rocket;
    private int shippingCost;
    private int price;
    private int couPrice;
    private int wowPrice;
    private int discount;
    private String brandName;
    private int starScore;
    private int reviewCnt;
}
