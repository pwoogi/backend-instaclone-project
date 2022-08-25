# springCloneCoding
# 📷 클론 코딩 
Spring Boot + React를 사용한 클론 코딩 프로젝트
<!-- [**[트리스티의 SpringBoot + React를 사용한 클론 코딩 프로젝트에 오신 여러분을 환영합니다!]**]  -->

<br/>

<!-- [**[Fornt-End Github]**]()   -->
<!-- [**[Demo Video]**]()   -->

<br/>
<br/>

🎮 클론 기능
-------------  

- 로그인 및 회원가입 기능을 통해 인스타그램 클론 서비스를 이용하실 수 있습니다.
- 메인화면에서 다른 사람이 올린 글을 볼 수 있습니다.
- 글쓰기 기능을 통해 남들에게 내가 찍은 사진을 공유할 수 있습니다.
- 좋아요 기능으로 다른 사람 글을 추천해보세요.(기능 구현완료)
- 프로필에서 내 정보를 확인할 수 있습니다.(백엔드 구현완료)

<br/>
<br/>

🤔 Team
-------------  
[Front-End, 대표주소] [나청운](https://github.com/jennywoon/Instagram-Clone), [오영일]
<br/>
[Back-End] [박현욱](https://github.com/pwoogi), [박민혁](https://github.com/Park-Seaweed)

<br/>
<br/>

🤔 프로젝트 개요
-------------  
<ul style="list-style-type: disc;" data-ke-list-type="disc">
<li><b>진행 날짜 - 2022.08.19 ~ 2022.08.25</b></li>
<!-- <li><b>백엔드 프론트 협업, 인스타그램 클론코딩하기</b></li> -->
<li><b>필수 포함 사항</b></li>
</ul>


🎮 ERD
-------------

![0](https://user-images.githubusercontent.com/107388110/186649800-b4bda2df-0b2f-4f63-afec-ac6bd6f08dea.PNG)

🎮 API
-------------
[Link]

https://park-seaweed.notion.site/5-1c6f8b5caf1741979939521d9b415a03


🎮 백엔드 기능
-------------
[Back-End] 기술
1. JWT를 활용한 accessToken, refreshToken 설정
2. SSL 설정으로 서버의 안정성 높임
3. 객체간의 느슨한 결합을 통해 데이터의 의존성을 낮춤
4. S3활용한 이미지 업로드 구현
5. 
<br/>
<br/>

🤔 담당 기능
------------- 
* 박현욱 : SSL, 회원가입, 로그인, 대댓글 CRUD, ERD 맵핑, issue tracking

<br/>

* 박민혁 : S3 업로드, 게시물, 댓글, 좋아요 기능, API 설계, AWS 배포

<br/>
<br/>

🤔 트러블 슈팅
* [ ] Entity 설정을 (LAZY)지연로딩으로 하면서 데이터 수정,삭제의 어려움을 겪음
* 해결 : 프록시 객체에 대한 학습을 통해 게시물, 댓글, 대댓글 삭제, 수정 기능 구현
* [ ] Dto 인스턴스 변수 설정오류로 생성자 부재로 인해 데이터 값을 제대로 불러오지 못했던 점
* details, update, get으로 request, reponse dto를 설정해서 중복을 방지
* [ ] 소셜 로그인, 무한 스크롤 기능 확인해보지 못한 점
* [ ] Backend에서 postman을 통해 데이터를 확인했더라도 프론트 console에서 에러가 나는 것은 결국 Backend의 데이터 전달과정에서  
문제가 있을 가능성이 크다는 것을 느낌 


<br/>
<br/>
