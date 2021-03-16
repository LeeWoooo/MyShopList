package com.shop.controller.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ItemDTO {

    //네이버 쇼핑에서 검색한 상품 정보중 필요한 정보만 가져와 저장해서 사용하는 DTO
    private String title;
    private String link;
    private String image;
    private Integer lprice;

    public ItemDTO(JSONObject itemInfo){
        this.title = itemInfo.getString("title");
        this.link = itemInfo.getString("link");
        this.image = itemInfo.getString("image");
        this.lprice = itemInfo.getInt("lprice");
    }

}
