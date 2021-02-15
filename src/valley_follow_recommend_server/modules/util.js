module.exports = {
    success: (status, data) => {
        return {
            code: status,
            data: data
        }
    },
    successWithPage: (status, data, page) => {
        return {
            code: status,
            data: data,
            page: page
        }
    },

    successWithoutData: (status) => {
        return {
            code: status
        }
    },
    fail: (status, message) => {
        return {
            code: status
        }
    }
};
