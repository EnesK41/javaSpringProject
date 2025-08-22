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
export const login = (email, password) =>
  API.post("/auth/login", { email, password });

// Register function
export const register = ({ name, email, password, role }) =>
  API.post("/auth/register", { name, email, password, role });

export default API;
