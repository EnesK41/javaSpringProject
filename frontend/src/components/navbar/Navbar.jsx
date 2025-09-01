import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { Home, LogOut, User as UserIcon, LayoutDashboard, UserCircle } from 'lucide-react';
import './Navbar.scss'; 
import logo from '../assets/logo.png'; 

const Navbar = ({ user, setUser }) => {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
    navigate("/login");
  };

  const loggedInLinks = () => (
    <>
      <span className="nav-link">
        <UserIcon />
        {user.name}
      </span>

      {user.role === "USER" && (
        <Link to={`/user/${user.id}/info`} className="nav-link">
            <UserCircle />
            My Profile
        </Link>
      )}
      {user.role === "PUBLISHER" && (
        <Link to={`/publisher/${user.id}/info`} className="nav-link">
          <LayoutDashboard />
          Dashboard
        </Link>
      )}

      <button onClick={logout} className="nav-button logout-btn">
        <LogOut />
        Logout
      </button>
    </>
  );

  const loggedOutLinks = () => (
    <>
      <Link to="/login" className="nav-link">
        Login
      </Link>
      <Link to="/register" className="nav-button register-btn">
        Register
      </Link>
    </>
  );

  return (
    <nav className="main-nav">
      <div className="navbar-container">
        <div className="nav-left">
          <div className="nav-brand-group">
            <img src={logo} alt="Site Logo" className="nav-logo" />
            <div className="nav-brand-container">
              <Link to="/community-news" className="nav-brand-secret">
                Secret
              </Link>
              <Link to="/" className="nav-brand">
                News
              </Link>
            </div>
          </div>
          <Link to="/" className="nav-link">
            <Home /> Home
          </Link>
        </div>

        <div className="nav-right">
          {user ? loggedInLinks() : loggedOutLinks()}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
