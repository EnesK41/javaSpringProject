import React from "react";
import { Link, useNavigate } from "react-router-dom";

const Navbar = ({ user, setUser }) => {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
    navigate("/login");
  };

  return (
    <nav style={{ padding: "1rem", borderBottom: "1px solid #ccc" }}>
      <Link to="/">Home</Link> |{" "}
      {!user ? (
        <>
          <Link to="/login">Login</Link> | <Link to="/register">Register</Link>
        </>
      ) : (
        <>
          {user.role === "USER" && <Link to="/user-news">My News</Link>}
          {user.role === "PUBLISHER" && <Link to="/publisher-news">My News</Link>}
          <button onClick={logout} style={{ marginLeft: "1rem" }}>Logout</button>
        </>
      )}
    </nav>
  );
};

export default Navbar;
