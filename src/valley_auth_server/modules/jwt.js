const randToken = require('rand-token');
const jwt = require('jsonwebtoken');
const TOKEN_EXPIRED = -3;
const TOKEN_INVALID = -2;
const SECRET_ACCESS_KEY= process.env.ACCESS_SECRET;

module.exports = {
    // access token 발급
    // hs256
    issueToken: async (email, userId, nickname) => {
        const payload = {
            userEmail: email,
            userId: userId,
            nickname : nickname
        };
        const result = {
                //TO DO: 토큰 만료시간 변경해야함
            accessToken: jwt.sign(payload,SECRET_ACCESS_KEY,{expiresIn: '7d'}),
            refreshToken: jwt.sign(payload, SECRET_ACCESS_KEY, {expiresIn: '7d'})
        };
        return result;
    },
    verify: async (token) => {
        let decoded;
        try {
            decoded = jwt.verify(token, secretKey);
        } catch (err) {
            if (err.message === 'jwt expired') {
                console.log('expired token');
                return TOKEN_EXPIRED;
            } else if (err.message === 'invalid token') {
                console.log('invalid token');
                console.log(TOKEN_INVALID);
                return TOKEN_INVALID;
            } else {
                console.log('invalid token');
                return TOKEN_INVALID;
            }
        }
        return decoded;
    },
    refresh: (email, userId, nickname) => {
        const payload = {
            userEmail: email,
            userId: userId,
            nickname : nickname
        };
        return jwt.sign(payload, SECRET_ACCESS_KEY,{expiresIn: '7d'});
    }
}