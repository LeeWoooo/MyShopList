package com.shop.dao;

import com.shop.controller.dto.SignInRequestDTO;
import com.shop.controller.dto.SignUpRequestDTO;
import com.shop.db.SqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class ShopMemberDAO {

    private SqlSession sqlSession;

    public ShopMemberDAO(){
        this.sqlSession = SqlSessionFactoryBean.getSqlSession();
    }

    //아이디 중복검사
    public int idCheck(String mem_id){
        return sqlSession.selectOne("memberMapper.idCheck",mem_id);
    }
    
    //이메일 중복검사
    public int emailCheck(String mem_eamil) {
        return sqlSession.selectOne("memberMapper.emailCheck",mem_eamil);
    }

    //회원가입
    public int memberSave(SignUpRequestDTO signUpRequestDTO){
        return sqlSession.insert("memberMapper.memberSave", signUpRequestDTO);
    }

    //로그인
    public int memberLogin(SignInRequestDTO signInRequestDTO) {
        return sqlSession.selectOne("memberMapper.memberLogin",signInRequestDTO);
    }

    public void commit(){
        sqlSession.commit();
    }


}
