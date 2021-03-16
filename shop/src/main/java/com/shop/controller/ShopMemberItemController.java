package com.shop.controller;

import com.google.gson.Gson;
import com.shop.controller.dto.ItemDTO;
import com.shop.controller.dto.MyItemDTO;
import com.shop.controller.dto.MyItemResponseDTO;
import com.shop.controller.dto.PageNumberDTO;
import com.shop.service.ShopMemberItemService;
import com.shop.util.PagiNation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ShopMemberItemController {

    private final ShopMemberItemService shopMemberItemService;

    @GetMapping("/shopList.do")
    public String shopList(HttpSession httpSession,Model model){
        model.addAttribute("mem_id",httpSession.getAttribute("mem_id"));
        return "shopList";
    }

    //사용자가 로그인 했을 경우 사용자가 등록한 최저가 리스트를 보여준다.
    @PostMapping("/loadList.do")
    @ResponseBody
    public String loadList(HttpSession httpSession , Model model, @RequestBody PageNumberDTO pageNumberDTO){
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

        //ajax통신으로 paging처리하기위해 JSON으로 변환 후 넘겨준다.
        return new Gson().toJson(new MyItemResponseDTO(pagiNation.getTotalPage(),myItemList));
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

}
