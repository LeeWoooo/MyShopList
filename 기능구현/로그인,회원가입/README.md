로그인 및 회원가입
===

각각의 회원의 최저가 리스트를 보여주는 것이 목적이기 때문에 먼저 회원가입,로그인 기능이 있어야한다. <br>

가장 처음 사용자가 유입되면 회원가입과 로그인 화면을 보여준다. <br>

* 화면

    <img src = https://user-images.githubusercontent.com/74294325/111017883-486b0280-83f9-11eb-8064-fa349d2f1c6f.PNG>

<br>

이후 회원가입과 로그인을 눌렀을 때 처리하는 controller를 만들어 사용자가 선택한 것에 따른 view를 보여준다.

* controller

    ```java
    @Controller
    public class IndexController {
        @GetMapping("/signUp.do")
        public String signUpForm(){
            return "signUp";
        }

        @GetMapping("/signIn.do")
        public String signInFrom(){
            return "signIn";
        }
    }
    ```

<Br>

---

<br>

## 회원가입

먼저 회원가입기능을 살펴보면 회원가입으로 이동했을 경우 초기 화면이다.

* 화면

    <img src = https://user-images.githubusercontent.com/74294325/111017946-9da71400-83f9-11eb-8262-72f59ce44300.PNG width=500 height = 650>

<Br>

여기서 중요하게 살펴볼 것은 회원가입을 할 때 유효성 검사 및 다음 API를 사용한 주소 검색이다.

<br>

---

<Br>

### 다음 API를 사용한 주소 검색

다음 API를 사용하기 위해 [다음우편번호서비스](https://postcode.map.daum.net/guide) 에 접속하여 사용법을 확인 한다. 

* 사용법 

    <img src = https://user-images.githubusercontent.com/74294325/111018030-1017f400-83fa-11eb-8e63-e7ec12a159dd.PNG>

<Br>

이를 적용한 나의 코드를 확인해보면

* 코드 

    ```javascript
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="js/post.js"></script>
    ```

* post.js
    ```javascript
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
    ```

<Br>

위의 코드를 간단히 설명하자면 회원가입창에서 우편번호 버튼이 클릭되면 이벤트를 실행하고 사용자가 검색하는 값을 회원가입 창에 넣어주는 것이다. 여기서 사용자가 지번주소 혹은 도로명 주소를 사용할 수 있기 때문에 한번 확인 후 값을 넣어준다.

<br>

### 유효성 검사

사용자가 입력한 값을 유효성 검사하기 위해 정규식을 사용하였다. 다음이 내가 사용자가 입력한 값을 검사하기 위해 사용한 정규식이다.

* 정규식

    ```javascript
    let id = /^[a-z0-9][a-z0-9_\-]{4,11}$/;
    let pwd = /^[A-Za-z0-9]{6,12}$/;
    let name = /^[가-힣]{2,5}$/;
    let email = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    let phone = /^01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})$/;
    ```

<br>

사용자가 입력을 하고 그 input 태그의 포커싱이 없어질 때 정규식을 test하여 값이 유효하다면 회원가입을 눌렀을 때 commit로 넘어가도록 하였다. test 함수를 이용하여 결과를 boolean값으로 받아서 처리하였다. (자세한 것은 코드를 참고 [코드](https://github.com/LeeWoooo/MyShopList/blob/master/shop/src/main/resources/static/js/signUpValidation.js))

<br>

또한 아이디 및 이메일 또한 유효성 검사를 진행하였는데 아이디는 PK값이고 이메일은 유니크이기 때문에 중복된 아이디나 이메일이 들어간다면 무결성 제약조건에 위배될 것이다. 그렇기 때문에 사용자가 입력한 아이디 혹은 이메일을 중복검사 하여 유효한 값이 들어가도록 처리한다.

* 중복검사 (id)

    ```javascript
    $.ajax({
        url:'/idCheck.do',
        type:"post",
        data:JSON.stringify(data),
        dataType:'json',
        contentType:'application/json; charset=utf-8'
    }).done(function (response){
            //아이디가 중복됬다는 가정에 경고창을 생성하고
            $('#id_check').text('이미 존재하는 아이디 입니다.');
            $('#id_check').css('color','red');
            //만약 중복이 되지 않으면 
            if(response !== EXISTID){
                //사용가능하다고 안내하고
                $('#id_check').text('사용가능한 아이디 입니다.');
                $('#id_check').css('color','blue');
                //사용가능한 아이디라면 입력창에 일기전용을 추가해서 다음으로 넘어간다.
                $('#mem_id').prop('readonly',true);
                validationArr[0] = true;
            }//if
    }).fail(function (error){
        alert(JSON.stringify(error))
    });
    ```

<br>

사용자가 입력한 값을 DTO로 받아서 DB에 조회를 한다. 만약 SELECT값이 1일 경우 아이디가 존재하는 것이기 때문에 이미 존재하는 아이디라고 표시해준 후 사용자가 다시 입력하도록 한다. 여기서 validationArr는 아이디 및 이메일의 중복검사가 통과될때 값을 부여하여 최종 유효성 검사 때 또 한번 검사하게된다.

<br>

---

<br>

이 후 유효한 값들이 입력이 되었다면 ajax통신을 이용하여 요청을 하게 되고 값은 DTO를 이용하여 값을 받았습니다, 

* 요청을 하는 ajax코드

    ```javascript
    $.ajax({
            url:'/signUp.do',
            type: 'post',
            data:JSON.stringify(data),
            contentType: 'application/json; charset=utf8',
            dataType: 'json'
        }).done(function (response){
            //회원가입이 완료되면 로그인 페이지로 이동
            if(response == SIGNUPSUCCESS){
                alert('회원 가입이 완료되었습니다.');
                window.location.href='signIn.do';
            }//end if
        }).fail(function (error){
            alert('오류가 발생하였습니다 잠시 후 다시 시도해주세요.');
            window.location.href='/';
        })
    ```

* controller

    ```java
    @PostMapping("/signUp.do")
    public String signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        return shopMemberService.memberSave(signUpRequestDTO);
    }
    ```

<Br>

이렇게 사용자가 입력한 값이 넘어오면 DB와 연결하여 회원 정보를 저장하고 로그인 페이지로 이동시킨다.

<br>

---

<br>

## 로그인
 
로그인 기능 또한 동일하다 사용자가 로그인버튼을 누르면 화면은 다음과 같다. 사용자가 회원가입을 할 때 사용했던 정규식을 이용하여 사용자가 입력한 값을 검사한 후 유효한 값이라면
controller에게 요청을 하여 로그인 처리를 진행한다. 여기도 회원가입과 동일하게 처리가 되며 DB처리 값에 따라 처리하게 달라지게 된다.

* 로그인 화면 

    <img src = https://user-images.githubusercontent.com/74294325/111019952-d947db00-8405-11eb-9fb5-d9c7ef5cc2d5.PNG>

* ajax통신

    ```javascript
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
    ```

<br>


---

<Br>

## 페이지 모듈화 중 Header 동적인 태그

사용자가 로그인한 상태로 서비스를 이용할 때 header의 nav를 동적으로 표현한다. session이 존재해서 Controller에서 Model에 mem_id값이 실려서 넘어올 때와 아닐 때를 구분하여 nav를 설정

* html

    ```html
    <ul class="navbar-nav">
                {{#mem_id}}
                    <li class="nav-item">
                        <a class="nav-link" href="shopList.do">{{mem_id}}님의 리스트</a>
                        <!--현재 session에 저장되어있는 아이디값 숨겨놓기-->
                        <input type="hidden" value="{{mem_id}}" id="session-mem_id">
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="search.do">상품검색</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="logOut.do">로그아웃</a>
                    </li>
                {{/mem_id}}
                {{^mem_id}}
                <li class="nav-item">
                    <a class="nav-link" href="signIn.do">로그인</a>
                </li>
                {{/mem_id}}
            </ul>
    ```

<br>

mustache 문법을 사용한 조건문 형식을 사용하였으며 mem_id의 값이 있을 때 "{{mem_id}}님의 리스트","상품검색","로그아웃"을 보여준다. <br>

중간에 session에 저장된 값을 다른 html에서 사용하기 위해 hidden으로 값을 저장했다.

* html 

    ```html
    <input type="hidden" value="{{mem_id}}" id="session-mem_id">
    ```

<BR

넘어오는 mem_id가 없다면 로그인만 표현된다.

