package com.shop.controller.dto;

import lombok.Getter;
import lombok.ToString;

import javax.servlet.http.HttpSession;

@Getter
public class MyItemDTO {

    private int item_no;
    private String mem_id;
    private String title;
    private String link;
    private String image;
    private Integer lprice;
    private Integer myPrice;

}
