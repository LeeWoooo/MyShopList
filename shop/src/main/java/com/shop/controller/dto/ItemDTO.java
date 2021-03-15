package com.shop.controller.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ItemDTO {

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
