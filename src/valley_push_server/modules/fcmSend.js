const admin = require("firebase-admin");
const graphModel = require('../models/query/neo4jQuery');
const notifications = require('../models/notifications');


module.exports = {
    sendMsg: async (pushData, key, req, res, next) => {

        // 1. MQ에 있는 데이터 뽑기 -> data에 정리하기
        var data = {};

        const fromUserId = pushData.data.fromUserId;
        const toUserId = pushData.data.toUserId;
        if (fromUserId == toUserId) {
            return;
        }

        if (key == 'comment.create'){
            data.event = 1;
        } else if (key == 'like.create') {
            data.event = 2;
        } else if (key == 'follow.create') {
            data.event = 3;
        }
        data.fromuserId = fromUserId;


        // 2. 유저 정보 취득(userId로 취득)

        // 보낸 사람
        const fromResult = await graphModel.getUserById(fromUserId);
        if (fromResult.records.length != 0) { 
            const getData = fromResult.records[0]._fields[0].properties;
            data.nickname = getData.nickname;
            if (getData.profileImg != undefined) {
                data.profileImg = getData.profileImg;
            } else {
                data.profileImg = undefined;
            }
        } else {
            console.log('From User 가 없음');
        }

        // 받을 사람
        const toResult = await graphModel.getUserById(toUserId);
        if(toResult.records.length != 0) {
            const getData = toResult.records[0]._fields[0].properties;
            var toUserDeviceToken = getData.deviceToken;
            var toUserNickname = getData.nickname;
        } else {
            console.log('To user 가 없음');
        }

        if (data.event == 1){
            var userEvent = "댓글을 남기셨습니다.";
        } else if (data.event == 2) {
            var userEvent = "좋아요을 누르셨습니다.";
        } else if (data.event == 3) {
            var userEvent = "팔로우 합니다.";
        }
            
        // 알림 띄워줄 메세지
        var noti_text = data.nickname + '님이 ' + userEvent;
        //console.log(noti_text);


        // 3. mongodb notifications collection에 알림 메세지 저장
        
        let dt = new Date();

        // 저장 데이터, 옵션, 쿼리 조건
        const messageLogObj = {
            fromUserId: fromUserId,
            nickname: data.nickname,
            event: data.event,
            profileImg: data.profileImg
        };
        console.log(messageLogObj);
        const options = {
            upsert: true,
            new: true,
            setDefaultsOnInsert: true
        };
        const query = {
            $push: {
                messageLog:{
                    $each: [messageLogObj],
                    $slice: -50 // 최대 50개 저장
                }
            }
        };
            
        // mongoDB 저장
        notifications.findOneAndUpdate(
            {toUserId:toUserId},
            query,
            options,
            function (error, success){
                if(error) {
                    console.log(error);
                } else {
                    console.log(toUserId+' 알림 데이터 추가 성공');
                }
        });
            

        // 4. FCM 메세지 보내기    
        try {

            // FCM 설정
            const payload = {
                notification: {
                    title: 'Valley 알림',
                    body: noti_text
                },
                data: {
                    title: 'valley 알림',
                    body: noti_text
                },
                token: toUserDeviceToken
            };
            
            // FCM 보내기
            admin
            .messaging()
            .send(payload)
            //.sendAll(messages) // 전체 발송
            .then(response => {
                console.log('Successfully sent message: : ', response)
            })
            .catch(function (err) {
                //console.log('Error Sending message!!! : ', err)
                console.log('디바이스 토큰이 유효하지 않음');
            });
        } catch {
            console.log("FCM 전송 실패");
        }
    }
}