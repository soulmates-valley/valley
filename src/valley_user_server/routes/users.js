const express = require('express');
const router = express.Router();
const multer = require('../modules/multer');

const userController = require('../controller/users_controller');


router.post('/verifyEmail', userController.verifyEmail);
router.post('/verifyNickname', userController.verifyNickname);
router.post('/signUp', multer.upload.single('profileImg'), userController.signUp);
router.post('/signIn', userController.signIn);
//router.post('/deleteUser',userController.deleteUser);

module.exports = router;