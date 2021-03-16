상품 저장 및 ajax을 이용한 페이징 처리
===

## 상품 저장

[네이버 검색](https://github.com/LeeWoooo/MyShopList/tree/master/%EA%B8%B0%EB%8A%A5%EA%B5%AC%ED%98%84/%EB%84%A4%EC%9D%B4%EB%B2%84%EA%B2%80%EC%83%89%EA%B8%B0%EB%8A%A5)이전 글에서 사용자가 값을 입력하여 저장을 누르면 controller에 값이 넘어오는 것 까지 확인했다. 이제 그 값을 DTO에 저장을 하고 DAO로 넘겨준다. 저장이 완료후 사용자의 상품 리스트로 이동을 하게된다.

* controller

    ```java
    //사용자가 나만의 리스트에 저장을 했을 때 저장 처리
    @PostMapping("api/saveItem")
    @ResponseBody
    public String saveItem(@RequestBody MyItemDTO myItemDTO){
        return shopMemberItemService.saveItem(myItemDTO);
    }
    ```

<BR>

---

<br>

## 상품 불러오기 및 ajax을 이용한 페이징 처리

상품 리스트로 페이지가 이동되었으면 사용자가 저장한 상품 리스트들을 보여줘야 한다. 먼저 페이징 처리를 위한 class를 만들어 페이징 처리를 해준다. class를 객체로 생성할 때 생성자 매개변수로 사용자가 저장한 상품의 갯수, 현재페이지번호를 넣어준다. 이전정리한 글 [SpringBoot,MyBatis 환경에서 페이징 처리](https://github.com/LeeWoooo/SIST_Class/tree/master/spring/spring_MyBatis%ED%99%98%EA%B2%BD_paging%EC%B2%98%EB%A6%AC)를 참조(이 글에서는 ajax이 아닌 view에서 처리)

* 페이징 처리를 위한 class의 생성자

    ```java
    public PagiNation(int count,int pageNumber) {
        //한 페이지에 6개씩 보여진다.
        this.pageSige = 6;
        //view에 보여질 총 페이지 수 구하기
        this.totalPage = (int)Math.ceil(count/(double)pageSige);
        //범위구하기
        this.startSearch = (pageNumber*pageSige)-pageSige;
        this.endSearch = startSearch + pageSige +1 ;
    }
    ```

<br>

이 후 사용자의 id와 검색범위를 받아 조회를 하는 query문을 작성한다. Mapper파일에 작성을 하며 반환형으로는 DTO를 이용하여 받는다.

* query문

    ```xml
     <select id="findAllPaging" resultType="com.shop.controller.dto.MyItemDTO">
        <![CDATA[
        select ITEM_NO, MEM_ID, TITLE, LINK, IMAGE, LPRICE, MYPRICE, SEQ
        from (SELECT ITEM_NO, MEM_ID, TITLE, LINK, IMAGE, LPRICE, MYPRICE, ROW_NUMBER()OVER(ORDER BY ITEM_NO DESC) SEQ
                FROM SHOPUSERITEM
                WHERE ITEM_NO > 0 AND MEM_ID = #{mem_id})
        where SEQ > #{startSearch} AND SEQ < #{endSearch}
        ]]>
    </select>
    ```

<br>

이제 준비가 끝났으니 요청을 하고 그에 대한 응답을 해보자. 

<Br>

---

<br>

### 요청

ajax으로 요청을 할 때 data에 페이지 번호를 추가하여 요청을 한다. 처음 페이지로 이동되었을 때는 페이지 번호를 1로하여 넘겨준다. 

* script    

    ```javascript
    let loadList = {
    init : function (pageNumber){
        let data = {pageNumber : pageNumber}
        $.ajax({
            url:'/loadList.do',
            type:'post',
            data:JSON.stringify(data),
            dataType:'json',
            contentType:'application/json; charset=utf-8'
        }).done(function (response){
        ,,,
    loadList.init(1);
    ```

<br>

### 응답

페이지 번호가 넘어오면 controller에서 값을 받아 사용자가 저장한 상품의 갯수를 가지고 페이징 처리 객체를 생성한다. 이 후 DB를 검색해 List를 반환받는다. 이 때 응답을 해줘야 할 것이 2가지인데 하나는 totalPage이고 하나는 DB에서 가져온 List이다. 그렇기 때문에 응답을 해줄 DTO를 만들어 값을 할당하고 JSON으로 변환하여 응답을 해줄 것이다.

* DTO

    ```java
    @AllArgsConstructor
    @Getter
    public class MyItemResponseDTO {
        private int totalPage;
        private List<MyItemDTO> myItemList;
    }
    ```

* controller

    ```java
     //사용자가 로그인 했을 경우 사용자가 등록한 최저가 리스트를 보여준다.
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
    ```

<br>

MyItemResponseDTO로 응답을 해주었으니 js에서 응답받은 값으로 태그를 그려준다.

* script

    ```javascript
     //먼저 사용자 list초기화
    $('#load-myItem').empty();
    //응답에는 페이지의 총 수와 사용자가 등록한 item이 넘어온다.
    //넘어온 값중 itemList를 변수에저장
    let itemList = response.myItemList;
    //페이지의 총 수 저장
    let itemList = response.totalPage;
    //받아온 값을 이용하여 태그 생성 최저가는 설정한 가격이 상품가격보다 높을 경우에만 보여진다
    $.each(itemList,function (index,item){
        $('#load-myItem')
            .append(`<div class="load-card">
                        <div class="card">
                            <img class="card-img-top" src="${item.image}" alt="Card image" style="width:300px">
                            <div class="card-body">
                                <h4 class="card-title">${loadList.priceFormat(item.lprice)}</h4>
                                <p class="card-text">${item.title}</p>
                                <a href="${item.link}" class="btn btn-dark" target="_blank">구매링크</a>
                                <button class="btn btn-danger ${item.lprice <= item.myPrice ? '' : 'none'}">최저가</button>
                                <div style="margin-top: 10px">
                                        <button class="btn btn-dark" >최저가수정</button>
                                        <button class="btn btn-dark" >삭제</button>   
                                </div>
                            </div>
                        </div>
                    </div>`)
    })//end each
    //페이지 값 초기화
    $('#pageUl').empty();
    //넘어온 totalPage값을 가지고 반복문 돌리면서 page값 만들기
    for(let i = 1; i <= totalPage; i++){
        $('#pageUl')
            .append(`
                    <li class="page-item"><a class="page-link" onclick="loadList.init(${i})">${i}</a></li>
            `)
    }//end for
    ```

<Br>

여기서 JSON객체가 response값으로 넘어오고 안에는 myItemList,totalPage 가지고 있다. myItemList를 반복문 돌리며 태그를 만들어 추가해주고 totalPage까지 반복문을 돌려 페이지 번호를 만들어준다. 여기서 페이지 번호가 눌릴때 마다 페이지 번호를 가지고 다시 요청을 하여 범위의 값을 가져오게 된다. 여기서 최저가는 조건부로 보여지도록 하는데 상품의 가격이 사용자가 입력한 최저가보다 작거나 같을 경우에 보여지게 된다.

<br>

* 결과 화면

    <img src = https://user-images.githubusercontent.com/74294325/111242624-e3a2e880-8642-11eb-92a5-ea6915385260.PNG>
