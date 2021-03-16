package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


//회원가입, 로그인, 로그아웃을 처리하는 컨트롤러
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

    @GetMapping("/logOut.do")
    public String logOut(HttpSession httpSession){
        httpSession.invalidate();
        return "index";
    }

}
