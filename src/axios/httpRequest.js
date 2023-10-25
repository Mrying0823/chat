import axios from "axios";
import qs from "qs";

// 设置默认值
axios.defaults.baseURL = "http://localhost:8081/api";
axios.defaults.timeout = 20000;

// get 请求方法，url 是请求的地址，params 是请求的 json 对象
function toGet(url,params) {
    return axios({
        url: url,
        method: "get",
        params: params
    });
}

// post 请求方法，url 是请求的地址，data 是请求的 json 对象
function doPostJson(url,data) {
    return axios({
        url: url,
        method: "post",
        data: data
    });
}

// post 请求方法，将 json 对象数据转为 qs 格式
function doPost(url,data) {
    let requestData = qs.stringify(data);
    return axios({
        url: url,
        method: "post",
        data: requestData
    })
}

// 导出函数
export {toGet,doPostJson,doPost}