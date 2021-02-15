// db 조회 사항을 return 하는 부분
const models = require('../../models');

module.exports = {
    deviceTokenCheck: async (toUserId) => {
        const result = await models.users
        .findOne({
            where: {
                id: toUserId
            }
        })
        .catch (err => {
            console.erroe('noti To User cant find ERROR :', err);
            throw err;
            
        });
        return result;

    },
    getNickname: async (fromUserId) => {
        const result = await models.users
        .findOne({
            where: {
                id: fromUserId
            }
        })
        .catch (err => {
            console.erroe('noti To User cant find ERROR :', err);
            throw err;
        });
        return result;
    }
}