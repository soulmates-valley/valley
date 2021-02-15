# Valley Search Server

### 목표

- node.js를이용해 client에게 검색결과를 반환, 페이징 처리

- feed, user 정보 수정시 검색물에 반영

- Client <--> node <--> elasticsearch
- 타 application <--> node <-- logstash <-- rabbitmq


### 기능

- 피드, 유저, 해시태그 검색
- 인기검색어 제공

