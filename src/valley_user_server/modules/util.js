module.exports = {
    successWithMessage: (code, message, data) => {
        return {
            code: code,
            message: message,
            data: data
        }
    },
    success: (code, data) => {
        return {
            code: code,
            data: data
        }
    },
    fail: (code, data) => {
        return {
            code: code,
            data: data
        }
    },
    failWithMessage: (code, message, data) => {
        return {
            code: code,
            message: message,
            data: data
        }
    }
};
