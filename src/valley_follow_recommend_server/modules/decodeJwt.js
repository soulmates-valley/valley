const jwt = require('jsonwebtoken');
const secretKey = 'a@u#t%hse#rver0102test!@#%';



module.exports = {
    decode: (accessToken) => {
        const result = jwt.verify(accessToken, secretKey, (err, decoded) => {
            return decoded;
        })
        return result;
    }
}
