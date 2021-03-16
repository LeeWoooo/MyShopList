//화면 넘어오면 init에 1을 넣어 실행한 후 밑에 페이지 번호들을 누를 때마 마다 다른 범위의 게시글들을 가져온다.
let loadList = {
    init : function (pageNumber){
        //페이지 번호를 넣어 ajax통신을 하여 값을 가져온다.
        let data = {pageNumber : pageNumber}
        $.ajax({
            url:'/loadList.do',
            type:'post',
            data:JSON.stringify(data),
            dataType:'json',
            contentType:'application/json; charset=utf-8'
        }).done(function (response){
            //결과로는 MyItemResponseDTO로 오게된다. 필드에는 사용자가 등록한 상품정보 List와 총 페이지 수가 있다.
            //먼저 사용자 list초기화
            $('#load-myItem').empty();
            //응답에는 페이지의 총 수와 사용자가 등록한 item이 넘어온다.
            //넘어온 값중 itemList를 변수에저장
            let itemList = response.myItemList;
            //페이지의 총 수 저장
            let totalPage = response.totalPage;
            //받아온 값을 이용하여 태그 생성 최저가는 설정한 가격이 상품가격보다 높을 경우에만 보여진다
            $.each(itemList,function (index,item){
                $('#load-myItem')
                    .append(`<div class="load-card">
                                <div class="card">
                                    <img class="card-img-top" src="${item.image}" alt="Card image" style="width:300px">
                                    <div class="card-body">
                                        <h6 class="card-title">상품가격: ${loadList.priceFormat(item.lprice)} 내 최저가: ${loadList.priceFormat(item.myPrice)}</h6>
                                        <p class="card-text">${item.title}</p>
                                        <a href="${item.link}" class="btn btn-dark" target="_blank">구매링크</a>
                                        <button class="btn btn-danger ${item.lprice <= item.myPrice ? '' : 'none'}">최저가</button>
                                        <div id="update-inputForm-${index}" class="none">
                                            <div>
                                                <input type="text" id="updatePrice-${index}" class="form-control" placeholder="수정할 최저가를 입력해주세요." style="margin: 10px 0 10px 0">
                                            </div>
                                            <button class="btn btn-dark" id="updatePrice-saveBtn-${index}" onclick="loadList.updateMyPrice(${index},${item.item_no})">수정하기</button>
                                            <button class="btn btn-dark" id="updatePrice-cancelBtn-${index}" onclick='loadList.cancelUpdate(${index})'>취소</button>
                                        </div>
                                        <div style="margin-top: 10px">
                                             <button class="btn btn-dark" id="update-myPriceBtn-${index}" onclick="loadList.showUpdateForm(${index})">최저가수정</button>
                                             <button class="btn btn-dark" id="delete-myItemBtn-${index}" onclick="loadList.deleteItem(${item.item_no})">삭제</button>   
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
        }).fail(function (error){
            alert(JSON.stringify(error));
        })
    },
    priceFormat : function (price){
        //상품 가격을 3자리 수마다 ,를 부여
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    //상품 번호를 받아서 삭제
    },deleteItem : function (item_no){
        $.ajax({
            url:`/delete.do/${item_no}`,
            type: 'delete',
            dataType: 'json'
        }).done(function (response) {
            alert(response);
            if(response == loadList.SUCCESS){
                alert('삭제가 완료되었습니다.');
                window.location.href='shopList.do';
            }//end if
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    },
    //최저가 수정하기가 눌렸을 경우
    showUpdateForm : function (index) {
        $(`#delete-myItemBtn-${index}`).addClass('none');
        $(`#update-myPriceBtn-${index}`).addClass('none');
        $(`#update-inputForm-${index}`).removeClass('none');
    },
    //최저가 수정을 눌렀다가 취소했을 경우
    cancelUpdate : function (index){
        $(`#update-inputForm-${index}`).addClass('none');
        $(`#delete-myItemBtn-${index}`).removeClass('none');
        $(`#update-myPriceBtn-${index}`).removeClass('none');
    },
    updateMyPrice : function (index,item_no){
        let updatePrice = $(`#updatePrice-${index}`).val();
        if(updatePrice == '' || isNaN(updatePrice)){
            alert('수정할 가격을 숫자로 입력해주세요');
            $(`#updatePrice-${index}`).focus();
            return;
        }//end if

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
    },
    SUCCESS : 1
}

loadList.init(1);