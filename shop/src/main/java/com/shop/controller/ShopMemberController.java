package com.shop.controller;

import com.shop.controller.dto.EmailCheckDTO;
import com.shop.controller.dto.IdCheckDTO;
import com.shop.controller.dto.SignInRequestDTO;
import com.shop.controller.dto.SignUpRequestDTO;
import com.shop.service.ShopMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class ShopMemberController {

    private final ShopMemberService shopMemberService;

    @PostMapping("/idCheck.do")
    public String idCheck(@RequestBody IdCheckDTO idCheckDTO){
        return shopMemberService.idCheck(idCheckDTO);
    }

    @PostMapping("/emailCheck.do")
    public String emailCheck(@RequestBody EmailCheckDTO emailCheckDTO){
        return shopMemberService.eamilCheck(emailCheckDTO);
    }

    @PostMapping("/signUp.do")
    public String signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        return shopMemberService.memberSave(signUpRequestDTO);
    }

    @PostMapping("signIn.do")
    public String signIn(@RequestBody SignInRequestDTO signInRequestDTO, HttpSession httpSession){
        return shopMemberService.memberLogin(signInRequestDTO,httpSession);
    }

}
