var express = require('express');
var router = express.Router();

// 라우터 연결
router.use('/users', require('./users'));
router.use('/profile', require('./profile'));


module.exports = router;
