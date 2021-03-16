package com.shop.dao;

import com.shop.controller.dto.MyItemDTO;
import com.shop.db.SqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ShopMemberItemDAO {

    private SqlSession sqlSession;

    public ShopMemberItemDAO() {
        this.sqlSession = SqlSessionFactoryBean.getSqlSession();
    }

    //사용자가 최저가로 등록한 상품 DB저장
    public int saveItem(MyItemDTO myItemDTO) {
        return sqlSession.insert("memberItemMapper.save",myItemDTO);
    }

    //사용자 id를 받아 등록된 상품 갯수 알아오기
    public int countRecord(String mem_id){
        return sqlSession.selectOne("memberItemMapper.countRecord",mem_id);
    }
    
    //사용자에게 paging처리에 필요한 값을 받아 List얻기
    public List<MyItemDTO> findAllPaging(Map<String,Object> pageMap){
        return sqlSession.selectList("memberItemMapper.findAllPaging",pageMap);
    }

    //openSession을 할 때 auto commit를 하지 않았기에 선언
    public void commit(){
        sqlSession.commit();
    }
}
