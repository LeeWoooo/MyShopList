package com.shop.service;

import com.shop.controller.dto.EmailCheckDTO;
import com.shop.controller.dto.IdCheckDTO;
import com.shop.controller.dto.SignInRequestDTO;
import com.shop.controller.dto.SignUpRequestDTO;
import com.shop.dao.ShopMemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class ShopMemberService {

    //DAO를 DI로 주입
    private final ShopMemberDAO shopMemberDAO;

    //아이디 중복검사
    public String idCheck(IdCheckDTO idCheckDTO){
        return String.valueOf(shopMemberDAO.idCheck(idCheckDTO.getMem_id()));
    }

    //이메일 중복검사
    public String eamilCheck(EmailCheckDTO emailCheckDTO) {
        return String.valueOf(shopMemberDAO.emailCheck(emailCheckDTO.getMem_email()));
    }

    //회원가입
    public String memberSave(SignUpRequestDTO signUpRequestDTO){
        int result = shopMemberDAO.memberSave(signUpRequestDTO);
        //DB insert결과가 성공이면 commit
        if(result == 1){
            shopMemberDAO.commit();
        }//end if
        return String.valueOf(result);
    }

    //로그인
    public String memberLogin(SignInRequestDTO signInRequestDTO, HttpSession httpSession) {
        int result = shopMemberDAO.memberLogin(signInRequestDTO);
        //로그인에 성공할 시 세션에 아이디 등록
        if(result == 1){
            httpSession.setAttribute("mem_id",signInRequestDTO.getMem_id());
        }//end if
        return String.valueOf(result);
    }
}
