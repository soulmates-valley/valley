require("dotenv").config();

var express = require('express');
var router = express.Router();
var Isemail = require('isemail');
var cors = require('cors');

const jwt = require('jsonwebtoken');
const responseMessage = require('../modules/responsemsg');
const statusCode = require('../modules/statuscode');
const util = require('../modules/util');
const redisClient = require('../modules/redis');

const ACCESS_SECRET_KEY = process.env.ACCESS_SECRET;

// TO DO: REDIS 삭제 오류 헨들
// 근데 굳이 해줘야하나 생각도 들긴해서 우선 남겨둠
const REDIS_DEL_SUCCESS = 1;
const REDIS_DEL_FAIL = -1;



router.get('/auth', async (req, res)=> {
    const accesstoken = req.headers.authorization;
    const userPayload = jwt.verify(accesstoken,ACCESS_SECRET_KEY);
    const userEmail = userPayload.userEmail;
    
    try {
        //blacklist
        await redisClient.lpush('blackListToken', accesstoken);
        await redisClient.del(userEmail+':refreshToken');
        return res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK));
    } catch(err) {
        return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST));
    }
});



module.exports = router;
