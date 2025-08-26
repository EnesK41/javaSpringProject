import React, { useState, useEffect } from "react"; // 1. Import useEffect
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";
import Home from "./components/Home";
import Login from "./components/Login";
import Register from "./components/Register";
import PublisherInfo from "./components/Publisher-Info";

import { register, login, getPublisherInfo } from "./api/auth";

function App() {
  const [user, setUser] = useState(null);
  

  // 2. ADD THIS useEffect HOOK.
  // This hook runs only once when the application first loads.
  useEffect(() => {
    const token = localStorage.getItem("token");
    console.log("[App.js Effect] Checking for token...", { token });
    // If a token exists, decode it and set the user state.
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        
        // Optional but recommended: Check if the token is expired
        if (payload.exp * 1000 > Date.now()) {
          setUser({
            id: payload.userId,
            name: payload.sub,
            role: payload.role,
          });
          console.log("[App.js Effect] User set from token:", {
            id: payload.userId,
            name: payload.sub,
            role: payload.role,
          });
        } else {
          // If token is expired, remove it
          localStorage.removeItem("token");
        }
      } catch (error) {
        // If token is invalid, remove it
        console.error("Invalid token found in localStorage", error);
        localStorage.removeItem("token");
      }
    }
  }, []); // The empty array [] ensures this runs only once on mount.
  console.log("[App.js Render] Current user state is:", user);

  return (
    <Router>
      <Navbar user={user} setUser={setUser} />
      <Routes>
        <Route path="/" element={<Home user={user} />} />
        <Route path="/login" element={<Login login={login} setUser={setUser} />} />
        <Route path="/register" element={<Register register={register} setUser={setUser} />} />
        <Route path="/publisher/:id/info" element={<PublisherInfo user={user} getPublisherInfo={getPublisherInfo} />} />
        {/* Add UserNews, PublisherNews later */}
      </Routes>
    </Router>
  );
}

export default App;