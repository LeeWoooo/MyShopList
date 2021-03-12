let  post = {
    init : function (){
        let _this = this;
        $('#postBtn').on('click',function (){
            _this.addPost();
        })
    },
    addPost : function (){
        new daum.Postcode({
            oncomplete: function(data) {
                //api를 사용해 받아온 도로명 주소를 변수에 선언하고
                let addr = data.roadAddress;
                //만약 사용자가 도로명 주소가 아닌 지번 주소를 사용 했다면 지번주소를 넣어준다.
                if(data.userSlectedType !== 'R'){
                    addr = data.jibunAddress;
                }
                //이 후 회원가입 창에 값을 넣어준다.
                //우편번호 넣기
                $('#mem_zipCode').val(data.zonecode)
                //주소 넣기
                $('#mem_address').val(addr)
                //다음 상세주소로 포커싱
                $('#mem_detailAddress').focus();
            }
        }).open();
    }
}
post.init();