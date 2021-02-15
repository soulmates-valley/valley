require("dotenv").config()

const redis = require("redis");

const redisClient = redis.createClient({
    //env port,host
    port:process.env.REDIS_PORT,
    host:process.env.HOST,
});

redisClient.on('connect', () => {
    console.log('Client connected to redis');
});

redisClient.on('error', (err) => {
    console.log(err.message);
});

redisClient.on('end', () => {
    console.log('Client disconnected from redis');
});




module.exports = redisClient;