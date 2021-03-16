package com.shop.controller.dto;

import lombok.Getter;

@Getter
public class SignInRequestDTO {
    //로그인 요청을 하는 DTO
    private String mem_id;
    private String mem_pwd;
}
