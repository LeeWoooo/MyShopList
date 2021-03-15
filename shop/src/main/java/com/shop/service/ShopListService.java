package com.shop.service;

import com.shop.controller.dto.ItemDTO;
import com.shop.controller.dto.MyItemDTO;
import com.shop.util.NaverShoppingSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopListService {

    private final NaverShoppingSearch naverShoppingSearch;

    public List<ItemDTO> findItemList(String query){
        //사용자가 입력한 keyoword로 API를 이용해 값을 가져온 결과를 저장
        String response = naverShoppingSearch.searchResult(query);
        //저장한 결과값에서 item만 추출해 List에 담아 반환
        return naverShoppingSearch.fromJsonItem(response);
    }

//    public String saveItem(MyItemDTO myItemDTO){
//        //현재 로그인 되어있는 유저의 아이디를 세션에서 가져와서 넣어준다.
//
//
//    }

}
