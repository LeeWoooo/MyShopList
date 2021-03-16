package com.shop.service;

import com.shop.controller.dto.ItemDTO;
import com.shop.controller.dto.MyItemDTO;
import com.shop.controller.dto.UpdateMyPriceRequestDTO;
import com.shop.dao.ShopMemberItemDAO;
import com.shop.util.NaverShoppingSearch;
import com.shop.util.PagiNation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ShopMemberItemService {

    private final NaverShoppingSearch naverShoppingSearch;
    private final ShopMemberItemDAO shopMemberItemDAO;

    //상수
    private final int SUCCESS = 1;

    //네이버 API를 사용하여 데이터를 List로 반환
    public List<ItemDTO> findItemList(String query){
        //사용자가 입력한 keyoword로 API를 이용해 값을 가져온 결과를 저장
        String response = naverShoppingSearch.searchResult(query);
        //저장한 결과값에서 item만 추출해 List에 담아 반환
        return naverShoppingSearch.fromJsonItem(response);
    }//findItemList

    //사용자가에게 DTO를 받아 DB에 넘겨주고 결과값을 처리한다.
    public String saveItem(MyItemDTO myItemDTO){
        int result = shopMemberItemDAO.saveItem(myItemDTO);
        if(result == SUCCESS){
            shopMemberItemDAO.commit();
        }//end if
        return String.valueOf(result);
    }//saveItem

    //사용자의 id를 받아 등록된 상품의 갯수 알아오기
    public int countRecord(String mem_id){
        return shopMemberItemDAO.countRecord(mem_id);
    }//countRecord

    //아이디와 페이징 객체를 받아 DB를 조회 후 List 반환
    public List<MyItemDTO> findAllPaging(String mem_id,PagiNation pagiNation){
        //사용자 아이디로 등록된 상품 갯수 알아오기
        int count = countRecord(mem_id);
        //pagiNation의 값들을 Map에 저장하여 DAO로 넘긴다.
        Map<String,Object> pageMap = new HashMap<>();
        pageMap.put("mem_id",mem_id);
        pageMap.put("startSearch",pagiNation.getStartSearch());
        pageMap.put("endSearch",pagiNation.getEndSearch());

        //DAO에 값 넘겨준다.
        return shopMemberItemDAO.findAllPaging(pageMap);
    }
    

    //등록되어 있는 상품 삭제
    public String deleteItem(int item_no) {
        int result = shopMemberItemDAO.delete(item_no);
        if(result == SUCCESS){
            shopMemberItemDAO.commit();
        }//end if
        return String.valueOf(result);
    }

    //등록되어 있는 최저가 수정
    public String updateItem(UpdateMyPriceRequestDTO updateMyPriceRequestDTO) {
        int result = shopMemberItemDAO.update(updateMyPriceRequestDTO);
        if(result == SUCCESS){
            shopMemberItemDAO.commit();
        }//end if
        return String.valueOf(result);
    }


}
