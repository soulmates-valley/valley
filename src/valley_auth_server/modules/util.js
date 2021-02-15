module.exports = {
    success: (status, data) => {
        return {
            status: status,
            data: data
        }
    },
    successWithoutData: (status) => {
        return {
            status: status,
        }
    },
    fail: (status) => {
        return {
            status: status,
        }
    }
};
