const mongoose = require('mongoose');
const Schema = mongoose.Schema;


// mongoDB
// Collection : notifications
const notiSchema = new Schema({
    toUserId: Number,
    messageLog: [{
        fromUserId: Number,
        nickname: String,
        event: Number,
        profileImg: String,
        notiTime: {type: Date, default: Date.now}
    }]
});


module.exports = mongoose.model('notification', notiSchema);