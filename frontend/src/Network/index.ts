import axios, { AxiosRequestConfig, AxiosResponse } from "axios";

// 请求配置类型
interface OptionsType {
    url: string;
    method: string;
    params?: Record<string, any>; // 参数类型
    data?: Record<string, any>;    // 请求数据
}

// 请求响应数据类型
interface ResponseData<T = any> {
    code: number;
    data: T;
    message: string;
}

// 创建ajax函数
function ajax<T>(options: OptionsType): Promise<ResponseData<T>> {
    const { url, method, params, data } = options;

    return new Promise((resolve, reject) => {
        try {
            // 创建一个 axios 实例
            const axiosInstance = axios.create({
                baseURL: url,  // 基础URL配置
                timeout: 7000, // 设置请求超时
            });

            // 请求拦截器
            axiosInstance.interceptors.request.use(
                (request) => {
                    // 在请求之前进行处理，可以加入 token 认证等
                    // if (localStorage.getItem("token")) {
                    //   let token = localStorage.getItem("token");
                    //   request.headers.Authorization = `Bearer ${token}`;
                    // }
                    return request;
                },
                (error) => {
                    // 请求错误处理
                    return Promise.reject(error);
                }
            );

            // 响应拦截器
            axiosInstance.interceptors.response.use(
                (response) => {
                    // 响应数据可以做进一步处理
                    return response;
                },
                (error) => {
                    // 响应错误处理
                    return Promise.reject(error);
                }
            );

            // 发送请求
            axiosInstance({
                url,
                method,
                params,
                data,
            })
                .then((res: AxiosResponse<ResponseData<T>>) => {
                    // 检查响应数据状态
                    if (res.data.code === 200) {
                        resolve(res.data); // 返回数据
                    } else {
                        resolve(res.data); // 即便是 500 状态，也直接返回数据
                    }
                })
                .catch((err) => {
                    // 请求失败的处理
                    console.error(`请求失败: ${err}`);
                    reject(err);
                });
        } catch (err) {
            // 捕获语法错误
            console.warn(`ajax语法错误: ${err}`);
            reject(err);
        }
    });
}

export default ajax;
