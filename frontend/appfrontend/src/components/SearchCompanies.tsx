import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './UseAuth';

export interface Company {
    id: string;
    name: string;
    description?: string;
    ownerEmail: string;
    rating: number;
    numberOfReviews: number;
}

const SearchCompanies: React.FC = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [companies, setCompanies] = useState<Company[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [hasSearched, setHasSearched] = useState(false);
    const { authenticatedFetch } = useAuth();
    const navigate = useNavigate();

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setHasSearched(true);

        try {
            const response = await authenticatedFetch(
                `http://localhost:8080/review_api/company/searchFor/${encodeURIComponent(searchTerm)}`,
                "GET"
            );

            if (!response.ok) {
                throw new Error('Failed to fetch companies');
            }

            const data = await response.json();
            setCompanies(data);
            
            // Log the response to help debug
            console.log('Search response:', data);
            
        } catch (err) {
            console.error('Search error:', err);
            setError('Failed to search companies. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const handleCompanyClick = (company: Company) => {
        navigate('/companyProfile', { state: { company } });
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-4">Search Companies</h1>
                    <p className="text-gray-600">Find and review companies in our database</p>
                </div>

                {/* Search Form */}
                <form onSubmit={handleSearch} className="mb-8">
                    <div className="flex gap-4">
                        <div className="flex-1">
                            <input
                                type="text"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                placeholder="Enter company name"
                                className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            />
                        </div>
                        <button
                            type="submit"
                            className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                        >
                            Search
                        </button>
                        <button
                            type="button"
                            onClick={() => navigate('/company/register')}
                            className="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                        >
                            Register Company
                        </button>
                    </div>
                </form>

                {/* Error Message */}
                {error && (
                    <div className="mb-8 p-4 bg-red-100 text-red-700 rounded-lg">
                        {error}
                    </div>
                )}

                {/* Loading State */}
                {loading && (
                    <div className="flex justify-center items-center py-8">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                    </div>
                )}

                {/* Results Grid */}
                {!loading && companies.length > 0 && (
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {companies.map((company) => (
                            <div
                                key={company.id}
                                onClick={() => handleCompanyClick(company)}
                                className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow cursor-pointer"
                            >
                                <h3 className="text-xl font-semibold mb-2">{company.name}</h3>
                                {company.description && (
                                    <p className="text-gray-600 mb-4 line-clamp-2">{company.description}</p>
                                )}
                                <div className="flex items-center justify-between">
                                    <div className="flex items-center">
                                        <span className="text-yellow-400 mr-1">★</span>
                                        <span className="font-medium">{company.rating.toFixed(1)}</span>
                                        <span className="text-gray-500 ml-1">
                                            ({company.numberOfReviews} reviews)
                                        </span>
                                    </div>
                                    <span className="text-sm text-gray-500">{company.ownerEmail}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* Empty State - Only show when a search has been performed */}
                {!loading && hasSearched && companies.length === 0 && (
                    <div className="text-center py-8">
                        <p className="text-gray-600">No companies found matching your search.</p>
                        <button
                            onClick={() => navigate('/company/register')}
                            className="mt-4 px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                        >
                            Register a New Company
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default SearchCompanies;
