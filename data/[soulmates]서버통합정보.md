| 분류                           | 명칭                    | 버전                         | 비고                                                               |
|------------------------------|-----------------------|----------------------------|------------------------------------------------------------------|
| 개발 환경                        | AWS EC2               | M4.large (ubuntu 18.04)    | gateway 구동                                                       |
|                              | AWS EC2               | M4.large (ubuntu 18.04)    | feed server, follow server 구동                                    |
|                              | AWS EC2               | M4.large (ubuntu 18.04)    | user server, chatting server, push server, mongoDB 구동            |
|                              | AWS EC2               | M4.large (ubuntu 18.04)    | auth server, search server, follow_recommend server, RabbitMQ 구동 |
|                              | AWS EC2               | M4.large (ubuntu 18.04)    | elasticsearch 구동, logstash 구동                                    |
|                              | AWS EC2               | M4.large (ubuntu 18.04)    | neo4j 구동                                                         |
|                              | AWS ElastiCache       | M4.large (cache.t3.medium) | redis 구동                                                         |
|                              | AWS S3                | standard                   | file storage                                                     |
| DB                           | neo4j - community     | 4.1.3                      |                                                                  |
|                              | mongoDB               | 4.4.3                      |                                                                  |
|                              | redis                 | 6.0.9                      |                                                                  |
| common                       | Rabbitmq              | 3.6.10                     | message queue                                                    |
| feed server                  | jdk                   | 11                         |                                                                  |
|                              | springboot            | 2.4.1                      |                                                                  |
| follow server                | jdk                   | 11                         |                                                                  |
|                              | springboot            | 2.4.1                      |                                                                  |
| user server                  | node.js               | 14.15.1                    |                                                                  |
|                              | express               | 4.16.1                     |                                                                  |
| push server                  | node.js               | 14.15.1                    |                                                                  |
|                              | express               | 4.16.1                     |                                                                  |
| chatting server              | node.js               | 14.15.1                    |                                                                  |
|                              | express               | 4.16.1                     |                                                                  |
|                              | node.js               | 10.23.1                    |                                                                  |
|    search server             | express               | 4.17.1                     |                                                                  |
|                              | elasticsearch.js      | 16.7.2                     |                                                                  |
| follow recommend<br/> server | node.js               | 10.23.1                    |                                                                  |
|                              | express               | 4.17.1                     |                                                                  |
| auth server                  | node.js               | 10.23.1                    |                                                                  |
|                              | express               | 4.17.1                     |                                                                  |
|                              | jsonwebtoken          | 8.5.1                      |                                                                  |
| android                      | retrofit2             | 2.9.0                      |                                                                  |
|                              | OkHttp3               | 3.9.0                      |                                                                  |
|                              | Rxjava2               | 2.2.7                      |                                                                  |
|                              | Koin                  | 2.1.5                      |                                                                  |
|                              | socket.io             | 1.0.0                      |                                                                  |
|                              | room                  | 1.1.1                      |                                                                  |
|                              | paging                | 2.1.2                      |                                                                  |
|                              | gson                  | 2.8.6                      |                                                                  |
|                              | viewPager2            | 1.1.0                      |                                                                  |
|                              | glide                 | 4.11.0                     |                                                                  |
|                              | tedimagepicker        | 1.0.8                      | 개인 깃 써드파티 라이브러리                                                  |
|                              | richlinkpreview       | 1.0.9                      | 개인 깃 써드파티 라이브러리                                                  |
|                              | brackeys.ui:editorkit | 1.0.0                      | 개인 깃 써드파티 라이브러리                                                  |