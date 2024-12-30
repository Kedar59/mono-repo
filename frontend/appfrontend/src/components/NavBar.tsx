import { Link } from "react-router-dom";
import { useAuth } from "./UseAuth";
import { useState } from "react";

const NavBar: React.FC = () => {
    const { isAuthenticated, logout, user } = useAuth();
    const [showDropdown, setShowDropdown] = useState(false);

    return (
        <nav className="bg-white shadow-md">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16 items-center">
                    {/* Logo/Brand */}
                    <div className="flex-shrink-0">
                        <Link to="/" className="flex items-center">
                            <span className="text-2xl font-bold text-red-600">Mouthshut</span>
                        </Link>
                    </div>

                    {/* Auth Buttons */}
                    <div className="flex items-center space-x-4">
                        {!isAuthenticated ? (
                            <>
                                <Link to="/login">
                                    <button className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                        Login
                                    </button>
                                </Link>
                                <Link to="/register">
                                    <button className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-blue-600 bg-blue-100 hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                                        Register
                                    </button>
                                </Link>
                            </>
                        ) : (
                            <div className="flex items-center space-x-4">
                                {/* User Profile Dropdown */}
                                <div className="relative">
                                    <button
                                        onClick={() => setShowDropdown(!showDropdown)}
                                        className="flex items-center space-x-3 focus:outline-none"
                                    >
                                        <div className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
                                            <span className="text-gray-600 font-medium">
                                                {user?.email?.charAt(0).toUpperCase()}
                                            </span>
                                        </div>
                                        <span className="text-gray-700">{user?.email}</span>
                                        <svg 
                                            className={`w-4 h-4 text-gray-500 transform transition-transform ${showDropdown ? 'rotate-180' : ''}`}
                                            fill="none" 
                                            stroke="currentColor" 
                                            viewBox="0 0 24 24"
                                        >
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7" />
                                        </svg>
                                    </button>

                                    {/* Dropdown Menu */}
                                    {showDropdown && (
                                        <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10">
                                            <Link 
                                                to="/profile"
                                                className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                                                onClick={() => setShowDropdown(false)}
                                            >
                                                Profile Settings
                                            </Link>
                                            <button
                                                onClick={() => {
                                                    logout();
                                                    setShowDropdown(false);
                                                }}
                                                className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
                                            >
                                                Sign out
                                            </button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default NavBar;