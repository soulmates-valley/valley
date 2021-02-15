const neo4j = require('neo4j-driver');
//const driver = new neo4j.driver("neo4j://localhost:7687", neo4j.auth.basic(ID, password));
const driver = new neo4j.driver("neo4j://IP:PORT", neo4j.auth.basic(ID, PASSWORD));


module.exports = {

    nicknameCheck: (nickname) => {
        const session = driver.session();
        const cypher = "MATCH (n:User) WHERE n.nickname = $nickname RETURN n;";
        const result = session.run(cypher, {nickname: nickname})
            .catch (err => {
                console.error('nickName find ERROR : ', err);
                throw err;
            })
            .then (result => {
                session.close();
                // 조회된 데이터 없다. -> 닉네임 가능
                if(result.records.length != 0) {
                    return result.records[0]._fields[0].properties;
                // 조회된 데이터 있다. -> 닉네임 중복
                } else { 
                    return false;
                }
            });
        return result;
    },

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
            // 조회된 데이터 없다. -> 이메일 가능
            if(result.records.length != 0) {
                return result.records[0]._fields[0].properties;
            // 조회된 데이터 있다. -> 이메일 중복
            } else { 
                return false;
            }
        });
    return result;
    },

    signUp: async (data, userInterest) => {
        const session = driver.session();
        const cypher = "CREATE (n: User $data) SET n.userId = ID(n), n.createdDt = localdatetime({timezone: 'Asia/Seoul'}) RETURN n.userId;";
        const result = await session.run(cypher, {data: data})
        .catch (err => {
            
            console.error('signup ERROR : ', err);
            return false;
        })
        .then (result => {
            session.close();
            return result;
        });
        
        if (result == false) {
            return false;
        }

        const userId = result.records[0]._fields[0].low;
        
        // interest Node 관계 연결
        for(var i=0; i<userInterest.length;i++){
            const session = driver.session();
            const cypher = "MATCH (u:User{userId:"+userId+"}) MERGE (i:Interest{content:"+userInterest[i]+"}) WITH u,i MERGE (u)-[:INTERESTED_IN]->(i);";
            const result2 = session.run(cypher)
            .catch(err => {
                throw err;
            })
            .then (result => {
                session.close();
            })
            
        }
        return userId;
    },

    updateDeviceToken: (userEmail, userDeviceToken) => {
        const session = driver.session();
        const cypher = "MATCH (u:User {email:$email}) SET u.deviceToken = $deviceToken";
        const result = session.run(cypher, {email: userEmail, deviceToken: userDeviceToken})
        .catch (err => {
            console.err('Token Regist ERROR : ', err);
            return false;
        }).then (result => {
            session.close();
        })
        if (result == false) {
            return false;
        } else {
            return;
        }
    },

    deleteUser: (userEmail) => {
        const session = driver.session();
        const cypher = "MATCH (u:User) WHERE u.email = $userEmail DETACH DELETE u;";
        const result = session.run(cypher, {userEmail: userEmail} )
        .catch (err => {
            console.err('delete User error : ',err);
            throw err;
        })
        .then (result => {
            session.close();
            return result;
        })
        return result;
    },

    deleteDeviceTokenById: (userId) => {
        const session = driver.session();
        const cypher = "MATCH (u:User) WHERE u.userId = $getUserId REMOVE u.deviceToken;"
        const result = session.run(cypher, {getUserId: userId})
        .catch (err => {
            console.err('delete deviceToken by Id ERROR : ', err);
            throw err;
        })
        .then(result => {
            session.close();
            return result;
        });
        return result;
    },

    updateUserById: (userId, userData) => {
        const session = driver.session();
        const cypher = "MERGE (u:User {userId: $userId}) SET u += $userData;"
        const result = session.run(cypher, {userId: userId, userData: userData})
        .catch (err => {
            console.err('update user ERROR : ', err);
            throw err;
        })
        .then(result => {
            session.close();
            return result;
        });
        return result;
    },

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
    },

    getFollowById: async (getUserId) => {
        const session = driver.session();
        const cypher = "MATCH (u:User {userId: $userId}) RETURN size((u)-[:FOLLOW]->()) as Following,size((u)<-[:FOLLOW]-()) as Follower;";
        const result = await session.run(cypher, {userId: getUserId})
        .catch (err => {
            console.err('get User by Id ERROR : ', err);
            throw err;
        })
        .then(result => {
            session.close();
            //console.log('1 :' + result);
            return result;
        });
        //console.log('2 :' + result);
        return result;
    },

    isFollowById: async (myId, userId) => {
        const session = driver.session();
        const cypher = "MATCH (u:User {userId: $id1}) -[r:FOLLOW] ->(u2:User{userId: $id2}) RETURN r;";
        const result = await session.run(cypher,{id1:myId, id2: userId})
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
