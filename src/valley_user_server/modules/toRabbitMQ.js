const amqp = require('amqplib/callback_api');
//const { DataTypes } = require('sequelize/types');


module.exports = {
    sendUser: function(userData) {
        amqp.connect('amqp://ID:PASSWORD@IP:PORT', function(error0, connection) {
            if(error0){
                throw error0;
            };
            connection.createChannel(function (error1, channel) {
                if (error1){
                    throw error1;
                }
                var queue = 'createdUser';
                
                channel.assertQueue(queue, {
                    durable: true
                });
                var msg = {
                    userId: userData.userId,
                    nickname : userData.nickname,
                    profileImg : userData.profileImg,
                    interest : userData.interest,
                    //created_dt: new Date().tz('Asia/Seoul')
                };
                console.log(msg);
                channel.sendToQueue(queue, Buffer.from(JSON.stringify(msg)));
            });
        });
    }
};