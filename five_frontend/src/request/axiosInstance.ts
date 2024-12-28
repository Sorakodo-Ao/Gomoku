// src/api/axiosInstance.js
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080", // 设置基础URL
  headers: {
    "Content-Type": "application/json",
    // 可以添加其他默认头信息，如授权令牌
  },
});

export default apiClient;
