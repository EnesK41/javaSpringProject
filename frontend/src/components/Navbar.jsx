import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Home, LogOut, User as UserIcon, LayoutDashboard, FileText } from 'lucide-react';

const Navbar = ({ user, setUser }) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const dropdownRef = useRef(null);

  // Handles closing the dropdown if you click outside of it
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsDropdownOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [dropdownRef]);

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
    setIsDropdownOpen(false); // Close dropdown on logout
    navigate("/login");
  };

  const loggedInLinks = () => (
    <div className="relative" ref={dropdownRef}>
      {/* User's name that acts as the dropdown toggle */}
      <button
        onClick={() => setIsDropdownOpen(!isDropdownOpen)}
        className="flex items-center text-white text-lg font-medium hover:text-blue-300 transition-colors duration-200"
      >
        <UserIcon className="w-5 h-5 mr-2 text-blue-400" />
        {user.name}
      </button>

      {/* Dropdown Menu */}
      {isDropdownOpen && (
        <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-20 border border-gray-200">
          <div className="px-4 py-2 text-sm text-gray-700 border-b">
            Signed in as <strong className="font-semibold">{user.role}</strong>
          </div>
          {/* Role-specific links */}
          {user.role === "USER" && (
            <Link to="/user-news" onClick={() => setIsDropdownOpen(false)} className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <FileText className="w-4 h-4 mr-3" /> My News
            </Link>
          )}
          {user.role === "PUBLISHER" && (
            <Link to={`/publisher/${user.id}/info`} onClick={() => setIsDropdownOpen(false)} className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <LayoutDashboard className="w-4 h-4 mr-3" /> Dashboard
            </Link>
          )}
          {/* Logout Button */}
          <button
            onClick={logout}
            className="w-full text-left flex items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
          >
            <LogOut className="w-4 h-4 mr-3" /> Logout
          </button>
        </div>
      )}
    </div>
  );

  const loggedOutLinks = () => (
    <>
      <Link to="/login" className="text-white text-lg font-medium hover:text-blue-300 transition-colors duration-200">
        Login
      </Link>
      <Link to="/register" className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-5 rounded-full shadow-md transition duration-300 ease-in-out transform hover:scale-105">
        Register
      </Link>
    </>
  );

  return (
    <nav className="bg-gradient-to-r from-gray-800 to-gray-900 p-4 shadow-xl font-inter">
      <div className="container mx-auto flex justify-between items-center">
        <Link to="/" className="text-white text-3xl font-extrabold tracking-wider hover:text-blue-300 transition-colors duration-200">
          NewsCentral
        </Link>

        <div className="flex items-center space-x-6">
          <Link to="/" className="flex items-center text-white text-lg font-medium hover:text-blue-300 transition-colors duration-200 group">
            <Home className="w-5 h-5 mr-2 text-blue-400 group-hover:text-blue-300" /> Home
          </Link>

          {user ? loggedInLinks() : loggedOutLinks()}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;