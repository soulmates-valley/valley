const express = require('express');
const router = express.Router();

const chatController = require('../controller/chat_controller');


router.get('/chatLog', chatController.getChatLog);
router.get('/roomList', chatController.getRoomList);


module.exports = router;
