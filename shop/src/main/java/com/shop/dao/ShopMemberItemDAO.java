package com.shop.dao;

import com.shop.controller.dto.MyItemDTO;
import com.shop.controller.dto.UpdateLpriceRequestDTO;
import com.shop.controller.dto.UpdateMyPriceRequestDTO;
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
    
    //상품번호를 받아서 삭제
    public int delete(int item_no) {
        return sqlSession.delete("memberItemMapper.delete",item_no);
    }

    //openSession을 할 때 auto commit를 하지 않았기에 선언
    public void commit(){
        sqlSession.commit();
    }

    //상품 최저가 및 상품 번호를 받아서 최저가 업데이트
    public int update(UpdateMyPriceRequestDTO updateMyPriceRequestDTO) {
        return sqlSession.update("memberItemMapper.update", updateMyPriceRequestDTO);
    }
    
    //스케줄링을 위한 전체 등록된 아이템 찾기
    public List<MyItemDTO> finaAll(){
        return sqlSession.selectList("memberItemMapper.findAll");
    }

    //스케줄링 update 오토
    public void updateLprice(UpdateLpriceRequestDTO UpdateLpriceRequestDTO) {
        sqlSession.update("memberItemMapper.updateAuto",UpdateLpriceRequestDTO);
        commit();
    }
}
