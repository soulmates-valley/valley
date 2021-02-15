const getUserByToken = require('../modules/jwtDecode');
const graphModel = require('../models/neo4jQuery');
const util = require('../modules/util');
const request = require('request');
const statusCode = require('../modules/status_code');


module.exports = {

    // 프로필 조회
    getProfile: async (req, res) => {

        // 1. 요청 userId 획득
        const requestId = parseInt(req.query.userId); // 요청 Id
        const accessToken = req.headers.authorization;
        const tokenResult = await getUserByToken.getDecodeData(accessToken);
        const userId = parseInt(tokenResult.userId); // 본인 Id

    
        // 2. 유저 정보 수집 (neo4j)
        const result = await graphModel.getUserById(requestId);
        var getData;
        if(result.records.length != 0) {
            getData = result.records[0]._fields[0].properties;
        } else {
            // 비회원 확인
            res.status(statusCode.OK).send(util.fail(statusCode.NO_USER, []));
            return;
        }


        // 3. 팔로우 정보 수집
        const result2 = await graphModel.getFollowById(requestId);
        const following = result2.records[0]._fields[0].low;
        const follower = result2.records[0]._fields[1].low;

        const returnData = {
            nickname: getData.nickname,
            interest: getData.interest,
            profileLink: getData.profileLink,
            description: getData.description,
            following: following,
            follower: follower
        }

        // 자신이면 팔로우(x)
        if(userId == requestId) {
            returnData.isFollow = false;
        } else {
            const result3 = await graphModel.isFollowById(userId, requestId);
            // 팔로우 관계이다
            if(result3.records.length != 0) {
                returnData.isFollow = true;
            }
            // 팔로우 관계가 아니다
            else{
                returnData.isFollow = false;
            }
        }

        // 프로필 이미지 등록
        if(getData.profileImg != undefined){
            returnData.profileImg = getData.profileImg;
        }


        // 4.  결과 return
        res.status(statusCode.OK).send(util.success(statusCode.OK, returnData));

    },

    modifyProfile: async (req, res) => {
        // 1. 요청 userId, 수정 데이터 획득
        const accessToken = req.headers.authorization;
        const result = await getUserByToken.getDecodeData(accessToken);
        const userId = result.userId;

        // client request 받기
        const {
            nickname,
            description,
            profileLink,
            birthYear,
            interest
        } = req.body;

        const imgFile = req.file;


        // 2. 수정할 데이터 가공
        const data = {
            nickname: nickname,
            description: description,
            profileLink: profileLink,
            birthYear: birthYear,
            interest: interest
        };
        // 프로필 이미지 유무
        if (imgFile) data.imgFile = imgFile.location;
        else data.profileImg = null;

        const interestInt = [];
        for (const imsi of data.interest){
            interestInt.push(parseInt(imsi));
        }
        data.interest = interestInt;

        
        // 3. 수정 데이터 -> Search Server (동기화)
        request.post ({
            header: {'content-type': 'application/json'},
            //url: AuthServer Location,
            body: data,
            json: true
        }, function (err, response, body){
            if(err){
                //console.log(err)
                console.log('응답값을 가져올 수 없음');
            } else {
                console.log(body.message);   
            }
        });
        

        // 4. 수정데이터 최종 반영 (neo4j)
        try {
            const result2 = await graphModel.updateUserById(userId, data);
            res.status(statusCode.OK).send(util.success(statusCode.OK));
            return;
        } catch {
            res.status(statusCode.OK).send(util.success(statusCode.MODIFY_USER_FAIL));
            return;
        }
    },

    logout: async (req, res) => {

        // 1. 요청 userId, 수정 데이터 획득
        const accessToken = req.headers.authorization;
        const result = await getUserByToken.getDecodeData(accessToken);
        const userId = result.userId;

        
        // 2. AccessToken -> Auth Server (블랙리스트 추가)
        request.get ({
            headers: {
                'content-type': 'application/json',
                Authorization: accessToken,
            },
            //url: AuthServer Location
            json: true
        }, function (err, response, body){
            // accessToken 전달 완료
            console.log("accessToken 전달 완료");
        });
        

        // 3. user의 DeviceToken 제거
        try {
            const result2 = await graphModel.deleteDeviceTokenById(userId);
            res.status(statusCode.OK).send(util.success(statusCode.OK));
            return;
        } catch {
            res.status(statusCode.OK).send(util.success(statusCode.DEVICETOKEN_DELETE_FAIL));
            return;
        }
    },

};