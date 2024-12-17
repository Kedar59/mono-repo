import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../App.css";

const RegistrationForm: React.FC = () => {
    return (
        <div className="registration-container">
            <div className="form-box">
                <h2 className="form-title">Register</h2>
                <form>
                    <div className="form-group">
                        <label htmlFor="name" className="form-label">Name</label>
                        <input
                            type="text"
                            id="name"
                            className="form-input"
                            placeholder="Enter your name"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="form-input"
                            placeholder="Enter your email"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password" className="form-label">Password</label>
                        <input
                            type="password"
                            id="password"
                            className="form-input"
                            placeholder="Enter your password"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
                        <input
                            type="password"
                            id="confirmPassword"
                            className="form-input"
                            placeholder="Confirm your password"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="phoneNumber" className="form-label">Phone Number</label>
                        <input
                            type="tel"
                            id="phoneNumber"
                            className="form-input"
                            placeholder="Enter your phone number"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="countryCode" className="form-label">Country Code</label>
                        <input
                            type="text"
                            id="countryCode"
                            className="form-input"
                            placeholder="Enter your country code"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="location" className="form-label">Location (Optional)</label>
                        <input
                            type="text"
                            id="location"
                            className="form-input"
                            placeholder="Enter your location"
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