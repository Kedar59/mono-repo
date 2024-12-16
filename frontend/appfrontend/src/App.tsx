import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginForm from './components/LoginForm';  // Ensure the correct path
import RegistrationForm from './components/RegistrationForm'; // Ensure the correct path
import NavBar from "./components/NavBar";
import HomePage from "./components/HomePage";





const App: React.FC = () => {
    return (
        <Router>
            <NavBar />
            <Routes>
                {/* Route for Home Page */}
                <Route path="/" element={<HomePage />} />
                {/* Route for Login Form */}
                <Route path="/login" element={<LoginForm />} />

                {/* Route for Registration Form */}
                <Route path="/register" element={<RegistrationForm />} />
            </Routes>
        </Router>
    );
};

export default App;
