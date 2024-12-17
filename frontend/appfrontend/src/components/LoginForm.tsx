import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../App.css";


const LoginForm: React.FC = () => {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const requestBody = {
            ...formData
        };

        try {
            const response = await fetch(
                "http://localhost:8080/gateway/auth/login",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(requestBody),
                }
            );
            if (response.ok) {
                console.log("User registered successfully");
                alert("Registration successful");
            } else {
                console.error("Failed to register user");
                alert("Registration failed");
            }
        } catch (error) {
            console.error("Error during registration", error);
            alert("An error occurred while registering");
        }
    };

    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
                <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            className="form-input"
                            placeholder="Enter your email"
                            value={formData.email}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password" className="form-label">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            className="form-input"
                            placeholder="Enter your password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>
                    <button type="submit" className="btn submit-btn">
                        Login
                    </button>
                </form>
                <p className="text-center mt-4">
                    Don’t have an account?
                    <Link to="/register" className="text-blue-500 hover:underline ml-1">Register here</Link>
                </p>
            </div>
        </div>
    );
};


export default LoginForm;
