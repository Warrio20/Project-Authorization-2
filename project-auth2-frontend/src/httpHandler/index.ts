import axios from "axios";

export const URL = "http://localhost:5173/api";

const $api = axios.create();

$api.interceptors.request.use((config) => {
    config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`;
    config.withCredentials = true;
    config.baseURL = URL;
    return config;
})
export default $api;