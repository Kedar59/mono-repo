import React, { useState } from "react";
import "../App.css";

const RegistrationForm: React.FC = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [confirmPassword, setConfirmPassword] = useState<string>("");
    const [fullName, setFullName] = useState<string>("");
    const [phoneNumber, setPhoneNumber] = useState<string>("");
    const [countryCode, setCountryCode] = useState<string>("");
    const [location, setLocation] = useState<string>("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Your registration logic (e.g., API call) goes here
        alert("Form submitted!");
    };

    return (
        <div className="registration-form-container">
            <h1 className="form-title">Register</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-field">
                    <label htmlFor="fullName" className="form-label">
                        Full Name
                    </label>
                    <input
                        type="text"
                        id="fullName"
                        className="form-input"
                        value={fullName}
                        onChange={(e) => setFullName(e.target.value)}
                        placeholder="Enter your full name"
                        required
                    />
                </div>
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
                        placeholder="Enter a password"
                        required
                    />
                </div>
                <div className="form-field">
                    <label htmlFor="confirmPassword" className="form-label">
                        Confirm Password
                    </label>
                    <input
                        type="password"
                        id="confirmPassword"
                        className="form-input"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm your password"
                        required
                    />
                </div>
                <div className="form-field">
                    <label htmlFor="phoneNumber" className="form-label">
                        Phone Number
                    </label>
                    <input
                        type="tel"
                        id="phoneNumber"
                        className="form-input"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        placeholder="Enter your phone number"
                        required
                    />
                </div>
                <div className="form-field">
                    <label htmlFor="countryCode" className="form-label">
                        Country Code
                    </label>
                    <input
                        type="text"
                        id="countryCode"
                        className="form-input"
                        value={countryCode}
                        onChange={(e) => setCountryCode(e.target.value)}
                        placeholder="Enter your country code"
                        required
                    />
                </div>
                <div className="form-field">
                    <label htmlFor="location" className="form-label">
                        Location
                    </label>
                    <input
                        type="text"
                        id="location"
                        className="form-input"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                        placeholder="Enter your location"
                        required
                    />
                </div>
                <button type="submit" className="submit-button">
                    SIGN UP
                </button>
            </form>
        </div>
    );
};

export default RegistrationForm;