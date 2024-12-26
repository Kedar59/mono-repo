import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from './UseAuth.ts';
import "../App.css";
import getResponseData from "../util";

export interface Company {
    id: string;
    name: string;
    ownerEmail: string;
    description: string;
    rating: number;
    numberOfReviews: number;
}
const SearchCompanies: React.FC = () => {
    const [searchString, setSearchString] = useState("");
    const [companies, setCompanies] = useState<Company[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { authenticatedFetch } = useAuth();
    const navigate = useNavigate();
    const handleSearch = async () => {
        if (!searchString.trim()) return;

        setLoading(true);
        setError(null);
        try {
            const response = await authenticatedFetch(
                `http://localhost:8080/review_api/company/searchFor/${encodeURIComponent(searchString)}`,
                'GET'
            );
            if (!response.ok) {
                throw new Error("Failed to fetch companies");
            }

            const data: Company[] = await response.json();
            setCompanies(data);
        } catch (err) {
            setError((err as Error).message);
            setCompanies(null);
        } finally {
            setLoading(false);
        }
    };
    const handleCompanyClick = (company: Company) => {
        navigate("/companyProfile", { state: { company } });
    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">Search Companies</h1>
            <div className="flex gap-4 mb-4">
                <input
                    type="text"
                    value={searchString}
                    onChange={(e) => setSearchString(e.target.value)}
                    placeholder="Enter company name"
                    className="border border-gray-300 rounded-lg px-4 py-2 w-full"
                />
                <button
                    onClick={handleSearch}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                >
                    Search
                </button>
            </div>
            <Link
                to="/company/register"
                className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600"
            >
                Register Company
            </Link>

            {loading && <p className="mt-4">Loading...</p>}
            {error && <p className="mt-4 text-red-500">Error: {error}</p>}

            {companies && (
                <div className="mt-4">
                    {companies.length > 0 ? (
                        <ul className="space-y-4">
                            {companies.map((company) => (
                                <li
                                key={company.id}
                                className="border border-gray-300 rounded-lg p-4 shadow-sm"
                              >
                                <h2
                                  className="text-lg font-semibold text-blue-500 cursor-pointer hover:underline"
                                  onClick={() => handleCompanyClick(company)}
                                >
                                  {company.name}
                                </h2>
                                <p className="text-sm text-gray-600">{company.description}</p>
                                <p className="text-sm text-gray-500">
                                  Owner Email: {company.ownerEmail}
                                </p>
                                <p className="text-sm text-gray-500">Rating: {company.rating}</p>
                              </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="mt-4 text-gray-700">
                            The company "{searchString}" isn't registered in our database.
                        </p>
                    )}
                </div>
            )}
        </div>
    );
};


export default SearchCompanies;
