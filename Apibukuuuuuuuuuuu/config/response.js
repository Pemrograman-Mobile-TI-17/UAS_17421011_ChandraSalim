module.exports = {

    commonError:{
        error: true,
        msg: 'Server Bermasalah'
    },
    commonErrorMsg: (msg) => {
        return{
            error: true,
            msg : msg
        }
    },
    commonSuccess: {
        error: false,
        msg: 'Berhasil Memuat Permintaan'
    },
    commonSuccessMsg: (msg) => {
        return{
            error: false,
            msg : msg
        }
    },
    commonResult: (data) => {
        return{
            error: false,
            msg: 'Berhasil Memuat Data',
            data : data
        }
    }
};