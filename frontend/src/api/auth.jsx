import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080",
});

// Add token to requests if logged in
API.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// --- Auth Endpoints ---
export const login = (email, password) => {
  return API.post("/auth/login", { email, password });
};

export const register = ({ name, email, password, role }) => {
  return API.post("/auth/register", { name, email, password, role });
};

// --- Publisher Endpoints ---
export const getPublisherInfo = (id) => {
  return API.get(`/api/publisher/${id}/info`); // Standardized
};

export const getNewsByPublisher = (publisherId) => {
  return API.get(`/api/publisher/${publisherId}/news`); // Standardized
};

export const createNews = (newsData) => {
  return API.post("/api/publisher/news", newsData); // Standardized
};

export const deleteNews = (newsId) => {
  return API.delete(`/api/publisher/news/${newsId}`); // Standardized
};

// --- User Endpoints ---
export const getUserInfo = (id) => {
  return API.get(`/api/user/${id}/info`); // Standardized
};

// --- News Endpoints ---
export const getNews = (query = 'latest headlines', country = 'us', page = 0, size = 9) => {
  return API.get(`/api/news?query=${query}&country=${country}&page=${page}&size=${size}`);
};

export const getApiNewsById = (id) => {
  return API.get(`/api/news/${id}`);
};

export const recordApiNewsView = (newsId) => {
  return API.post(`/api/news/${newsId}/view`);
};

export const getLocalNews = (page = 0, size = 9) => {
  return API.get(`/api/local-news?page=${page}&size=${size}`); 
};

export const getLocalNewsById = (id) => {
  return API.get(`/api/local-news/${id}`);
};

export const recordLocalNewsView = (newsId) => {
  return API.post(`/api/local-news/${newsId}/view`);
};

export default API;