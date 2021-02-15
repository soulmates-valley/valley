var express = require('express');
var router = express.Router();
var neo4j = require('neo4j-driver')

const decodeJwt = require('../modules/decodeJwt');
const driver = require('../modules/neo4j');
const statusCode = require('../modules/statuscode');
const util = require('../modules/util');


router.get('/if',async (req, res) => {

    const accessToken = req.headers.authorization;
    const payload = await decodeJwt.decode(accessToken)
    const userId = parseInt(payload.userId);




    //var userId = parseInt(req.query.userId);
    //임시
    var graphName = Math.random().toString(36).substr(2,11);
    var neo4jSession = driver.session();
    var recomResult = []


    var makeGraph = neo4jSession.run("CALL gds.graph.create($graphName,\'User\',\'FOLLOW\')",{graphName:graphName})
                            .then(result =>{
                                    var pageRank = neo4jSession.run('MATCH (u1:User{userId:$userId})\n' +
                                        'MATCH (u1)-[:FOLLOW*2..4]-(u2)\n'+
                                        'WHERE NOT (u1)-[:FOLLOW]-(u2)\n'+
                                        'WITH DISTINCT 5 as friendScore, u2.nickname as nick,u1\n'+
                                        'CALL gds.pageRank.stream($graphName, {maxIterations: 20,dampingFactor: 0.85,sourceNodes:[u1]})\n'+
                                        'YIELD nodeId, score\n'+
                                        'WHERE gds.util.asNode(nodeId).nickname = nick\n'+
                                        'WITH friendScore + score * 50 as score, gds.util.asNode(nodeId) as u2\n'+
                                        'WHERE u1.nickname<>u2.nickname and NOT (u1)-[:FOLLOW]-(u2)\n'+
                                        'RETURN u2.userId,u2.nickname,u2.profileImg,u2.description,u2.interest,u2.isFollowed\n'+
                                        'ORDER BY score DESC LIMIT 50'

                                    ,{userId: userId,
                                                graphName:graphName
                                                 })
                                                .then(result =>{
                                                    result.records.forEach(function (value){
                                                            const data = {
                                                                userId: value._fields[0].low,
                                                                nickname: value._fields[1],
                                                                profileImg: value._fields[2],
                                                                description: value._fields[3],
                                                                interest: value._fields[4],
                                                                isFollowed: value._fields[5]
                                                            };
                                                            recomResult.push(data);
                                                    });
                                                    if (recomResult.length == 0)
                                                        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
                                                    else
                                                        res.status(statusCode.OK).send(util.success(statusCode.OK,recomResult));

                                                  })
                                                .catch(error =>{
                                                    console.log(error);
                                                    res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
                                                })
                                    })
                            .catch(error =>{
                                  console.log(error);
                                  res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
                             }).then(result=>{
                                var deleteGraph = neo4jSession.run("CALL gds.graph.drop($graphName)",{graphName:graphName})
                             });
                            //.then(() => neo4jSession.close())

});


router.get('/like',async (req, res) => {

    const accessToken = req.headers.authorization;
    const payload = await decodeJwt.decode(accessToken)
    const userId = parseInt(payload.userId);

    //var userId = parseInt(req.query.userId);
    //임시
    var neo4jSession = driver.session();
    var recomResult = []


    var sim = neo4jSession.run('MATCH (u1:User{userId:$userId})-[:INTERESTED_IN]->(i:Interest)\n' +
        'WITH u1, collect(id(i)) AS u1interest\n' +
        'MATCH (u2:User)-[:INTERESTED_IN]->(i:Interest)\n' +
        'WHERE u1 <> u2\n' +
        'WITH u1, u1interest, u2, collect(id(i)) AS u2interest\n' +
        'WHERE not (u1)-[:FOLLOW]->(u2)\n' +
        'WITH u1, u1interest, u2, u2interest,gds.alpha.similarity.jaccard(u1interest, u2interest) AS similarity, u2.lastPostDt AS lastPostDt,0 as score\n'+
        'WHERE similarity<>0\n'+
        'MATCH (User{userId:u2.userId}) \n'+
        'WHERE duration.inDays(lastPostDt,localdatetime.realtime()).days <= 7\n'+
        'WITH score + 5 as score,u2,u2.similarity * 10 as similarity\n'+
        'RETURN u2.userId as userId, u2.nickname, u2.profileImg, u2.description, u2.interest, false as isFollowed\n' +
        'ORDER BY similarity DESC LIMIT 50;',{
        userId: userId
    })
        .then(result =>{
            result.records.forEach(function (value){
                const data = {
                    userId: value._fields[0].low,
                    nickname: value._fields[1],
                    profileImg: value._fields[2],
                    description: value._fields[3],
                    interest: value._fields[4],
                    isFollowed: value._fields[5]
                };
                recomResult.push(data);
            });
            if (recomResult.length == 0)
                res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
            else
                res.status(statusCode.OK).send(util.success(statusCode.OK,recomResult));
        })
        .catch(error =>{
            console.log(error);
            res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
        })
});


module.exports = router;