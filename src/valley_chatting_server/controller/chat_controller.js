const chatRoom = require('../model/chattingRoom');
const statusCode = require('../modules/status_code');
const util = require('../modules/util');


module.exports = {

    // 채팅 기록 확인
    getChatLog: async (req, res) => {

        // 1. request 데이터 받기 (roomName : 방이름)
        const roomName = req.query.roomName;

        // NULL 값 확인
        if (!roomName) {
            res.status(statusCode.OK).send(util.fail(statusCode.NULL_VALUE));
            return;
        }


        // 2. 채팅 기록 모으기
        try {
            const result = await chatRoom.aggregate([
                {$match: {"chatRoomName" : roomName}},
                {$unwind: "$chatLog"},
                {$sort: {"chatLog.createdTime": 1}},
                {$limit: 50}, // 한번에 몇개씩 가져올지
                {
                    $group: {
                        _id:"$_id",
                        chatLog: {
                            $push: {
                                userId: "$chatLog.userId",
                                message: "$chatLog.message",
                                Time: "$chatLog.createdTimeStr",
                                createdTime: "$chatLog.createdTime"
                            }   
                        }
                    }
                }
            ]);
        
            const data2 = [];
            if(result.length == 0) { // 대화 기록이 없다면 -> 빈 배열 return
                var empty=[];
                res.status(statusCode.OK).send(util.successWithData(statusCode.OK, empty));
                return;
            } else {
                for await (const doc of result) {
                    data2.push(doc.chatLog);
                }
                const resultData = [];
                for (const docs of data2[0]) {
                    var imsi = {};
                    imsi.userId = docs.userId;
                    imsi.message = docs.message;
                    imsi.time = docs.Time;
                    createdTime = docs.createdTime;
                    resultData.push(imsi);
                }
                // 배열 보내주기
                res.status(statusCode.OK).send(util.successWithData(statusCode.OK, resultData));
                return;
            }
        } catch (err) {
            console.log("arrgegate 에러 : " + err);
            res.status(statusCode.OK).send(util.fail(statusCode.MONGODB_ERROR));
            return;
        }

    },

    // 채팅방 목록 확인
    getRoomList: async (req, res) => {

        // 1. request 데이터 받기 (userId : 본인 userId)
        const userId = req.query.userId;


        // 2. 본인 포함된 채팅방 목록 가져오기
        const result = await chatRoom.find({"chatRoomName":{"$regex":("^r"+userId+"w|w"+userId+"$")}});
        const resultNum = result.length;

        if (resultNum == 0){
            // 생성된 채팅방이 없음
            res.status(statusCode.OK).send(util.successWithData(statusCode.OK, []));
            return;
        }


        // 3. 각 채팅방의 최신 대화순으로 재정렬
        const arr = [];
        for (var i = 0; i<resultNum; i++){
            var objData = {};
            if (result[i].user1Id == userId) {
                objData.chatRoomImg = result[i].user2Img;
                objData.nickname = result[i].user2Nickname;
                objData.userId = result[i].user2Id;
            } else {
                objData.chatRoomImg = result[i].user1Img;
                objData.nickname = result[i].user1Nickname;
                objData.userId = result[i].user1Id;
            }
            objData.roomName = result[i].chatRoomName;
            if (result[i].chatLog.length == 0){
                // 채팅방만 생성 (대화기록이 없음)
                continue;
            }
            objData.lastMessageTime = result[i].chatLog[result[i].chatLog.length-1].createdTime;
            objData.content = result[i].chatLog[result[i].chatLog.length-1].message;
            arr.push(objData);
        }
        
        arr.sort(function(a, b) {
            return b.lastMessageTime-a.lastMessageTime;
        });
        
        res.status(statusCode.OK).send(util.successWithData(statusCode.OK, arr));
        return;
    }
}