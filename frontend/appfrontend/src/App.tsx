import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginForm from './components/LoginForm';  // Ensure the correct path
import RegistrationForm from './components/RegistrationForm'; // Ensure the correct path

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                {/* Route for Login Form */}
                <Route path="/" element={<LoginForm />} />

                {/* Route for Registration Form */}
                <Route path="/register" element={<RegistrationForm />} />
            </Routes>
        </Router>
    );
};

export default App;
