package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    @GetMapping("/signUp.do")
    public String signUpForm(){
        return "signUp";
    }

    @GetMapping("/signIn.do")
    public String signInFrom(){
        return "signIn";
    }

}
