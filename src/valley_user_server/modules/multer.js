const path = require('path');
const multerS3 = require('multer-s3');
const multer = require('multer');
const AWS = require('aws-sdk');
AWS.config.loadFromPath(__dirname + '/../config/awsconfig.json');
let s3 = new AWS.S3();


module.exports = {
    upload: multer({
        storage : multerS3({
          s3: s3,
          bucket: "soulmates",
          //acl: 'public-read-write',
          key: function(req,file,cb){
            let extension = path.extname(file.originalname);
            cb(null, Date.now().toString() + extension)
          },
          
        })
      })
}