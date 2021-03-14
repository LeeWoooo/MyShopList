package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ShopListController {

    @GetMapping("/shopList.do")
    public String shopList(HttpSession httpSession , Model model){
        String mem_id = (String)httpSession.getAttribute("mem_id");
        model.addAttribute("mem_id",mem_id);
        return "shopList";
    }
}
