# TicketRadar

![123213](https://github.com/LSW990918/TicketRaider/assets/48382951/584572fe-3a2d-49d7-9ce7-894ac3211a88)


## 👨‍🏫 프로젝트 소개
프로젝트 제목 : 티켓 레이더(티켓팅 프로그램)

프로젝트 설정 이유 : 

- 티케팅 서비스 (Why?)
    
    캠프에서 공부한 기능들을 적절하게 사용할 수 있고, 모두에게 친숙하기 때문에 프로젝트 계획을 짜고 구현하는데 수월하다.
    6주 내에 구현 가능하고, 새롭게 구현해보고 싶은 기술을 적용할 때 확장성이 좋다고 생각했다.
    
- 티켓 레이더 (Why?)
    
    중의적인 뜻으로
    raider - 모험을 떠나듯이 티켓을 얻는 과정을 표현한 이름
    radar - 레이더로 무언가를 감지해내듯 원하는 티켓도 감지할수 있다는 걸 표현한 이름

## ⏲️ 개발기간
- 2024.02.26(월) ~ 2024.04.05(금)

## 📚️ Stacks
<div align=center>
  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/supabase-3FCF8E?style=for-the-badge&logo=supabase&logoColor=white">
  <img src ="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white">
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white">
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
</div>

🏗 아키텍쳐
![image](https://github.com/ticketraider/TicketRadar/assets/150113407/10115724-da69-4edc-b523-f29001b55365)



## 와이어프레임
<img width="1077" alt="스크린샷 2024-02-26 오후 3 55 55" src="https://github.com/LSW990918/TicketRaider/assets/48382951/7ba30086-6d1d-42a1-a3ed-5f919945c925">



## ERD

![erd수정본 PNG](https://github.com/LSW990918/TicketRaider/assets/48382951/a891642b-40ae-46cd-a570-f44d90fcb4d6)



## 🍀 주요 기술 및 역할 분담

### [김건우](https://github.com/hongdul) - 팀장
- CRUD - ticket, member
- 인증, 인가 - 소셜 로그인
- S3 - 이미지 파일 업로드 구현
- 배포 - 프론트 엔드, 백 엔드

### [장준혁](https://github.com/JangCoding) - 부팀장
- CRUD - like, review
- Redis - 인기 검색어 기능, 캐싱 기능
- Vue - 프론트엔드 페이지 디자인 마무리, front-back 연결
  
### [이시원](https://github.com/LSW990918) - 조원
- CRUD - category, place, availableSeat, price
- Redisson - Pub-Sub Lock 구현, AOP를 활용해 Pub-Sub Lock 어노테이션화
- Vue - 각종 프론트엔드 페이지 디자인 뼈대 작성, 티켓 예매시 좌석배치도 구현, front - back 연결
  
### [장준용](https://github.com/tsdnhts) - 조원
- CRUD - event
- QueryDsl - QueryDSL을 활용한 동적쿼리 구현(다양한 필터 적용)
- 발표 - 중간발표 및 최종 발표간 대본 작성 및 발표




## 팀 노션페이지

https://www.notion.so/44f1197d79e04f1e8ac5437f86db634d?pvs=4#5e055bee96514a36b1638e2433fb3b70


