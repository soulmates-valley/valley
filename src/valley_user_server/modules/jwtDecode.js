const jwt = require('jsonwebtoken');
const tokenKey = 'a@u#t%hse#rver0102test!@#%';


module.exports = {
    getDecodeData: (accessToken) => {
        const result = jwt.verify(accessToken, tokenKey, (err, decoded) => {
            return decoded;
        })
        return result;
    }
    /*
    {
        userEmail: 'interestTest1@smilegate.com',
        userId: 43,
        nickname: 'twtwtwtw2',
        iat: 1611807778,
        exp: 1612412578
    }   

    */
}