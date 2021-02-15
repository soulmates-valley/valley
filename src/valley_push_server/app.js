const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const mongoConfig = require('./config/mongoDB.json')
const reveice_MQ = require('./modules/receive')
const mongoose = require('mongoose');

// rdb orm - 미사용
const Sequelize = require('sequelize');
const SequelizeAuto = require('sequelize-auto');




// 라우터 등록
const notiRouter = require('./routes/noti');

// mongoDB 연결
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

const app = express();


app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


// 라우터 사용
app.use('/noti', notiRouter);


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

// queue에 있는 메세지들 listen 하기
// modules/receive.js
reveice_MQ.sendFcm();

module.exports = app;
