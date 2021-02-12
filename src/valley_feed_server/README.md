# Valley feed Server


## 기능  

#### 피드기능  

- 홈피드 조회
- 추천피드 조회


#### 포스팅기능  

- 포스트 업로드
- 포스트 상세조회
- 특정 user 게시글 조회

#### 댓글기능  

- 댓글 등록


#### 좋아요기능 

- 좋아요 요청
- 좋아요 취소 


## how to run?

1. go to application.yml 

2. fill detail about
- neo4j : username, password, uri
- mongoDB : uri
- redis : host, port
- s3 : bucket, region, credentials
- rabbitmq : host, port, username, password
