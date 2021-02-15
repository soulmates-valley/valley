const responseMessage = require('../modules/response_message');
const statusCode = require('../modules/status_code');
const util = require('../modules/util');
const graphModel = require('../models/neo4jQuery');
const request = require('request');
const crypto = require('../modules/crypto');
const toRabbitMQ = require('../modules/toRabbitMQ');


module.exports = {

    // 이메일 확인
    verifyEmail: async (req, res) => {

        // request 데이터 받기
        const {
            email
        } = req.body;
        
        // NULL 값 확인
        if (!email) {
            res.status(statusCode.OK).send(util.fail(statusCode.NULL_VALUE, []));
            return;
        }

        // 이메일 중복 확인
        const result = await graphModel.emailCheck(email);
        if (result) {
            res.status(statusCode.OK).send(util.fail(statusCode.DUPLICATE_EMAIL, []));
            return;
        }


        // auth 서버에 email 전달 -> 인증 코드 생성
        const jsonDataObj = {
            email : email
        };
        request.post ({
            header: {'content-type': 'application/json'},
            //url: AuthServer Location
            body: jsonDataObj,
            json: true
        }, function (err, response, body){// auth 서버한테 받은 인증코드 클라이언트 한테 return
            const data = {
            verifycode: body.data
            };

            // 이메일 유효하지 않음
            if (body.status == 400) {
                res.status(statusCode.OK).send(util.fail(statusCode.NOT_VALID_EMAIL, []));
                return;
            }
            
            // 인증코드 없음
            if (!data.verifycode) {
                res.status(statusCode.OK).send(util.fail(statusCode.NO_RANDOM_NUM, []));  
                return;
            }
            // 인증 메일 보내기 성공
            res.status(statusCode.OK).send(util.success(statusCode.OK, data));
        });
    },

    // 닉네임 중복 확인
    verifyNickname: async (req, res) => {

        // request 데이터 받기
        const {
            nickname
          } = req.body;
        
        // NULL 값 확인
        if (!nickname) {
            res.status(statusCode.OK).send(util.fail(statusCode.NULL_VALUE));
            return;
        }

        // NICKNAME 중복 확인
        const result = await graphModel.nicknameCheck(nickname);
        if (result) {
            // 닉네임 중복
            res.status(statusCode.OK).send(util.fail(statusCode.DUPLICATE_NICKNAME));
            return;
        } else {
            // 사용 가능
            res.status(statusCode.OK).send(util.success(statusCode.OK));
            return;
        }
    },

    // 회원가입
    signUp: async (req, res) => {

        // request 받기
        const {
            email,
            password,
            nickname,
            description,
            profileLink,
            birthYear,
            interest,
        } = req.body;
        const imgFile = req.file;
        
        // NULL 값 확인
        if (!email || !password || !nickname || !birthYear || !interest) {
            res.status(statusCode.OK).send(util.fail(statusCode.NULL_VALUE));
            return;
        }


        // 1. 회원 데이터 가공 (data)
        // 비밀번호 암호화
        const {
            salt,
            hashed
        } = await crypto.encrypt(password);

        const data = {
            email,
            password: hashed,
            nickname,
            description,
            profileLink,
            birthYear,
            interest,
            createdDt: undefined,
            salt: salt
        };
        // s3 이미지 파일 경로 설정
        if (imgFile) {
            data.profileImg = imgFile.location;
        }
        // interest 형변환 (int형)
        const interestInt = [];
        for (const imsi of interest){
            interestInt.push(parseInt(imsi));
        }
        data.interest = interestInt;


        
        // 2. graphdb 저장
        const result = await graphModel.signUp(data, interest);
        // 저장 실패
        if (result == false) {
            res.status(statusCode.OK).send(util.fail(statusCode.DUPLICATE_EMAIL));
            return;
        }
        data.userId = parseInt(result);

        
        // 3. rabbitMQ 전달 (Search Server)
        try {
            toRabbitMQ.sendUser(data);
            res.status(statusCode.OK).send(util.success(statusCode.OK));
            return;
        } catch (err) {
            res.status(statusCode.OK).send(util.fail(statusCode.INTERNAL_SERVER_ERROR));
            return;
        }
    },
    // 로그인
    signIn: async (req, res) => {

        // request 데이터 받기
        const {
            email,
            password,
            deviceToken
        } = req.body;
          
        
        // NULL 확인
        if (!email || !password || !deviceToken) {
            res.status(statusCode.OK).send(util.fail(statusCode.NULL_VALUE, responseMessage,NULL_VALUE, []));
            return;
        };

        // 이메일 확인
        const result = await graphModel.emailCheck(email);

        if (!result) {
            res.status(statusCode.OK).send(util.failWithMessage(statusCode.NO_USER, responseMessage.MISS_MATCH_ID, []));
            return;
        }

        // 비밀번호 일치 확인
        const hashed = await crypto.encryptWithSalt(password, result.salt);
        if(hashed != result.password) {
            res.status(statusCode.OK).send(util.failWithMessage(statusCode.MISS_MATCH_PW, responseMessage.MISS_MATCH_PW, []));
            return;
        }

        // 디바이스 토큰 등록
        const result2 = await graphModel.updateDeviceToken(email, deviceToken);
        if (result2) {
            res.status(statusCode.OK).send(util.fail(statusCode.NO_DEVICE_TOKEN, responseMessage.NO_DEVICE_TOKEN, []));
            return;
        }

        // auth 서버에 토큰 취득 -> 클라이언트 return
        const toAuthData = {
            email: email,
            userId: result.userId.low,
            nickname: result.nickname
        };
    
        request.post ({
            header: {'content-type': 'application/json'},
            //url: AuthServer Location
            body: toAuthData,
            json: true
        }, (err, response, body) => {
            if (err) { // 인증서버 오류
                res.status(statusCode.OK).send(util.fail(statusCode.EMPTY_TOKEN, responseMessage,EMPTY_TOKEN, []));
            } else {
                const data = {
                    userId: result.userId.low,
                    accessToken: body.data.accessToken,
                    refreshToken: body.data.refreshToken,
                    nickname: result.nickname,
                    profileImg: result.profileImg,
                    description: result.description,
                    link: result.profileLink,
                    interest: result.interest
                };
                res.status(statusCode.OK).send(util.successWithMessage(statusCode.OK, responseMessage.LOGIN_SUCCESS, data));
            } 
        });
        return;
    },

    /*
    // 회원 탈퇴 (임시)
    deleteUser: async (req, res) => {
        const {
            email
        } = req.body;
        
        // NULL 값 확인
        if (!email) {
            res.status(statusCode.OK).send(util.fail(statusCode.NULL_VALUE, responseMessage.NULL_VALUE));
            return;
        }
        const result = await graphModel.deleteUser(email);
        
    
        res.status(statusCode.OK).send('삭제성공');
        return;

    }
    */
};