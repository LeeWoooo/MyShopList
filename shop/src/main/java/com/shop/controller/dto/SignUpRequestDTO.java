package com.shop.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SignUpRequestDTO {

    private String mem_id;
    private String mem_pwd;
    private String mem_name;
    private String mem_birth;
    private String mem_email;
    private String mem_phone;
    private String mem_zipCode;
    private String mem_address;
    private String mem_detailAddress;
}
