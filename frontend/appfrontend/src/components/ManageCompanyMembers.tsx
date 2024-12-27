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
                setSuccess('Bot registered successfully!');
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
    const handleDemote = async () => {
        if (!newProfileEmail.trim()) return;
        setLoading(true);
        setError(null);
        try {
            console.log("email : " + newProfileEmail);
            const response = await authenticatedFetch(
                `http://localhost:8080/review_api/company/profile/${user?.id}/company/${company.id}/demote`,
                'POST',
                {
                    email: newProfileEmail,
                }
            );
            if (!response.ok) {
                throw new Error("Failed to fetch companies");
            }
            const data: CompanyMemberProfile = await response.json();
            console.log("data : " + data);
            if (response.status === 200) {
                const membersList: CompanyMemberProfile[] = CompanyMemberProfileList;
                setSuccess('Bot registered successfully!');
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

    if (error) {
        return <div className="text-red-500 p-4 text-center">{error}</div>;
    }

    if (!CompanyMemberProfileList) {
        return <div className="text-center p-4">No company members found.</div>;
    }

    return (
        <div className="container mx-auto">
            <div className="flex gap-4 mb-4">
                <input
                    type="text"
                    value={newProfileEmail}
                    onChange={(e) => setNewProfileEmail(e.target.value)}
                    placeholder="Enter users email"
                    className="border border-gray-300 rounded-lg px-4 py-2 w-full"
                />
                <button
                    onClick={handlePromote}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                >
                    Promote to Moderator
                </button>
            </div>
            {error && (
                <div className="text-red-500 text-sm">{error}</div>
            )}

            {success && (
                <div className="text-green-500 text-sm">{success}</div>
            )}
            <h2 className="text-xl font-bold mb-4">Managing company members for {company.name}</h2>
            <ul className="list-none p-4">
                {CompanyMemberProfileList.map((profile) => (
                    <li key={profile.profile.id} className="flex items-center border-b border-gray-200 py-2">
                        <div className="mr-4">
                            <p className="text-lg font-medium">{profile.profile.name}</p>
                            <p className="text-gray-500">{profile.profile.email}</p>
                        </div>
                        <span className="inline-block bg-gray-200 px-2 rounded-full text-xs font-semibold text-gray-700">
                            {profile.role}
                        </span>
                        {profile.role === Role.MODERATOR && (
                            <>
                                <button onClick={()=>{
                                    setNewProfileEmail(profile.profile.email);
                                    handleDemote();}} className="bg-red-500 text-white px-6 py-3 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400">
                                    Demote from Moderator
                                </button>
                            </>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ManageCompanyMembers;