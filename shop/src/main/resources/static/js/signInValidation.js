//로그인 성공 시 DB에서 1을 반환하기 때문에 상수로 처리 후 가독성 증가
const SIGNINSUCCESS = 1;

//정규식
const id = /^[a-z0-9][a-z0-9_\-]{4,11}$/;
const pwd = /^[A-Za-z0-9]{6,12}$/;

//로그인 버튼을 눌렀을시 유효성 검사 및 로그인 처리
let signInValidation = {
    //init 함수 선언
    init : function (){
        let _this = this;
        $('#signInBtn').on('click',function (){
            _this.signIn();
        });
    },
    signIn : function (){
        //먼저 사용자가 입력한 값으로 js 객체 생성
        let data = {
            mem_id : $('#mem_id').val(),
            mem_pwd : $('#mem_pwd').val()
        };

        //유효성 검사
        if(!id.test(data.mem_id)){
            alert('아이디 형식이 올바르지 않습니다.');
            return;
        }//end if

        if(!pwd.test(data.mem_pwd)) {
            alert('비밀번호 형식이 올바르지 않습니다.');
            return;
        }//end if

        //만약 유효한 값들을 입력했다면 ajax통신을 통해 db와 연동
        $.ajax({
            url:'/signIn.do',
            type:'post',
            data:JSON.stringify(data),
            dataType:'json',
            contentType:'application/json; charset=utf-8'
        }).done(function (response){
            //만약 결과값으로 1이 왔을 때 list페이지로 이동
            if(response === SIGNINSUCCESS){
                alert('로그인에 성공하였습니다.');
                window.location.href='shopList.do';
                return;
            }//end if
            //기본값을 실패로 가정
            alert('로그인에 실패하였습니다. 비밀번호 혹은 아이디를 확인해주세요');
            window.location.href='signIn.do';
        }).fail(function (error){
            alert(JSON.stringify(error))
        })//end ajax
    }
}

signInValidation.init();