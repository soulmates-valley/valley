const neo4j = require('neo4j-driver');
//const driver = new neo4j.driver("neo4j://localhost:7687", neo4j.auth.basic("id", "password"));
const driver = new neo4j.driver("neo4j://ip:port", neo4j.auth.basic("id", "password"));


// neo4j
module.exports = {

    // 닉네임 중복 체크
    nicknameCheck: (nickname) => {
        const session = driver.session();
        const cypher = "MATCH (n:User) WHERE n.nickname = $nickname RETURN n;";
        const result = session.run(cypher, {nickname: nickname})
            .catch (err => {
                console.error('nickName find ERROR : ', err);
                throw err;
            }
        );
        return result;
    },

    // 이메일 중복 체크
    emailCheck: (userEmail) => {
        const session = driver.session();
        const cypher = "MATCH (n:User) WHERE n.email = $email RETURN n;";
        const result = session.run(cypher, {email: userEmail})
            .catch (err => {
                console.error('email find ERROR : ', err);
                throw err;
            })
            .then (result => {
                session.close();
            });
        return result;
    },

    // 회원 가입
    signUp: (data, userInterest) => {
        const session = driver.session();
        const cypher = "CREATE (n: User $data) SET n.createdDt = localdatetime({timezone: 'Asia/Seoul'});";
        const result = session.run(cypher, {data: data})
            .catch (err => {
                console.error('signup ERROR : ', err);
                throw err;
            })
            .then (result => {
                session.close();
            });


        for(var i = 0; i < userInterest.length ; i++) {
            const session = driver.session();
            const cypher = "MATCH (u:User{userId:"+data.userId+"}) MERGE (i:Interest{content:"+userInterest[i]+"}) WITH u,i MERGE (u)-[:INTERESTED_IN]->(i);";
            const result2 = session.run(cypher)
            .catch(err => {
                //console.error('interest insert ERROR : ',err);
                throw err;
            })
            .then (result => {
                session.close();
            })
            
        }

    },

    // 회원 디바이스 토큰 등록 - 로그인
    updateDeviceToken: (userEmail, userDeviceToken) => {
        const session = driver.session();
        const cypher = "MATCH (u:User {email:$email}) SET u.deviceToken = $deviceToken";
        const result = session.run(cypher, {email: userEmail, deviceToken: userDeviceToken})
        .catch (err => {
            console.err('Token Regist ERROR : ', err);
            throw err;
        }).then (result => {
            session.close();
        })
    },

    // userId값으로 회원 정도 취득
    getUserById: (userId) => {
        const session = driver.session();
        const cypher = "MATCH (u:User) WHERE u.userId = $getUserId RETURN u;"
        const result = session.run(cypher, {getUserId: userId})
        .catch (err => {
            console.err('get User by Id ERROR : ', err);
            throw err;

        })
        .then(result => {
            session.close();
            return result;
        });
        return result;
    }
}