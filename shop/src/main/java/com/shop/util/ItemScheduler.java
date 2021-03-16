package com.shop.util;

import com.shop.controller.dto.ItemDTO;
import com.shop.controller.dto.MyItemDTO;
import com.shop.controller.dto.UpdateLpriceRequestDTO;
import com.shop.dao.ShopMemberItemDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class ItemScheduler {

    private final NaverShoppingSearch naverShoppingSearch;
    private final ShopMemberItemDAO shopMemberItemDAO;

    private int updateLpirce;
    private int item_no;

    @Scheduled(cron = "0 0 1 * * *") //매월 매일 1시마다 update
    public void updateLprice() throws InterruptedException{
        System.out.println("상품 가격 업데이트 시작");

        List<MyItemDTO> MyItemList = shopMemberItemDAO.finaAll();

        //반복문을 돌면서 update진행
        for(MyItemDTO myItemDTO : MyItemList){
            //먼저 가져온 상품의 제목으로 네이버 쇼핑을 검색한다.
            List<ItemDTO> itemDTOList = naverShoppingSearch.fromJsonItem(naverShoppingSearch.searchResult(myItemDTO.getTitle()));
            TimeUnit.SECONDS.sleep(1);
            //검색한 것중 가장 위에 나오는 상품의 가격을 가져온다.
            updateLpirce = itemDTOList.get(0).getLprice();
            System.out.println(updateLpirce);
            //상품의 번호를 가져온다.
            item_no =myItemDTO.getItem_no();
            System.out.println(item_no);
            shopMemberItemDAO.updateLprice(new UpdateLpriceRequestDTO(item_no,updateLpirce));
        }//end for
        System.out.println("상품 가격 업데이트 종료");
    }

}
