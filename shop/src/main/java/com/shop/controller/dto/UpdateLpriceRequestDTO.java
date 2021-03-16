package com.shop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateLpriceRequestDTO {
    //상품 가격 자동 최신화를 위한 DTO
    private int item_no;
    private int lprice;
}
