require("dotenv").config();

var express = require('express');
var router = express.Router();


const statusCode = require('../modules/statuscode');
const util = require('../modules/util');
const esClient = require('../modules/es');

//인기검색어 처리하기 위해 hit수 만큼 저장
var saveSearchedKey = async(keyword) => {
    try {
        const collectKeyword = await esClient.index({
            index: 'topkeyword',
            type: '_doc',
            body: {
                "searchedKey": keyword
            },
        });

    } catch (err) {
        console.log(err);
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
    }
}


//인기검색어
router.get('/popular',async (req, res) => {

    var searchResult = []

    try {
        const response = await esClient.search({
            index: 'topkeyword',
            type: '_doc',
            body: {
                from: 0,
                size: 10,
                query: {
                    match_all: {}
                },
                aggs: {
                    total: {
                        terms: {
                            field: "searchedKey"
                        }
                    }
                }
            }
        });
        response.aggregations.total.buckets.forEach(function (value){
            searchResult.push(value.key);
        });

    }  catch (err) {
        console.log(err);
    }
    if (searchResult.length !== 0)
        res.status(statusCode.OK).send(util.success(statusCode.OK,searchResult));
    else
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
});

//유저검색
router.get('/user',async (req, res) => {

    var keyword = req.query.q;
    var searchResult = []

    saveSearchedKey(keyword);

    keyword = "*"+keyword+"*";

    console.log(keyword);

    try {
        const response = await esClient.search({
            index: 'user',
            type: '_doc',
            body: {
                from: 0,
                size: 50,
                query: {
                    wildcard: {
                        "nickname": keyword
                    }
                }
            }
        });
        console.log(response.hits.hits);
        response.hits.hits.forEach(function (value){
            searchResult.push(value._source);
        });

    } catch (err) {
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
    }

    if (searchResult.length !== 0)
        res.status(statusCode.OK).send(util.success(statusCode.OK,searchResult));
    else
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
});


router.get('/feed',async (req, res) => {

    var keyword = req.query.q;
    var getPage = parseInt(req.query.page);
    var size = parseInt(req.query.size);

    var searchResult = []
    var page = getPage * 10

    saveSearchedKey(keyword);

    keyword = "*"+keyword+"*";

    try {
        //검색결과가 10000개까지 나오는 것으로제한
        if (page >= 1000)
            res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));

        const response = await esClient.search({
            index: 'feed',
            type: '_doc',
            body: {
                // sort:[{"id":"asc"}],
                from: page,
                size: size,
                query: {
                    query_string: {
                        "query": keyword,
                        "fields": ["content", "nickname"]

                    }
                }
            }
        });
        response.hits.hits.forEach(function (value) {
            searchResult.push(value._source);
        });
    } catch (err) {
        console.log(err);
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
    }

    if (searchResult.length !== 0) {
        ++getPage;
        res.status(statusCode.OK).send(util.successWithPage(statusCode.OK, searchResult, getPage));
    }
    else
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
});

//해시태그 검색 -1차뷰
router.get('/hashTag',async (req, res) => {
    var keyword = req.query.q;
    var searchResult = []

    saveSearchedKey(keyword);
    
    try {
        const response = await esClient.search({
            index: 'hashtag',
            type: '_doc',
            body: {
                from:0,
                size:50,
                query: {
                    prefix: {
                        "hashTag": keyword
                    }
                    //해시태그는 접두어검색부터 검
                },
                aggs: {
                    total: {
                        terms: {
                            field: "hashTag"
                        }
                    }
                }
            }
        });

        response.aggregations.total.buckets.forEach(function (value){
            searchResult.push(value);
        });

    }  catch (err) {
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
        console.log(err);
    }

    if (searchResult.length !== 0)
        res.status(statusCode.OK).send(util.success(statusCode.OK,searchResult));
    else
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
});

//해시태그 상세검색
router.get('/hashTag/detail',async (req, res) => {

    var keyword = req.query.q;
    var getPage = parseInt(req.query.page);
    var size = parseInt(req.query.size);

    var searchResult = []
    var page = getPage * 10

    if (page >= 1000)
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));

    try {
        const response = await esClient.search({
            index: 'feed',
            type: '_doc',
            body: {
                from: page,
                size: size,
                query: {
                    term: {
                        "hashTag": keyword
                    }
                }
            }
        });

        response.hits.hits.forEach(function (value){
            searchResult.push(value._source);
        });

    }  catch (err) {
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
    }
    if (searchResult.length !== 0){
        ++getPage;
        res.status(statusCode.OK).send(util.successWithPage(statusCode.OK, searchResult, getPage));}
    else
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.NO_CONTENT));
});

//유저 프로필 업데이트
router.post('/user/update',async (req, res) => {

    var userId = parseInt(req.body.id);
    var nickname = req.body.nickname;
    var interest = req.body.interest;
    var profileImg = req.body.profileImg;

    var theScript = `ctx._source.nickname = '${nickname}';
                     ctx._source.interest = '${interest}';
                     ctx._source.profileImg = '${profileImg};'`

    try {
        const response = await esClient.updateByQuery({
            index: 'user',
            type: '_doc',
            body: {
                script: {
                    lang:'painless',
                    source: theScript
                },
                query: {
                    match: {
                        id: userId
                    }
                }
            }
        });

    }  catch (err) {
        console.log(err);
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
    }
        res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK));
});

//피드 업데이트
router.post('/feed/update',async (req, res) => {

    var postId = parseInt(req.body.postId);
    var content = req.body.content;
    var link = req.body.link;
    var code = req.body.code;
    var hashTag = req.body.hashTag;

    var theScript = `ctx._source.content = '${content}';
                     ctx._source.link = '${link}';
                     ctx._source.code = '${code}';
                     ctx._source.hashTag = '${hashTag}'`;

    try {
        const response = await esClient.updateByQuery({
            index: 'feed',
            type: '_doc',
            body: {
                script: {source:theScript},
                query: {
                    match: {
                        postId: postId
                    }
                }
            }
        });

    }  catch (err) {
        console.log(err)
        res.status(statusCode.NOT_FOUND).send(util.fail(statusCode.NOT_FOUND));
    }
    res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK));
});


module.exports = router;