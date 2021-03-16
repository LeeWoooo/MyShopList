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
        }).fail(function (error){
            alert(JSON.stringify(error));
        })
    },
    priceFormat : function (price){
        //상품 가격을 3자리 수마다 ,를 부여
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
}

loadList.init(1);