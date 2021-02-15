module.exports = {
    success: (code) => {
        return {
            code: code
        }
    },
    successWithData: (code, data) => {
        return {
            code: code,
            data: data
        }
    },
    fail: (code, message) => {
        return {
            code: code,
            data: message
        }
    },
    failWithMessage: (code, data) => {
        return {
            code: code,
            data: data,
        }
    }
};
