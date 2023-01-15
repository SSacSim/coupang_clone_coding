package com.example.demo.src.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private int itemId;
    private String itemName;
    private int categoryId;
    private String rocket;
    private int shippingCost;
    private int price;
    private int couPrice;
    private int wowPrice;
    private int discount;
    private String sellerName;
    private String brandName;
    private String[][] itemBoard;
    private int starScore;
    private int reviewCnt;
    private String[] mainImgUrlArr;
    private String[] subImgUrlArr;
}
