import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from './UseAuth';

interface CompanyRegistrationForm {
    name: string;
    ownerEmail: string;
    description: string;
}

const RegisterCompany: React.FC = () => {
    const navigate = useNavigate();
    const { authenticatedFetch, user } = useAuth();
    const [formData, setFormData] = useState<CompanyRegistrationForm>({
        name: "",
        ownerEmail: user?.email || "",
        description: "",
    });
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (user && !user.verified) {
            setError("Your account needs to be verified to register a company");
        }
    }, [user]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        if (!user?.verified) {
            setError("Your account needs to be verified to register a company");
            return;
        }

        try {
            const response = await authenticatedFetch(
                "http://localhost:8080/review_api/company/addCompany",
                "POST",
                formData
            );

            const data = await response.json();

            if (response.ok) {
                alert("Company registered successfully!");
                navigate('/companies');
            } else {
                setError(data.message || "Failed to register company");
            }
        } catch (err) {
            setError("An error occurred while registering the company");
            console.error("Error:", err);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 py-8">
            <div className="max-w-2xl mx-auto px-4">
                <div className="bg-white rounded-lg shadow-lg p-6">
                    <h2 className="text-2xl font-bold mb-6">Register New Company</h2>
                    
                    {error && (
                        <div className="bg-red-50 text-red-600 p-4 rounded-lg mb-6">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-4">
                            <label htmlFor="name" className="block text-gray-700 mb-2">
                                Company Name
                            </label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                required
                                disabled={!user?.verified}
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="ownerEmail" className="block text-gray-700 mb-2">
                                Owner Email
                            </label>
                            <input
                                type="email"
                                id="ownerEmail"
                                name="ownerEmail"
                                value={formData.ownerEmail}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                required
                                disabled={!user?.verified}
                            />
                        </div>

                        <div className="mb-6">
                            <label htmlFor="description" className="block text-gray-700 mb-2">
                                Description
                            </label>
                            <textarea
                                id="description"
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                rows={4}
                                required
                                disabled={!user?.verified}
                            />
                        </div>

                        <button
                            type="submit"
                            className={`w-full py-2 px-4 rounded-lg transition-colors ${
                                user?.verified 
                                    ? 'bg-blue-500 text-white hover:bg-blue-600'
                                    : 'bg-gray-400 text-gray-200 cursor-not-allowed'
                            }`}
                            disabled={!user?.verified}
                        >
                            Register Company
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default RegisterCompany; 