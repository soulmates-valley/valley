# Valley User Server

### 목표

- node.js(express)를 이용한 유저 관리 서버
  - node.js : 14.15.1
  - express : 4.16.1
- Client <--> User Server <--> Auth Server 통신간 Token 관리
  - AccessToken
  - RefreshToken
  - DeviceToken

### 기능

- 로그인
- 회원가입
- 닉네임 검사
- 이메일 검사
- 프로필 조회
- 프로필 수정
- 로그아웃



## Open Soucre

amqplib : 0.6.0

aws-sdk : 2.828.0

cookie-parser : 1.4.4

debug : 2.6.9

ejs : 2.6.1

express : 4.16.1

http-errors : 1.6.3

jsonwebtoken : 8.5.1

morgan :  1.9.1

multer : 1.4.2

multer-s3 : 2.9.0

mysql2 : 2.2.5

neo4j-driver : 4.2.2

pbkdf2 : 3.1.1

request : 2.88.2

sequelize : 6.3.5

sequelize-auto : 0.7.6