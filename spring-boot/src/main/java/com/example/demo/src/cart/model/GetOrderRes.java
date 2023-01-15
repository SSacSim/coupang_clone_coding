package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderRes {
    private int userId;
    private int addressId;
    private String recipient;
    private String address;
    private String phone;
    private String deliveryReq;
    private int paymentId;
    private String paymentType;
    private String payInfo;
    private int totalCount;
    private String[] itemNames;
    private String[] rockets;
    private int[] itemCounts;
    private int[] itemPrices;
}