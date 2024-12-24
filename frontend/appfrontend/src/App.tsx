import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginForm from './components/LoginForm';  // Ensure the correct path
import RegistrationForm from './components/RegistrationForm'; // Ensure the correct path
import NavBar from "./components/NavBar";
import HomePage from "./components/HomePage";
import Profile from "./components/Profile";
import { AuthProvider, PrivateRoute } from './components/AuthContext';
import SearchCompanies from "./components/SearchCompanies";




const App: React.FC = () => {
    return (
        <Router>
            <AuthProvider>
                <NavBar />
                <Routes>
                    {/* Route for Home Page */}
                    <Route path="/" element={<HomePage />} />
                    {/* Route for Login Form */}
                    <Route path="/login" element={<LoginForm />} />
                    {/* Profile Protected route */}
                    <Route 
                        path="/profile" 
                        element={
                            <PrivateRoute>
                                <Profile />
                            </PrivateRoute>
                        } 
                    />
                    {/* Companies */}
                    <Route path="/companies" element={
                            <PrivateRoute>
                                <SearchCompanies />
                            </PrivateRoute>
                        } />
                    {/* Route for Registration Form */}
                    <Route path="/register" element={<RegistrationForm />} />
                </Routes>
            </AuthProvider>
        </Router>
    );
};

export default App;
