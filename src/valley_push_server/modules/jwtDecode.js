const jwt = require('jsonwebtoken');
const tokenKey = 'a@u#t%hse#rver0102test!@#%';


// 토큰 payload decode
module.exports = {
    getDecodeData: (accessToken) => {
        const result = jwt.verify(accessToken, tokenKey, (err, decoded) => {
            return decoded;
        })
        return result;
    }
}