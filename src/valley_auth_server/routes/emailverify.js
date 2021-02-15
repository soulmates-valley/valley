require("dotenv").config();

var express = require('express');
var router = express.Router();
var Isemail = require('isemail');
var cors = require('cors');
var validator = require("email-validator");

const smtpTransport = require('nodemailer-smtp-transport');
const nodemailer = require('nodemailer');
const responseMessage = require('../modules/responsemsg');
const statusCode = require('../modules/statuscode');
const util = require('../modules/util');

const EMAIL_ADDRESS_ERROR=-4;

router.post('/auth', async (req, res)=> {
    //verifyemail
    const email = req.body.email;
    console.log(validator.validate('test@gmail'));

    if (!validator.validate(email)) {
        res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST));
        return EMAIL_ADDRESS_ERROR;
    }
    else {
        let authNum = Math.random().toString().substr(2, 6);
        const smtpTransport = nodemailer.createTransport({
            service: 'Naver',
            host: 'smpt.naver.com',
            port: 465,
            auth: {
                user: process.env.NODEMAILER_USER,
                pass: process.env.NODEMAILER_PASS
            }
        });
        const mailOptions = {
            from: process.env.NODEMAILER_USER,
            to: email,
            subject: 'valley 회원가입 이메일입니다.',
            text: authNum,
            html: '<p>인증번호는 ' + authNum + ' 입니다.\n 인증번호 창에 입력해주세요.'
        }
        try {
            await smtpTransport.sendMail(mailOptions);
            res.status(statusCode.OK).send(util.success(statusCode.OK, authNum));
        } catch (err) {
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR));
        }
    }
});


module.exports = router;
