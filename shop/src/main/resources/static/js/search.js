const SUCCESS = 1;

let search = {
    init : function (){
        let _this = this;

        $('#searchBtn').on('click',function (){
            _this.searchItem();
        })
    },
    searchItem : function (){
        //사용자가 입력한 상품명을 받고
        const query = $('#searchQuery').val();

        //사용자가 상품명을 입력하지 않았을 경우 알림
        if(query == ""){
            alert('검색하실 상품명을 입력해주세요:)');
            $('#searchQuery').focus();
            return;
        }//end if

        //사용자가 상품명을 입력했다면 ajax통신을 이용하여 상품 목록을 얻어온다.
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
                                                    <h4 class="card-title">${search.priceFormat(item.lprice)}</h4>
                                                    <p class="card-text">${item.title}</p>
                                                    <a href="${item.link}" class="btn btn-dark" target="_blank">구매링크</a>
                                                    <button class="btn btn-dark" id="myPrice-inputBtn-${index}" onclick="search.addMyPrice(${index})">최저가등록하기</button>
                                                    <div id="myPrice-inputForm-${index}" class="none">
                                                        <div>
                                                            <input type="text" id="myPrice-${index}" name="myPrice" class="form-control" placeholder="최저가를 입력해주세요." style="margin: 10px 0 10px 0">
                                                        </div>
                                                        <button class="btn btn-dark" id="myPrice-saveBtn-${index}" onclick='search.addItem(${JSON.stringify(item)},${index})'>리스트에 등록</button>
                                                        <button class="btn btn-dark" id="myPrice-cancelBtn-${index}" onclick='search.cancelAdd(${index})'>취소</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`
                                    )// end append
            })//end each
        }).fail(function (error){
            alert(JSON.stringify(error));
        })//end ajax
    },
    priceFormat : function (price) {
        //상품 가격을 3자리 수마다 ,를 부여
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    addMyPrice : function (index){
        //최저가 입력하는 버튼 가리기
        $(`#myPrice-inputBtn-${index}`).addClass('none');
        //최저가 입력하는 form보여주기
        $(`#myPrice-inputForm-${index}`).removeClass('none');
    },
    cancelAdd : function (index) {
        //취소를 누르면 다시 최저가 입력하는 form 지우기
        $(`#myPrice-inputForm-${index}`).addClass('none');

        //최저가 입력하는 버튼 보여주기
        $(`#myPrice-inputBtn-${index}`).removeClass('none');
    }
    //상품 저장을 누르면 json객체를 받아 내가 입력한 최저가를 추가하여 변수에 저장하는 함수
    ,addItem : function (item,index){
        //사용자가 입력한 최저가 가격을 변수에 선언
        const myPrice = $(`#myPrice-${index}`).val();

        //만약 입력하지 않았다거나 숫자가 아닌 값을 입력했을 경우 return
        if(myPrice == '' || isNaN(myPrice)){
            alert('원하시는 최저가 가격을 숫자로 입력해주세요.');
            $(`#myPrice-${index}`).val('');
            $(`#myPrice-${index}`).focus();
            return;
        }//end if

        //유효한 값이라면 상품 정보를 변수에 저장을 하고
        const saveItem = item;
        //객체에 myPrice 필드를 추가한다.
        saveItem.myPrice = parseInt(myPrice);

        //세선에 저장되어있는 id를 header에 숨겨놨다 이것을 가져다가 사용하자
        //setter을 이용하여 값을 넣고싶지 않았기 떄문
        const mem_id = $('#session-mem_id').val();
        saveItem.mem_id = mem_id;

        //이 후 ajax통신을 통해 상품 저장
        $.ajax({
            url:'/api/saveItem',
            type: 'post',
            data: JSON.stringify(saveItem),
            dataType:'json',
            contentType:'application/json; charset=utf-8'
        }).done(function (response){
            if(response == SUCCESS){
                alert('상품등록이 완료되었습니다.');
                window.location.href='shopList.do';
            }//end if
        }).fail(function (error){
            alert(JSON.stringify(error));
        })
    }
}


search.init();