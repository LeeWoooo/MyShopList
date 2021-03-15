네이버 검색
===

네이버에서 제공하는 API를 이용하여 사용자가 입력한 keyword로 검색하여 상품의 데이터를 받아온다. <br>

먼저 로그인을 하고 들어가면 사용자에게 보여지는 화면이다.

* 화면 

    <img src = https://user-images.githubusercontent.com/74294325/111149263-02b46280-85d0-11eb-9e4f-632ca5135ede.PNG>

<br>

상품명을 검색을 눌러 요청을 하면 네이버 API에서 전해주는 data를 사용자에게 보여주는 과정이다.

### RestTemplate를 통해 요청해서 DATA받기

[RestTemplate 사용법](https://github.com/LeeWoooo/TIL/tree/main/spring/Resttemplate) 이전 정리해두었던 글을 참고하여 작성을 하였다. 요청을 할 때 응답이 JSON형태의 String으로 오는데 [JSON In Java](https://mvnrepository.com/artifact/org.json/json)을 통해 JSON 객체로 변환해주고 거기서 내가 필요한 값을 뽑아온다. 코드로 확인해보자.

* JSON객체로 변환하기

    ```java
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
    ```

<br>

간단하게 코드설명을 붙이면 String 타입의 JSON을 객체로 변환한다. 변환한 JSON에서 key값이 items인 JSON 배열을 뽑아서 안에있는 상품 DATA를 가지고 ItemDTO를 생성하여 LIST에 담아 반환해준다.

### 반환한 LIST를 view에 뿌려주기

사용자가 상품 keyword를 입력하면 유효성을 검사 후 ajax을 통해 List를 가져와서 view에 뿌려준다.

* 스크립트 코드

    ```javascript
    $.ajax({
        url:`/api/search.do?query=${query}`,
        type:'get'
    }).done(function (response){
        //먼저 검색창에 있었던 리스트 및 검색창 초기화 한다.
        $('#search-goodsBox').empty();
        $('#searchQuery').val('');
        //검색결과를 태그를 만들어 붙여준다.
        //최저가 입력창은 버튼을 눌렀을 경우에만 보여준다.
        $.each(response,function (index,item){
            $('#search-goodsBox')
                                .append(
                                    `<div class="search-card">
                                        <div class="card">
                                            <img class="card-img-top" src="${item.image}" alt="Card image" style="width:300px">
                                            <div class="card-body">
                                                <h4 class="card-title">${priceFormat(item.lprice)}</h4>
                                                <p class="card-text">${item.title}</p>
                                                <a href="${item.link}" class="btn btn-dark" target="_blank">구매링크</a>
                                                <button class="btn btn-dark" id="myPrice-inputBtn-${index}" onclick="addMyPrice(${index})">최저가등록하기</button>
                                                <div id="myPrice-inputForm-${index}" class="none">
                                                    <div>
                                                        <input type="text" id="myPrice-${index}" name="myPrice" class="form-control" placeholder="최저가를 입력해주세요." style="margin: 10px 0 10px 0">
                                                    </div>
                                                    <button class="btn btn-dark" id="myPrice-saveBtn-${index}" onclick='addItem(${JSON.stringify(item)},${index})'>리스트에 등록</button>
                                                    <button class="btn btn-dark" id="myPrice-cancelBtn-${index}" onclick='cancelAdd(${index})'>취소</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>`
                                )// end append
        })//end each
    }).fail(function (error){
        alert(JSON.stringify(error));
    })//end ajax
    ```

<br>

반복문을 돌려 태그를 작성한다 이 때 백틱(`)을 사용하여 중간중간 값을 List에 담겨서 넘어온 상품정보를 담아준다. 각각 id값에 index값을 부여하여 각각 핸들링할 수 있도록 처리를 하였으며 검색을 했을 때는 아직 원하는 최저가를 입력하는 창이 사용자에게 보여지지 않는다.(class에 none을 추가) 아래 사진은 검색을 했을 때 사용자에게 보여질 화면이다.

* 화면 

    <img src = https://user-images.githubusercontent.com/74294325/111157300-14026c80-85da-11eb-89f9-ac73ff7ebae7.PNG>

<br>

### 사용자에게 최저가값을 입력받아 Controller에서 값 받기

각각 card에서 최저가 등록을 누르게 되면 "최저가등록하기"버튼이 사라지고 최저가 입력창,등록,취소 버튼이 보여지게 된다.

* 스크립트 코드

    ```javascript
    let addMyPrice = function (index){
    //최저가 입력하는 버튼 가리기
    $(`#myPrice-inputBtn-${index}`).addClass('none');
    //최저가 입력하는 form보여주기
    $(`#myPrice-inputForm-${index}`).removeClass('none');
    }

    let cancelAdd = function (index){
        //취소를 누르면 다시 최저가 입력하는 form 지우기
        $(`#myPrice-inputForm-${index}`).addClass('none');

        //최저가 입력하는 버튼 보여주기
        $(`#myPrice-inputBtn-${index}`).removeClass('none');
    }
    ```

<br>

최저가등록하기를 누른 화면이다.

* 화면

    <img src = https://user-images.githubusercontent.com/74294325/111157896-c20e1680-85da-11eb-8762-9f571d7ab62c.PNG>

<br>

사용자가 원하는 최저가를 입력하고 리스트에 등록을 누르게 되면 ajax통신을 통해 controller로 값을 넘기게 되며 controller는 DTO를 통해 값을 받는다.

* 스크립트

    ```javascript
    //유효한 값이라면 상품 정보를 변수에 저장을 하고
    let saveItem = item;
    //객체에 myPrice 필드를 추가한다.
    saveItem.myPrice = parseInt(myPrice);

    //세선에 저장되어있는 id를 header에 숨겨놨다 이것을 가져다가 사용하자
    //setter을 이용하여 값을 넣고싶지 않았기 떄문
    let mem_id = $('#session-mem_id').val();
    saveItem.mem_id = mem_id;

    //이 후 ajax통신을 통해 상품 저장
    $.ajax({
        url:'/api/saveItem',
        type: 'post',
        data: JSON.stringify(saveItem),
        dataType:'json',
        contentType:'application/json; charset=utf-8'
    }).done(function (response){
        alert(response);
    }).fail(function (error){
        alert(JSON.stringify(error));
    })
    ```

* Controller에서 받은 값 정보

    <img src = https://user-images.githubusercontent.com/74294325/111158766-c5ee6880-85db-11eb-899c-47d01d2608dd.PNG>

<br>


