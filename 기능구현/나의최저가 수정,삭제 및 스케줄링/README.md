등록한 상품 최저가 수정,삭제 및 스케줄링
===

## 수정 및 삭제

<br>

상품이 등록되고 상품이 보여지는 작업까지 완료했으면 이제 등록된 상품에 대해서 수정 및 삭제기능이 필요할 것이다. 먼저는 등록된 상품에 대해서 설정한 최저가를 수정하는 작업을 진행해보자.  <br>

수정하기 전 상품이 등록되어 있는 화면이다.

* 사진 

    <img src = https://user-images.githubusercontent.com/74294325/111323432-3618ef00-86ad-11eb-8988-6f18c2ac9f47.PNG>

<br>

현재 상품에는 내가 설정한 최저가 가격이 450,000원으로 설정이 되어있다. 여기서 최저가 수정 버튼을 누르게 되면 아래와 같은 입력창이 나온다.

* 사진

    <img src = https://user-images.githubusercontent.com/74294325/111323765-84c68900-86ad-11eb-8e2a-99c9109d8bce.PNG>

<br>

이렇게 입력 숨어있던 입력창이 나오게 되고 입력을 하게되면 ajax을 통해 controller에 요청을 하게되고 수정한 값이 반영되면서 상품 리스트가 다시 로딩된다. 아래의 사진은 수정 후 모습이다. 최저가를 620,000원으로 설정해 보겠다.

* 사진

    <img src = https://user-images.githubusercontent.com/74294325/111324658-50070180-86ae-11eb-980d-990aeb48e37a.PNG>

<br>

지정한 가격이 잘 반영되고 내가 지정한 최저가보다 상품가격이 낮으면 최저가 표시도 나타나는 것을 잘 확인할 수 있다. 아래는 update를 요청하는 코드이다.

* script

    ```javascript
    const data = {
            myPrice : updatePrice,
            item_no : item_no
        }
        
        //값이 유효할 경우 ajax통신으로 update진행 put방식
        $.ajax({
            url:'/updateMyPrice.do',
            type:'put',
            data:JSON.stringify(data),
            dataType:'json',
            contentType: 'application/json; charset=urf-8'
        }).done(function (response) {
            if(response == loadList.SUCCESS){
                alert('수정에 성공하였습니다.');
                window.location.href='shopList.do';
            }//end if
        })
    ```

간단히 설명하자면 요청을 put방식으로 요청을 하게 되며 수정할 최저가와 상품번호를 data로 넘겨주면 controller에서 DTO로 받아 DB에 전달해줘 update를 진행하게 된다.

<br>

삭제는 수정보다 훨씬 간단하다. 상품이 등록되어 있는 화면에서 삭제 버튼을 누르면 ajax을 통해 삭제를 하게된다. 아래는 삭제를 요청하는 ajax통신이다.

* script

    ```javascript
     $.ajax({
        url:`/delete.do/${item_no}`,
        type: 'delete',
        dataType: 'json'
    }).done(function (response) {
        if(response == loadList.SUCCESS){
            alert('삭제가 완료되었습니다.');
            window.location.href='shopList.do';
        }//end if
    }).fail(function (error) {
        alert(JSON.stringify(error))
    })
    ```

<br>

delete방식으로 요청을 하게되면 RESTful url을 사용해보고자 /delete.do/${item_no}처럼 작성해보았다. 그럼 controller에서는 @PathVariable를 이용하여 파라미터를 받게된다 

* controller

    ```java
    @DeleteMapping("/delete.do/{item_no}")
    @ResponseBody
    public String deleteItem(@PathVariable int item_no){
        return shopMemberItemService.deleteItem(item_no);
    }
    ```

삭제 또한 받은 상품번호를 db에 전달하여 상품을 삭제하게 된다.

<br>

---

<br>


## 스케줄링

스케줄링을 이용하여 지정한 시간에 다시 네이버 쇼핑을 검색하여 사용자가 등록한 상품의 상품가격을 최신화 한다. 스케줄링에 사용되는 class는 Bean으로 등록되어 있어야 하기 때문에 @Component로 스프링이 로딩될 때 Bean으로 등록되게 된다. 자세한 건 이전 정리글 참조 -> [DI](https://github.com/LeeWoooo/TIL/tree/main/spring/DI) <Br>

상품 검색을 매월 매일 새벽 1시에 진행되도록하는 method를 만들어 상품을 초기화 할 것이다.

* method

    ```java
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
    ```

1초의 시간을 딜레이 시킨 이유는 API를 짧은시간에 여러번 요청하기 때문에 요청할 때마다 지연시간을 부여한 것이다.  <br>

스케줄링 코드는 간단하다. 네이버 쇼핑을 담당하는 class와 DAO를 DI로 주입받아 전체 상품을 검색한 후 상품의 제목으로 네이버 쇼핑을 검색하고 가장 상위에 있는 상품의 가격을 받아와 업데이트를 진행해주면 되는 것이다.



