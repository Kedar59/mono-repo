import React, { useState , useEffect} from "react";
import { Link , useNavigate } from "react-router-dom";
import { useAuth } from './UseAuth.ts';
import "../App.css";
import getResponseData from "../util";

interface Company {
    id: string;
    name: string;
    owner: {
        number: string;
        countryCode: string;
    };
    description: string;
    rating: number;
}
const SearchCompanies: React.FC = () => {
    const [companies, setCompanies] = useState<Company[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const {authenticatedFetch } = useAuth();

    useEffect(() => {
        const fetchCompanies = async () => {
            try {
                setLoading(true);
                const response = await authenticatedFetch(
                    'http://localhost:8080/review_api/company/allCompanies',
                    'GET'
                );

                if (!response.ok) {
                    throw new Error('Failed to fetch companies');
                }

                const data:Company[] = await response.json();
                setCompanies(data);
            } catch (err) {
                console.error('Error fetching companies:', err);
                setError('Failed to fetch companies. Please try again later.');
            } finally {
                setLoading(false);
            }
        };


        fetchCompanies();

    }, [authenticatedFetch]);


    return (
        <div className="min-h-screen bg-gray-100 py-8">
            <div className="max-w-7xl mx-auto px-4">
                <div className="bg-white rounded-lg shadow-lg p-6">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-3xl font-bold">Companies</h1>
                        <Link
                            to="/company/register"
                            className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600"
                        >
                            Register Company
                        </Link>
                    </div>

                    {loading && (
                        <div className="text-center py-8">
                            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
                            <p className="mt-4 text-gray-600">Loading companies...</p>
                        </div>
                    )}

                    {error && (
                        <div className="bg-red-50 text-red-600 p-4 rounded-lg mb-6">
                            {error}
                        </div>
                    )}

                    {!loading && !error && companies.length === 0 && (
                        <div className="text-center py-8">
                            <p className="text-gray-600">No companies found.</p>
                        </div>
                    )}

                    {!loading && !error && companies.length > 0 && (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {companies.map((company,index) => (
                                <div
                                    key={index}
                                    className="bg-gray-50 rounded-lg p-6 hover:shadow-md transition-shadow"
                                >
                                    <h3 className="text-xl font-semibold mb-4">Name : {company.name}</h3>
                                    <h3 className="text-xl font-semibold mb-4">Description :{company.description}</h3>
                                    <h3 className="text-xl font-semibold mb-4">Rating :{company.rating} / 5</h3>
                                    {/* <div className="flex space-x-4">
                        <Link
                          to={/company/${encodeURIComponent(company)}}
                          className="text-blue-500 hover:text-blue-600"
                        >
                          View Details
                        </Link>
                        <Link
                          to={/company/${encodeURIComponent(company)}/reviews}
                          className="text-green-500 hover:text-green-600"
                        >
                          Reviews
                        </Link>
                      </div> */}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};


export default SearchCompanies;
