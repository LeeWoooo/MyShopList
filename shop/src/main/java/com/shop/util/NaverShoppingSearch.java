package com.shop.util;

import com.shop.controller.dto.ItemDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class NaverShoppingSearch {

    public String searchResult(String query){
        //ARC에서 제공해주는 code
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "v7MX0sUJttnYIe7CzCzV");
        headers.add("X-Naver-Client-Secret", "TjhPmX3Dkm");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?query="+query+"&display=12", HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
        //System.out.println("Response status: " + status);
        //System.out.println(response);
        return response;
    }//searchResult

    public List<ItemDTO> fromJsonItem(String response){
        //네이버 검색 결과(JSON객체)를 문자열로 받았기 때문에 다시 JSON으로 변경해준다.
        JSONObject searchResult = new JSONObject(response);
        //얻어온 결과에서 내가 필요한 정보인 Item을 뽑아온다.
        JSONArray itemArray = searchResult.getJSONArray("items");

        //반환할 List의 틀을 만들고
        List<ItemDTO> itemDTOList = new ArrayList<>();
        //얻어온 JsonArray를 가져와서 반복문을 돌려 DTO생성
        for (int i = 0; i<itemArray.length(); i++){
            JSONObject itemInfo = itemArray.getJSONObject(i);
            ItemDTO itemDTO = new ItemDTO(itemInfo);
            itemDTOList.add(itemDTO);
        }//end for

        //반환
        return itemDTOList;
    }//fromJsonItem

}
