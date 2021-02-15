const express = require('express');
const router = express.Router();

const notiController = require('../controller/noti_controllers');


router.get('/', notiController.getNotiLog);

module.exports = router;
