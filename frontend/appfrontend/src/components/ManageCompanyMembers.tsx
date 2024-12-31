// In any component where you need to make an authenticated request
import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from './UseAuth.ts';
import "../App.css";
import { Role } from "./CompanyProfile.tsx";

interface CompanyMemberProfile {
    profile: {
        id: string;
        email: string;
        name: string;
        location: string;
        numberOfSpamCallReports: number;
        numberOfSpamSMSReports: number;
        verified: boolean;
    };
    role: Role;
}
const ManageCompanyMembers: React.FC = () => {
    const { authenticatedFetch, user } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const company = location.state?.company as Company;
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [CompanyMemberProfileList, setCompanyMemberProfileList] = useState<CompanyMemberProfile[]>([]);
    const [newProfileEmail, setNewProfileEmail] = useState<string>('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        const fetchCompanyMembers = async () => {
            if (!company) return;

            try {
                const response = await authenticatedFetch(
                    `http://localhost:8080/review_api/company/profile/${user?.id}/company/${company.id}/memberManagementPage`, "GET"
                );

                if (!response.ok) {
                    throw new Error("Failed to fetch reviews");
                }

                const data: CompanyMemberProfile[] = await response.json();
                setCompanyMemberProfileList(data);
            } catch (err) {
                setError((err as Error).message);
                setCompanyMemberProfileList([]);
            } finally {
                setLoading(false);
            }
        };
        fetchCompanyMembers();
    }, [user, authenticatedFetch, company]);

    const handlePromote = async () => {
        if (!newProfileEmail.trim()) return;

        setLoading(true);
        setError(null);
        try {
            const response = await authenticatedFetch(
                `http://localhost:8080/review_api/company/profile/${user?.id}/company/${company.id}/promote`,
                'POST',
                {
                    email: newProfileEmail,
                }
            );
            if (!response.ok) {
                throw new Error("Failed to fetch companies");
            }
            const data: CompanyMemberProfile = await response.json();
            if (response.status === 200) {
                const membersList: CompanyMemberProfile[] = CompanyMemberProfileList;
                membersList.push(data);
                setSuccess('Promoted successfully!');
                setCompanyMemberProfileList(membersList);
            } else if (response.status === 201 || response.status === 202) {
                setError(`Error: ${data.message}`);
            } else {
                setError('An unexpected error occurred');
            }


        } catch (err) {
            setError((err as Error).message);
            setCompanies(null);
        } finally {
            setLoading(false);
        }
    };
    const handleDemote = async (email: string) => {
        setLoading(true);
        setError(null);
        try {
            console.log("email : " + email);
            const response = await authenticatedFetch(
                `http://localhost:8080/review_api/company/profile/${user?.id}/company/${company.id}/demote`,
                'POST',
                {
                    email: email,
                }
            );
            if (!response.ok) {
                throw new Error("Failed to fetch companies");
            }
            const data: CompanyMemberProfile = await response.json();
            console.log("data : " + data);
            if (response.status === 200) {
                const membersList: CompanyMemberProfile[] = CompanyMemberProfileList;
                setSuccess('Demoted successfully!');
                setCompanyMemberProfileList(membersList.filter(member => member.profile.email !== data.profile.email));
            } else if (response.status === 201 || response.status === 202) {
                setError(`Error: ${data.message}`);
            } else {
                setError('An unexpected error occurred');
            }
        } catch (err) {
            setError((err as Error).message);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <div className="text-center p-4">Loading...</div>;
    }

    // if (error) {
    //     return <div className="text-red-500 p-4 text-center">{error}</div>;
    // }

    if (!CompanyMemberProfileList) {
        return <div className="text-center p-4">No company members found.</div>;
    }

    return (
        <div className="max-w-6xl mx-auto px-4 py-8">
            {/* Header Section */}
            <div className="mb-8">
                <h2 className="text-2xl font-bold text-gray-900 mb-2">
                    Managing Company Members
                </h2>
                <p className="text-gray-600">
                    {company.name}
                </p>
            </div>

            {/* Promote User Section */}
            <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                <h3 className="text-lg font-semibold mb-4">Add New Moderator</h3>
                <div className="flex gap-4">
                    <input
                        type="text"
                        value={newProfileEmail}
                        onChange={(e) => setNewProfileEmail(e.target.value)}
                        placeholder="Enter user's email"
                        className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                    <button
                        onClick={handlePromote}
                        className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition-colors duration-200 flex items-center gap-2"
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
                        </svg>
                        Promote to Moderator
                    </button>
                </div>

                {/* Status Messages */}
                {error && (
                    <div className="text-red-500 text-sm mt-2">{error}</div>
                )}
                {success && (
                    <div className="text-green-500 text-sm mt-2">{success}</div>
                )}
            </div>

            {/* Members List */}
            <div className="bg-white rounded-lg shadow-md overflow-hidden">
                <div className="px-6 py-4 border-b border-gray-200">
                    <h3 className="text-lg font-semibold text-gray-900">Company Members</h3>
                </div>
                <ul className="divide-y divide-gray-200">
                    {CompanyMemberProfileList.map((profile) => (
                        <li key={profile.profile.id} className="px-6 py-4 hover:bg-gray-50">
                            <div className="flex items-center justify-between">
                                <div>
                                    <p className="text-sm font-medium text-gray-900">{profile.profile.name}</p>
                                    <p className="text-sm text-gray-500">{profile.profile.email}</p>
                                </div>
                                <div className="flex items-center gap-4">
                                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium
                                        ${profile.role === Role.ADMIN
                                            ? 'bg-purple-100 text-purple-800'
                                            : profile.role === Role.MODERATOR
                                                ? 'bg-blue-100 text-blue-800'
                                                : 'bg-gray-100 text-gray-800'
                                        }`}>
                                        {profile.role}
                                    </span>
                                    {profile.role === Role.MODERATOR && (
                                        <button
                                            onClick={() => handleDemote(profile.profile.email)}
                                            className="inline-flex items-center px-3 py-2 border border-red-300 text-sm leading-4 font-medium rounded-md text-red-700 bg-white hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                                        >
                                            <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                            </svg>
                                            Demote
                                        </button>
                                    )}
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default ManageCompanyMembers;