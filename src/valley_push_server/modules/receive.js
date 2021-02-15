const amqp = require('amqplib/callback_api');
const admin = require("firebase-admin");
const serviceAccount = require("../config/firebase-adminsdk.json");
const fcmSend = require('../modules/fcmSend');


// rabbitMQ 연결
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

module.exports = { 

  // rabbitMQ로 부터 알림 요청 message 취득
  sendFcm:  () => {
    amqp.connect('amqp://id:password@ip:port', function(error0, connection) {
      if (error0) {
        console.error("[AMQP] error", error0);
        throw error0;
      }
      
      connection.createChannel(function (error1, channel) {
        if (error1) {
          throw error1;
        }
        var exchange = 'follow.topic';
        var key = 'follow.create';
        var queue = 'notifications';

        channel.assertExchange(exchange, 'topic', {
          durable: true
        });

        channel.assertQueue(queue, {
          //durable: false
        }, function(error2, q) {
          if (error2) {
            throw error2;
          }
          console.log('[rabbitMQ] Queue : ' + queue +' 대기중');

          
          channel.bindQueue(q.queue, exchange, key);
          
          channel.consume(q.queue, function(msg) {
            const getData = JSON.parse(msg.content); // {"fromUserId":27,"toUserId":20,"postId":26}
            //console.log(getData);
            fcmSend.sendMsg(getData,msg.fields.routingKey);
            }, {
              noAck: true
            });
          });
        });
    });
  }
}
