import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { UserPlus } from 'lucide-react';

const Register = ({ register, setUser }) => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("USER");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const response = await register({ name, email, password, role });
      const token = response.data.token;
      
      localStorage.setItem("token", token);
      
      const payload = JSON.parse(atob(token.split(".")[1]));
      
      const user = { 
        id: payload.userId, 
        name: payload.sub, 
        role: payload.role 
      };
      setUser(user); 

      navigate("/"); 

    } catch (err) {
      console.error("Registration error:", err);
      if (err.response && err.response.data) {
        // If the response data is an object with a 'message' field (from our custom handler)
        setError(err.response.data.message || "Registration failed.");
      } else {
        setError("Registration failed. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[calc(100vh-80px)] flex items-center justify-center bg-gray-100 p-4 font-inter">
      <div className="bg-white p-8 rounded-2xl shadow-2xl w-full max-w-md border border-gray-200">
        <h2 className="text-3xl font-extrabold text-center text-gray-900 mb-8 flex items-center justify-center space-x-3">
          <UserPlus className="w-8 h-8 text-green-600" />
          <span>Register</span>
        </h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
              Name
            </label>
            <input
              id="name"
              name="name"
              type="text"
              placeholder="Your Name"
              value={name}
              onChange={e => setName(e.target.value)}
              required
              className="appearance-none block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm transition-colors duration-200"
            />
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              Email Address
            </label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="your@email.com"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
              className="appearance-none block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm transition-colors duration-200"
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
              placeholder="********"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
              className="appearance-none block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm transition-colors duration-200"
            />
          </div>

          <div>
            <label htmlFor="role" className="block text-sm font-medium text-gray-700 mb-1">
              Role
            </label>
            <select
              id="role"
              name="role"
              value={role}
              onChange={e => setRole(e.target.value)}
              className="block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm transition-colors duration-200"
            >
              <option value="USER">User</option>
              <option value="PUBLISHER">Publisher</option>
            </select>
          </div>

          {error && (
            <p className="text-sm text-red-600 text-center -mt-2">{error}</p>
          )}

          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-lg font-semibold text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-200 ease-in-out transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Registering...' : 'Register'}
            </button>
          </div>
          <div className="text-center text-sm text-gray-600">
            Already have an account? <Link to="/login" className="font-medium text-green-600 hover:text-green-500">Login here</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;
