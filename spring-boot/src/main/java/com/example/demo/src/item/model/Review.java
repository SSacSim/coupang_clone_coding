package com.example.demo.src.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int itemId;
    private String profileImgUrl;
    private String userName;
    private int reviewScore;
    private String date;
    private String sellerName;
    private String itemName;
    private String[] reviewImgArr;
    private String reviewTxt;
    private int likeCount;
}
