require("dotenv").config();

var express = require('express');
var router = express.Router();
var cors = require('cors');

const responseMessage = require('../modules/responsemsg');
const statusCode = require('../modules/statuscode');
const util = require('../modules/util');
const jwt = require('../modules/jwt');

router.get('/auth', async (req, res)=> {
    const accesstoken = req.headers.authorization;
    const userPayload = jwt.verify(accesstoken,ACCESS_SECRET_KEY);

    const email = userPayload.userEmail;
    const userId = userPayload.userId;
    const nickname = userPayload.nickname;

    //TO DO:토큰만료시 재발급
    //유저의 access 토큰이 만료되었을 때, 해당 유저의 refresh을 확인하고 재발급
    //user 서버와 맞추는 과정이 필요

    try {
        const accessToken = await jwt.refresh(email,userId,nickname);
        res.status(statusCode.OK).send(util.success(statusCode.OK, {
            accessToken: accessToken
        }));

    } catch(err) {
        return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST,err.message));
    }

});

module.exports = router;
