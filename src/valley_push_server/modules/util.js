module.exports = {
    success: (code, data) => {
        return {
            code: code,
            data: data
        }
    },
    successWithoutData: (status, message) => {
        return {
            status: status,
            //success: true,
            message: message
        }
    },
    fail: (status, message) => {
        return {
            status: status,
            //success: false,
            message: message
        }
    }
};
