# Valley Push Server

### 목표

- node.js(express)를 이용한 push 알림 서버
  - node.js : 14.15.1
  - express : 4.16.1
- Client <--> RabbotMQ <--> **Push Server** <--> FCM
  - MQ : Exchange Topic 방식
  - 디바이스 토큰을 통한 push notification 전달

### 기능

- 알림 요청 수신 (MQ)
- FCM 알림 요청
- 알림 기록 저장
- 알림 기록 조회



## Open Soucre

amqplib : 0.6.0

cookie-parser : 1.4.4

debug : 2.6.9

express : 4.16.1

fcm-node : 1.5.2

firebase-admin : 9.4.2

http-errors : 1.6.3

jade : 1.11.0

jsonwebtoken : 8.5.1

moment : 2.29.1

moment-timezone : 0.5.32

mongoose : 5.11.13

morgan : 1.9.1

mysql2 : 2.2.5

neo4j-driver : 4.2.2

nodemon : 2.0.7

sequelize : 6.4.0

sequelize-auto : 0.7.7