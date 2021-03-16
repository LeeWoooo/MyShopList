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

//사용자의 회원가입 및 로그인을 처리하는 컨트롤러
@RequiredArgsConstructor
@RestController
public class ShopMemberController {

    private final ShopMemberService shopMemberService;

    //아이디 중복검사
    @PostMapping("/idCheck.do")
    public String idCheck(@RequestBody IdCheckDTO idCheckDTO){
        return shopMemberService.idCheck(idCheckDTO);
    }

    //이메일 중복검사
    @PostMapping("/emailCheck.do")
    public String emailCheck(@RequestBody EmailCheckDTO emailCheckDTO){
        return shopMemberService.eamilCheck(emailCheckDTO);
    }

    //회원가입 요청
    @PostMapping("/signUp.do")
    public String signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        return shopMemberService.memberSave(signUpRequestDTO);
    }

    //회원 로그인 요청
    @PostMapping("signIn.do")
    public String signIn(@RequestBody SignInRequestDTO signInRequestDTO, HttpSession httpSession){
        return shopMemberService.memberLogin(signInRequestDTO,httpSession);
    }

}
