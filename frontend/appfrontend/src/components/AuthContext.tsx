import React, { createContext, useState , useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { useAuth } from './UseAuth.ts';
import { useNavigate } from "react-router-dom";
// Define JWT payload interface
interface JWTPayload {
    sub: string; // email
    exp: number;
    iat: number;
    // Add other claims as needed
}

// Define the shape of user data
// interface UserData {
//     email: string;
//     // Add other user properties
// }

interface User {
    id: string;
    email: string;
    // phoneNumber?: string; // Optional phone number
    // countryCode?: string;   // Optional country code
    name: string;
    // location?: string;      // Optional location
    // numberOfSpamCallReports?: number; // Optional number of spam call reports
    // numberOfSpamSMSReports?: number; // Optional number of spam SMS reports
    // timestamp?: Date | null;  // Optional timestamp
    verified: boolean;
  }

// Define the shape of authentication data
interface AuthContextType {
    token: string | null;
    user: User | null;
    login: (token: string, user: User) => void;
    logout: () => void;
    authenticatedFetch: (url: string) => Promise<Response>,
    isAuthenticated: boolean;
}

// Create the context with a default value
const AuthContext = createContext<AuthContextType>({
    token: null,
    user: null,
    login: () => {},
    logout: () => {},
    authenticatedFetch: async () => new Response(),
    isAuthenticated: false
});

// Authentication Provider Component
const AuthProvider: React.FC<{children: React.ReactNode}> = ({ children }) => {
    // State for token and user
    const [token, setToken] = useState<string | null>(null);
    const [user, setUser] = useState<User | null>(null);

    // Load token and user from localStorage on initial render
    useEffect(() => {
        const storedToken = localStorage.getItem('authToken');
        const storedUser = localStorage.getItem('user');
        if (storedToken && storedUser) {
            try {
                // Decode token and set user data
                const parsedUser = JSON.parse(storedUser);  
                const decoded = jwtDecode<JWTPayload>(storedToken);
                setToken(storedToken);
                if(decoded.sub==parsedUser.email){
                    setUser(parsedUser);
                }
            } catch (error) {
                // If decoding fails, clear the token
                localStorage.removeItem('authToken');
                console.log(error)
            }
        }
    }, []);

    // Login method
    const login = (newToken: string, user: User) => {
        try {
            // Decode token
            const decoded = jwtDecode<JWTPayload>(newToken);
            if(decoded.sub == user.email){
                localStorage.setItem('user', JSON.stringify(user));
            }
            // Store token in localStorage
            localStorage.setItem('authToken', newToken);
            setToken(newToken);

            // Set user data from token
            setUser(user);
        } catch (error) {
            console.error('Invalid token', error);
        }
    };

    // Logout method
    const logout = () => {
        // Remove token from localStorage
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        // Clear state
        setToken(null);
        setUser(null);
    };

    // Create a fetch wrapper for authenticated requests
    const authenticatedFetch = async (url: string, methodName: string) => {
        const defaultOptions: RequestInit = {
            method:methodName,
            headers: {
                'Authorization': token ? `Bearer ${token}` : '',
                'Content-Type': 'application/json',
            },
        };

        return fetch(url, defaultOptions);
    };

    // Provide context value
    const contextValue = {
        token,
        user,
        login,
        logout,
        isAuthenticated: !!token,
        authenticatedFetch, // Add fetch wrapper to context
    };

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};
const PrivateRoute: React.FC<{children: React.ReactNode}> = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isAuthenticated) {
            // Redirect to login if not authenticated
            navigate('/login');
        }
    }, [isAuthenticated, navigate]);

    return isAuthenticated ? <>{children}</> : null;
};

export { AuthProvider , AuthContext , PrivateRoute };