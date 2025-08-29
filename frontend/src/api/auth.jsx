import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080", // your Spring Boot backend
});

// Add token to requests if logged in
API.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Login function (send email)
export const login = (email, password) => {
  console.log("[DEBUG] Sending login request:", email);
  return API.post("/auth/login", { email, password });
};
// Register function
export const register = ({ name, email, password, role }) => {
  console.log("[DEBUG] Sending register request:", name, email, role);
  return API.post("/auth/register", { name, email, password, role });
};

export const getPublisherInfo = (id) => {
  console.log("[DEBUG] Sending getPublisherInfo request:", id);
  return API.get(`/publisher/${id}/info`);
};

export const getUserInfo = (id) => {
  console.log("[DEBUG] Sending getUserInfo request:", id);
  return API.get(`/user/${id}/info`);
};

export const getNews = (query = 'latest headlines', country = 'us', page = 0, size = 10) => {
  return API.get(`/api/news?query=${query}&country=${country}&page=${page}&size=${size}`);
};

export const createNews = (newsData) => {
  return API.post("/publisher/news", newsData);
};

export const deleteNews = (newsId) => {
  return API.delete(`/publisher/news/${newsId}`);
};

export const getLocalNews = (page = 0, size = 10) => {
    return API.get(`/api/local-news?page=${page}&size=${size}`);
}

export const getNewsByPublisher = (publisherId) => {
    return API.get(`/publisher/${publisherId}/news`); 
}

export default API;
