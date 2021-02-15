# Valley Chatting Server

### 목표

- node.js(express)를 이용한 채팅 서버
  - node.js : 14.15.1
  - express : 4.16.1
- Client <--> Nginx<--> **Chatting Server** <--> NginX <--> Client
  - webSocket 통신 (TCP)
  - Nginx Gateway haproxy를 통한 통신

### 기능

- 1:1 채팅
- 채팅방 목록 조회
  - 대화가 시작된방
  - 최신 대화 순
- 채팅 기록 조회
  - 최신 대화 순



## Open Soucre

debug : 2.6.9

express : 4.16.1

http-errors : 1.6.3

moment : 2.29.1

moment-timezone : 0.5.33

mongoose : 5.11.15

morgan : 1.9.1

nodemon : 2.0.7

serve-favicon : 2.5.0

socket.io : 2.3.0