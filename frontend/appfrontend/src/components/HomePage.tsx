import { useNavigate } from "react-router-dom";
import { useAuth } from "./UseAuth";

const HomePage: React.FC = () => {
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();

    const handleCardClick = (path: string) => {
        if (!isAuthenticated) {
            navigate('/login');
        } else {
            navigate(path);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            
            <div className="text-center py-20 px-4">
                <h1 className="text-5xl font-bold mb-4">
                    Welcome to <span className="text-red-600">Mouthshut</span>
                </h1>
                <p className="text-xl text-gray-600 mb-12">
                    Your trusted platform for authentic company reviews and ratings
                </p>
            </div>

            {/* Features Grid */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mb-20">
                <div className="grid md:grid-cols-3 gap-8">
                    {/* Search Companies Card */}
                    <div 
                        onClick={() => handleCardClick('/companies')}
                        className="bg-white rounded-lg shadow-md p-8 text-center hover:shadow-lg transition-shadow cursor-pointer"
                    >
                        <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg className="w-8 h-8 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                        </div>
                        <h3 className="text-xl font-semibold mb-2">Search Companies</h3>
                        <p className="text-gray-600">Find and review companies in our database</p>
                    </div>

                    {/* Register Company Card */}
                    <div 
                        onClick={() => handleCardClick('/company/register')}
                        className="bg-white rounded-lg shadow-md p-8 text-center hover:shadow-lg transition-shadow cursor-pointer"
                    >
                        <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg className="w-8 h-8 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                            </svg>
                        </div>
                        <h3 className="text-xl font-semibold mb-2">Register Company</h3>
                        <p className="text-gray-600">Add your company to our platform</p>
                    </div>

                    {/* Your Profile Card */}
                    <div 
                        onClick={() => handleCardClick('/profile')}
                        className="bg-white rounded-lg shadow-md p-8 text-center hover:shadow-lg transition-shadow cursor-pointer"
                    >
                        <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg className="w-8 h-8 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                            </svg>
                        </div>
                        <h3 className="text-xl font-semibold mb-2">Your Profile</h3>
                        <p className="text-gray-600">Manage your account and reviews</p>
                    </div>
                </div>
            </div>

            {/* Why Choose Section */}
            <div className="bg-white py-20">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <h2 className="text-3xl font-bold text-center mb-12">Why Choose Mouthshut?</h2>
                    <div className="grid md:grid-cols-3 gap-8">
                        {/* Verified Reviews */}
                        <div className="text-center">
                            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <svg className="w-6 h-6 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-semibold mb-2">Verified Reviews</h3>
                            <p className="text-gray-600">All reviews are from verified users</p>
                        </div>

                        {/* Real-time Updates */}
                        <div className="text-center">
                            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <svg className="w-6 h-6 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-semibold mb-2">Real-time Updates</h3>
                            <p className="text-gray-600">Get instant notifications</p>
                        </div>

                        {/* Telegram Integration */}
                        <div className="text-center">
                            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <svg className="w-6 h-6 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-semibold mb-2">Telegram Integration</h3>
                            <p className="text-gray-600">Connect via Telegram bot</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HomePage;