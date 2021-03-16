package com.shop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MyItemResponseDTO {
    private int totalPage;
    private List<MyItemDTO> myItemList;
}
