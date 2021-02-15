require("dotenv").config();

var express = require('express');
var router = express.Router();
var Isemail = require('isemail');
var cors = require('cors');

const jwt = require('../modules/jwt');
const responseMessage = require('../modules/responsemsg');
const statusCode = require('../modules/statuscode');
const util = require('../modules/util');
const redisClient = require('../modules/redis');

const EMAIL_ADDRESS_ERROR = -4;
const REDIS_SET_SUCCESS = 1;
const REDIS_SET_FAIL = 0;
const REDIS_BLACKLIST_ERROR = -1;
const REDIS_BLACKLIST_SUCCESS = 1;

var saveInRedis = async (refreshToken, key) => {
    //hmset에서와 별개의 문제가 발생할 수 있지않을까?
    //1월 7일 현재까지는 문제가 특별히 발생하지 않음
    await redisClient.hset('userInfo',key,refreshToken, function (err, reply) {
        if (err) {
            console.log(err);
            return REDIS_SET_FAIL;
        } else {
            return REDIS_SET_SUCCESS;
        }
    });
    await redisClient.setex(key+':refreshToken', 1000 * 60 * 60 * 24 * 7, 'null', function (err, reply) {
        if (err) {
            console.log(err);
            return REDIS_SET_FAIL;
        } else {
            return REDIS_SET_SUCCESS;
        }
    });
}

var selectRedis = async (accessToken) => {
    console.log(accessToken);
    await redisClient.lpos('token', accessToken, function (err, reply) {
       if (err) {
           console.log(err);
           return REDIS_BLACKLIST_ERROR;
       } else{
           //블렉리스트에 accesstoken이 없을때
           if (reply === null) {
               //console.log(reply);
               return REDIS_BLACKLIST_SUCCESS;
           } else {
               return REDIS_BLACKLIST_ERROR;
           }
       }
    });
}

router.post('/auth', async (req, res)=> {
    const email = req.body.email;
    const userId = req.body.userId;
    const nickname = req.body.nickname;

    if (!Isemail.validate(email)) {
        return EMAIL_ADDRESS_ERROR;
    }
    try {
        //issue token
        const {accessToken,refreshToken} = await jwt.issueToken(email,userId,nickname);

        //check blacklist
        //TO-DO: lpos error 남.. 로컬에서는 되는데 클라우드에서는 안됌..
        const blackListResult = selectRedis(accessToken);

        if (blackListResult === REDIS_BLACKLIST_ERROR){
           res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST));
        }

        var result = saveInRedis(refreshToken, email);

        if (result <= 0) {
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST));
        }
        console.log(accessToken);
        res.status(statusCode.OK).send(util.success(statusCode.OK, {
            accessToken: accessToken,
            refreshToken: refreshToken
        }));
    } catch (err) {
        console.error(err);
        res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR));
    }
});

module.exports = router;
