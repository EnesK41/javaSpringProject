import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { LogIn as LogInIcon } from 'lucide-react';

const Login = ({ login, setUser }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const res = await login(email, password);
      const token = res.data.token;
      localStorage.setItem("token", token);

      // Decode token payload to get user details
      const payload = JSON.parse(atob(token.split(".")[1]));
      
      const user = { 
        id: payload.userId, // Extract the userId from the token
        name: payload.sub, 
        role: payload.role 
      };
      setUser(user); // Update user state in App.jsx with the complete object

      navigate("/"); // Redirect to home page after successful login
    } catch (err) {
      console.error("Login error:", err);
      if (err.response && err.response.data) {
        // If the response data is an object with a 'message' field (from our custom handler)
        setError(err.response.data.message || "Invalid email or password.");
      } else {
        setError("Invalid email or password. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[calc(100vh-80px)] flex items-center justify-center bg-gray-100 p-4 font-inter">
      <div className="bg-white p-8 rounded-2xl shadow-2xl w-full max-w-sm border border-gray-200">
        <h2 className="text-3xl font-extrabold text-center text-gray-900 mb-8 flex items-center justify-center space-x-3">
          <LogInIcon className="w-8 h-8 text-indigo-600" />
          <span>Login</span>
        </h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              Email Address
            </label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              required
              className="appearance-none block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm transition-colors duration-200"
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="current-password"
              required
              className="appearance-none block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm transition-colors duration-200"
              value={password}
              onChange={e => setPassword(e.target.value)}
            />
          </div>

          {error && (
            <p className="text-sm text-red-600 text-center -mt-2">{error}</p>
          )}

          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-lg font-semibold text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition duration-200 ease-in-out transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Logging in...' : 'Login'}
            </button>
          </div>
          <div className="text-center text-sm text-gray-600">
            Don't have an account? <Link to="/register" className="font-medium text-indigo-600 hover:text-indigo-500">Register here</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
