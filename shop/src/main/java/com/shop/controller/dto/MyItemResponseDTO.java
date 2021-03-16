package com.shop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MyItemResponseDTO {
    //페이징 처리를 위한 DTO
    private int totalPage;
    private List<MyItemDTO> myItemList;
}
