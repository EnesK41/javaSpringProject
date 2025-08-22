import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/auth";

const Register = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("USER"); // default role
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await register({ name, email, password, role }); // match your RegisterDTO
      navigate("/login"); // redirect to login after successful registration
    } catch (err) {
      setError(err.response?.data || "Registration failed");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Register</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={e => setName(e.target.value)}
        required
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={e => setEmail(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={e => setPassword(e.target.value)}
        required
      />
      <select value={role} onChange={e => setRole(e.target.value)}>
        <option value="USER">User</option>
        <option value="PUBLISHER">Publisher</option>
      </select>
      <button type="submit">Register</button>
    </form>
  );
};

export default Register;
