const express = require('express');
const router = express.Router();
const multer = require('../modules/multer');
const profileController = require('../controller/profile_controller');


router.get('/', profileController.getProfile);
router.post('/modify', multer.upload.single('profileImg'), profileController.modifyProfile);
router.get('/logout', profileController.logout);


module.exports = router;