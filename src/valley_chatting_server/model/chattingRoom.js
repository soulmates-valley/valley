const mongoose = require('mongoose');
const Schema = mongoose.Schema;



const chatSchema = new Schema({
    chatRoomName: String,// R 12 TO 320 (작은숫자 앞으로)
    user1Id: Number,
    user1Nickname: String,
    user1Img: String,
    user2Id: Number,
    user2Nickname: String,
    user2Img: String,

    chatLog: [{
        userId: Number, // 글 쓴사람
        message: String, // 채팅 1개 내용
        createdTimeStr: String,
        createdTime: {type: Date, default: Date.now, timezone: 'Asia/Seoul'}
    }]
});


module.exports = mongoose.model('chatroom', chatSchema);