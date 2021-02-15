const notifications = require('../models/notifications');
const statusCode = require('../modules/status_code');
const util = require('../modules/util');
const getUserByToken = require('../modules/jwtDecode');



module.exports = {

    // 알림 기록 확인
    getNotiLog: async (req, res) => {

        // 1. header의 accessToken을 통해 userId 확인
        const accessToken = req.headers.authorization;
        const tokenResult = await getUserByToken.getDecodeData(accessToken);
        const userId = parseInt(tokenResult.userId); // 본인 Id

        
        // 2. 본인Id로 수집된 알림 기록 수집 및 최신순으로 재정렬 (mongoDB)
        const result = await notifications.aggregate([
            {$match:{"toUserId": parseInt(userId)}},
            {$unwind:"$messageLog"},
            {$sort: {"messageLog.notiTime":-1}},
            {$limit: 50},
            {
                $group: {
                    _id: "$_id",
                    messageLog: {
                        $push: {
                            _id: "$messageLog._id",
                            nickname: "$messageLog.nickname",
                            fromUserId: "$messageLog.fromUserId",
                            event: "$messageLog.event",
                            profileImg: "$messageLog.profileImg",
                            notiTime: "$messageLog.notiTime"
                        }
                    }
                }
            }
        ]);

        
        // 3. 취득한 데이터 가공
        var data =[];
        for await (const doc of result) {
            data.push(doc.messageLog);
        }
        var responseData = [];
        try {
            for (const docs of data[0]) {
            var imsi = {};
            imsi.fromUserId = docs.fromUserId;
            imsi.notiTime = docs.notiTime;
            imsi.event = docs.event;
            imsi.nickname = docs.nickname;
            imsi.profileImg = docs.profileImg;
            responseData.push(imsi);
            }
        } catch (err) {
            // 데이터 없음 (알림 로그 없음)
            res.status(statusCode.OK).send(util.success(statusCode.OK, []));
            return;
        }
        
        // 성공
        res.status(statusCode.OK).send(util.success(statusCode.OK, responseData));
        return;
    }

}