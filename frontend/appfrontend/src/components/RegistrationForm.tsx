import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../App.css";

const RegistrationForm: React.FC = () => {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
        phoneNumber: "",
        countryCode: "",
        location: "",
        isVerified: true,
        numberOfSpamCallReports: 0,
        numberOfSpamSMSReports: 0,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if(formData.password != formData.confirmPassword){
            alert("Passwords dont match");
        }
        const requestBody = {
            email: formData.email,
            password: formData.password,
            phoneNumber: formData.phoneNumber,
            countryCode: formData.countryCode,
            name: formData.name,
            isVerified: true,
            location: formData.location,
            numberOfSpamCallReports: 0,
            numberOfSpamSMSReports: 0,
        };

        try {
            const response = await fetch(
                "http://localhost:8080/gateway/auth/addNewUser",
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
        <div className="registration-container">
            <div className="form-box">
                <h2 className="form-title">Register</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name" className="form-label">Name</label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            className="form-input"
                            placeholder="Enter your name"
                            value={formData.name}
                            onChange={handleChange}
                        />
                    </div>
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
                    <div className="form-group">
                        <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            className="form-input"
                            placeholder="Confirm your password"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="phoneNumber" className="form-label">Phone Number</label>
                        <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            className="form-input"
                            placeholder="Enter your phone number"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="countryCode" className="form-label">Country Code</label>
                        <input
                            type="text"
                            id="countryCode"
                            name="countryCode"
                            className="form-input"
                            placeholder="Enter your country code"
                            value={formData.countryCode}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="location" className="form-label">Location (Optional)</label>
                        <input
                            type="text"
                            id="location"
                            name="location"
                            className="form-input"
                            placeholder="Enter your location"
                            value={formData.location}
                            onChange={handleChange}
                        />
                    </div>
                    <button
                        type="submit"
                        className="btn submit-btn"
                    >
                        Register
                    </button>
                </form>
                <p className="text-center mt-4">
                    Have an account? 
                    <Link to="/login" className="text-blue-500 hover:underline ml-1">Login page</Link>
                </p>
            </div>
        </div>
    );
};

export default RegistrationForm;