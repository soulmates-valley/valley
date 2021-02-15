const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const mongoose = require('mongoose');
const mongoConfig = require('./config/mongoDB.json')
const chatRoom = require('./model/chattingRoom');

const chatRouter = require('./routes/chat')


const app = express();



// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));



mongoose.set('useNewUrlParser', true);
mongoose.set('useFindAndModify', false);
mongoose.set('useCreateIndex', true);
mongoose.set('useUnifiedTopology', true);
mongoose.connect(mongoConfig.ip + mongoConfig.database);
var db = mongoose.connection;

db.once('open', function(){
    console.log('[mongo DB] 연결 성공');
});
  
  
db.on('error', function(err){
    console.log('[mongo DB] ERROR : ', err);
});




// 라우터 등록
app.use('/chat', chatRouter);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


var socket_io = require('socket.io');

var io = socket_io({
    transports: ['websocket']
  });
var socketAPI = {};

io.on('connection', function (socket) {

  console.log('user connected: ' + socket.id);
  // 접속한 클라이언트의 정보가 수신
  socket.on('login', function (data) {
      console.log('Client logged-in:\n name:' + data.name + '\n userid: ' + data.userid);

      // socket에 클라이언트 정보를 저장한다
      socket.name = data.name;
      socket.userid = data.userid;

      // 접속된 모든 클라이언트에게 메시지를 전송한다
      io.emit('login', data.name);
  });

  // 채팅 방 접속
  socket.on('joinRoom', async function (data) {
      const roomData = JSON.parse(data);
      

      // data.chatRoom 긁어오기 -> JSON array로 만들기
      console.log(roomData.roomName);



      try {

        // mongoDB 데이터 저장 옵션
        const chatLogObj = {
            user1Id: roomData.user1Id,
            user1Nickname: roomData.user1Nickname,
            user1Img: roomData.user1Img,
            user2Id: roomData.user2Id,
            user2Nickname: roomData.user2Nickname,
            user2Img: roomData.user2Img
        }
        console.log("방 생성 데이터");
        console.log(chatLogObj);
        // 찾았는데 없으면 추가하라
        const options = {
            upsert: true,
            new: true,
            setDefaultsOnInsert: true,
            returnNewDocument: true
        };
        // 배열 형식으로 넣는다
        const query = {
            $set: {
                user1Id: roomData.user1Id,
                user1Nickname: roomData.user1Nickname,
                user1Img: roomData.user1Img,
                user2Id: roomData.user2Id,
                user2Nickname: roomData.user2Nickname,
                user2Img: roomData.user2Img
            }
        }
        

        await chatRoom.findOneAndUpdate(
            {chatRoomName: roomData.roomName},
            {$set: {
                //chatRoomName: roomData.roomName,
                user1Id: roomData.user1Id,
                user1Nickname: roomData.user1Nickname,
                user1Img: roomData.user1Img,
                user2Id: roomData.user2Id,
                user2Nickname: roomData.user2Nickname,
                user2Img: roomData.user2Img}
            },
            {
                upsert: true,
                new: true,
                setDefaultsOnInsert: true,
                returnNewDocument: true
            },
           
            function (error, success) {
                if (error) {
                    console.log(error);
                } else {
                    console.log("대화방 : " + roomData.roomName);
                    console.log(chatLogObj);
                }
            }
        );
    } catch (err) {
        console.log("update err : " + err);
    }


      
      const roomName = roomData.roomName; // data로 들어온 방 이름
      //console.log(`${roomData}`)
      console.log(`${roomData.userName} joined ${roomName}`)
      socket.join(`${roomName}`, () => {
          io.to(`${roomName}`).emit('newUserToChatRoom', data.userName)
      })
  })

  // 채팅 방 나가기
  socket.on('leaveRoom', function (data) {
      const roomData = JSON.parse(data);
      const roomName = roomData.roomName;
      console.log(`${roomData.userName} left ${roomName}`)
      socket.leave(`${roomName}`, () => {
          io.to(roomName).emit('leaveRoom', data.userName)
      })
  })

  // 클라이언트로부터의 새로운 메시지가 수신
  socket.on('message', async function (data) {


      const roomData = JSON.parse(data);

      // charRoomName 에 추가하기 (mongoDB 저장)

      try {

          // mongoDB 데이터 저장 옵션
          const chatLogObj = {
              userId: roomData.userId,
              message: roomData.message,
              createdTimeStr: roomData.time,
              // 프로필 이미지?
          }
          // 찾았는데 없으면 추가하라
          const options = {
              upsert: true,
              new: true,
              setDefaultsOnInsert: true
          };
          // 배열 형식으로 넣는다
          const query = {
              $push: {
                  chatLog: {
                      $each: [chatLogObj],
                      $slice: -100// 최대 100개 저장
                  }
              }
          }

          await chatRoom.findOneAndUpdate(
              {chatRoomName: roomData.roomName},
              query,
              options,
              function (error, success) {
                  if (error) {
                      console.log(error);
                  } else {
                      console.log("대화방 : " + roomData.roomName);
                      console.log(chatLogObj);
                  }
              }
          );
      } catch (err) {
          console.log("update err : " + err);
      }
      console.log('Message from %s: %s', roomData.nickname, roomData.message);

      var msg = {
          from: {
              name: socket.name,
              userid: socket.userid
          },
          name: data.name,
          message: data.message
      };

      // // 메시지를 전송한 클라이언트를 제외한 모든 클라이언트에게 메시지를 전송
      // socket.broadcast.emit('newMessage', msg);

      // 메시지를 전송한 클라이언트에게만 메시지를 전송
      // socket.emit('newMessage', msg);

      // 접속된 모든 클라이언트에게 메시지를 전송
      // io.emit('newMessage', msg);

      // 특정 클라이언트에게만 메시지를 전송
      // io.to(roomData.roomName).emit('newMessage', roomData); // (server to client (메세지), 메세지 내용)

      // 방에 있는 모든 사용자에게 메세지 전송
      socket.broadcast.to(roomData.roomName).emit('newMessage', roomData)
  });

  // force client disconnect from server
  socket.on('forceDisconnect', function () {
      socket.disconnect();
  })

  socket.on('disconnect', function () {
      console.log('user disconnected: ' + socket.name);
  });
});
socketAPI.io = io;


module.exports = {app, socketAPI};
