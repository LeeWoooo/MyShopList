나만의 최저가 리스트
===

## Project 목표

1. 네이버 Open API를 사용한다. 

    * 네이버 쇼핑의 API를 이용하여 상품을 검색하고 상품에 대한 정보를 가지고 온다

2. Ajax의 이용

    * 이전 JSP를 이용해 게시판을 만들고 페이징 처리를 하였지만 JSP를 이용하지 않고 다른 템플릿 엔진(mustache)를 이용하여 view를 처리한다.

    * 다양한 API의 사용 (항상 GET,POST만 사용하는 것이 아닌 PUT과 DELETE 또한 사용)

3. 회원 가입 및 로그인

    * 이전에는 유효성 검사를 하지 않고 회원가입 및 로그인을 처리하였지만 정규식을 이용하여 유효성 검사를 진행하여 회원가입과 로그인을 처리한다.

4. web 3계층

    * Controller - Service - DAO(Repository)에 맞게 코드를 작성한다.

5. 스프링 스케줄링

    * 스케줄링을 이용하여 원하는 시간마다 상품의 가격을 다시 가져와 업데이트를 해준다.

<br>

## 사용기술

Java 1.8, Oracle, SpringBoot,MyBatis,Gradle,BootStrap,LomBox,Mustache

<Br>

---

<Br>

## 프로젝트를 하면서 느낀점

스프링부트와 마이바티스 환경에서 기본적인 웹 게시판을 만드는 것에 대한 자신감이 생겼다. 또한 웹을 만들 때 openAPI를 사용하는 것이 얼마나 편리한지를 알게되었다. 이번 프로젝트에서는 다음API와 네이버 쇼핑 API를 사용해봤지만 앞으로 다양한 API를 사용하며 더욱 풍성한 웹 어플리케이션을 개발할 수 있도록 해야겠다. 또한 이번 프로젝트에서는 jsp를 사용하지 않고 ajax을 이용하여 통신을 하고 DTO를 이용해 계층간 데이터를 주고 받는 연습을 충분히 할 수 있엇던 것 같다. api를 만들 때 RESTful에 대한 개념이 조금 부족하다고 느껴졌다. (추후 Restful API에 대한 공부해야겠다.) 회원가입시 정규식을 이용하여 유효성 검사하였다. 확실히 내가 코드를 짜서 비교하며 유효성을 검증하는 것보다 정규식을 이용한 유효성 검사가 확실하다는 생각을 하게 되었다.


<br>

---

<br>

## 구현화면

<Strong>이미지를 클릭하면 구현 영상을 확인하실 수 있습니다:)</strong>

[![나만의리스트](https://user-images.githubusercontent.com/74294325/111347826-ed6c3080-86c2-11eb-973e-dd823a32ef9e.PNG)](https://www.youtube.com/watch?v=06lWpu9XtUY)

<br>

---

<br>

## 구현 기능

* [로그인,회원가입](https://github.com/LeeWoooo/MyShopList/tree/master/%EA%B8%B0%EB%8A%A5%EA%B5%AC%ED%98%84/%EB%A1%9C%EA%B7%B8%EC%9D%B8%2C%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85)
* [네이버상품검색](https://github.com/LeeWoooo/MyShopList/tree/master/%EA%B8%B0%EB%8A%A5%EA%B5%AC%ED%98%84/%EB%84%A4%EC%9D%B4%EB%B2%84%EA%B2%80%EC%83%89%EA%B8%B0%EB%8A%A5)
* [상품저장 및 ajax을 이용한 페이징](https://github.com/LeeWoooo/MyShopList/tree/master/%EA%B8%B0%EB%8A%A5%EA%B5%AC%ED%98%84/%EC%83%81%ED%92%88%EC%A0%80%EC%9E%A5%2Cajax%EC%9D%84%EC%9D%B4%EC%9A%A9%ED%95%9C%ED%8E%98%EC%9D%B4%EC%A7%95)
* [상품 수정 및 삭제, 스케줄링](https://github.com/LeeWoooo/MyShopList/tree/master/%EA%B8%B0%EB%8A%A5%EA%B5%AC%ED%98%84/%EB%82%98%EC%9D%98%EC%B5%9C%EC%A0%80%EA%B0%80%20%EC%88%98%EC%A0%95%2C%EC%82%AD%EC%A0%9C%20%EB%B0%8F%20%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81)