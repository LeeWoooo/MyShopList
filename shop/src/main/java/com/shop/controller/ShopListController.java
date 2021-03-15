package com.shop.controller;

import com.shop.controller.dto.ItemDTO;
import com.shop.controller.dto.MyItemDTO;
import com.shop.service.ShopListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ShopListController {

    private final ShopListService shopListService;

    //사용자가 로그인 했을 경우 사용자가 등록한 최저가 리스트를 보여준다.
    @GetMapping("/shopList.do")
    public String shopList(HttpSession httpSession , Model model){
        String mem_id = (String)httpSession.getAttribute("mem_id");
        model.addAttribute("mem_id",mem_id);
        return "shopList";
    }//shopList

    //사용자가 상품 검색을 눌렀을 경우
    @GetMapping("/search.do")
    public String searchPage(HttpSession httpSession, Model model){
        model.addAttribute("mem_id",httpSession.getAttribute("mem_id"));
        return "searchGoods";
    }//searchPage

    //사용자가 상품명을 입력하고 검색을 눌렀을 때
    @GetMapping("api/search.do")
    //JSON으로 응답할것이기 때문에 ResponseBody
    @ResponseBody
    public List<ItemDTO> searchItem(@RequestParam String query){
        return shopListService.findItemList(query);
    }//searchItem

    //사용자가 나만의 리스트에 저장을 했을 때 저장 처리
    @PostMapping("api/saveItem")
    @ResponseBody
    public String saveItem(@RequestBody MyItemDTO myItemDTO){
        String result ="1";
        System.out.println(myItemDTO.getMem_id());
        System.out.println(myItemDTO.getTitle());
        System.out.println(myItemDTO.getLink());
        System.out.println(myItemDTO.getImage());
        System.out.println(myItemDTO.getLprice());
        System.out.println(myItemDTO.getMyPrice());
        return result;
    }

}
