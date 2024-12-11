import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../App.css";

const LoginForm: React.FC = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Your login logic goes here
        alert("Login form submitted!");
    };

    return (
        <div className="registration-form-container">
            <h1 className="form-title">Login</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-field">
                    <label htmlFor="email" className="form-label">
                        Email
                    </label>
                    <input
                        type="email"
                        id="email"
                        className="form-input"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Enter your email address"
                        required
                    />
                </div>
                <div className="form-field">
                    <label htmlFor="password" className="form-label">
                        Password
                    </label>
                    <input
                        type="password"
                        id="password"
                        className="form-input"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        required
                    />
                </div>
                <button type="submit" className="submit-button">
                    LOGIN
                </button>
            </form>

            <p>
                Don't have an account? <Link to="/register">Register here</Link>
            </p>
        </div>
    );
};

export default LoginForm;
