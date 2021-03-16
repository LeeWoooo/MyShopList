package com.shop.controller;


import com.shop.controller.dto.*;
import com.shop.service.ShopMemberItemService;
import com.shop.util.PagiNation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

//회원이 로그인 후 상품을 등록하고 검색하는 것을 처리하는 컨트롤러
@RequiredArgsConstructor
@Controller
public class ShopMemberItemController {

    private final ShopMemberItemService shopMemberItemService;

    //로그인 시 상품리스트 페이지로 이동
    @GetMapping("/shopList.do")
    public String shopList(HttpSession httpSession,Model model){
        model.addAttribute("mem_id",httpSession.getAttribute("mem_id"));
        return "shopList";
    }

    //페이징 처리를 하여 사용자에게 등록한 상품 리스트를 보여준다.
    @PostMapping("/loadList.do")
    @ResponseBody
    public MyItemResponseDTO loadList(HttpSession httpSession , Model model, @RequestBody PageNumberDTO pageNumberDTO){
        //여기서는 서비스로 pageNumber와 세션 아이디를 넘겨서 페이징 처리를 한다.
        String mem_id = (String)httpSession.getAttribute("mem_id");
        //사용자 아이디로 저장된 레코드 수 불러오기
        int count = shopMemberItemService.countRecord(mem_id);

        //페이징 처리하는 객체를 생성하고
        PagiNation pagiNation = new PagiNation(count,pageNumberDTO.getPageNumber());

        //페이징 처리된 List가져오기
        List<MyItemDTO> myItemList = shopMemberItemService.findAllPaging(mem_id, pagiNation);

        //세션에 저장되어 있는 id값 넘겨주기
        model.addAttribute("mem_id",mem_id);

        //ajax통신으로 paging처리하기위해 JSON으로 넘겨준다.
        return new MyItemResponseDTO(pagiNation.getTotalPage(),myItemList);
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
        return shopMemberItemService.findItemList(query);
    }//searchItem

    //사용자가 나만의 리스트에 저장을 했을 때 저장 처리
    @PostMapping("api/saveItem")
    @ResponseBody
    public String saveItem(@RequestBody MyItemDTO myItemDTO){
        return shopMemberItemService.saveItem(myItemDTO);
    }

    //상품번호와 수정 가격을 받아서 등록한 최저가 수정
    @PutMapping("/updateMyPrice.do")
    @ResponseBody
    public String updateItem(@RequestBody UpdateMyPriceRequestDTO updateMyPriceRequestDTO){
        return shopMemberItemService.updateItem(updateMyPriceRequestDTO);
    }

    //상품번호를 받아서 삭제
    //DeleteMapping사용
    @DeleteMapping("/delete.do/{item_no}")
    @ResponseBody
    public String deleteItem(@PathVariable int item_no){
        return shopMemberItemService.deleteItem(item_no);
    }
}
