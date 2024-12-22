// In any component where you need to make an authenticated request
import React, { useState , useEffect } from "react";
// import { Link , useNavigate } from "react-router-dom";
import { useAuth } from './UseAuth.ts';
import "../App.css";

interface Profile {
    id: string;
    email: string;
    phoneNumber?: string;
    countryCode?: string;
    name: string;
    location?: string;
    numberOfSpamCallReports: number;
    numberOfSpamSMSReports: number;
    timestamp: string | null;
    verified: boolean;
}

const Profile: React.FC = () => {
    const { authenticatedFetch, user } = useAuth();
    const [profile, setProfile] = useState<Profile | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                setLoading(true);
                const response = await authenticatedFetch(
                    `http://localhost:8080/truecaller_api/profile/getProfileByEmail/${user?.email}`,"GET"
                );
                
                if (!response.ok) {
                    throw new Error('Failed to fetch profile');
                }

                const data = await response.json();
                setProfile(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'An error occurred');
            } finally {
                setLoading(false);
            }
        };

        if (user?.email) {
            fetchProfile();
        }
    }, [user?.email, authenticatedFetch]);

    if (loading) {
        return <div className="text-center p-4">Loading...</div>;
    }

    if (error) {
        return <div className="text-red-500 p-4 text-center">{error}</div>;
    }

    if (!profile) {
        return <div className="text-center p-4">No profile data found</div>;
    }

    return (
        <div className="max-w-2xl mx-auto p-4">
            <div className="bg-white shadow-lg rounded-lg p-6">
                <h1 className="text-2xl font-bold mb-6 text-gray-800">Profile Information</h1>
                
                <div className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <h3 className="text-sm font-medium text-gray-500">Name</h3>
                            <p className="text-lg text-gray-800">{profile.name}</p>
                        </div>
                        
                        <div>
                            <h3 className="text-sm font-medium text-gray-500">Email</h3>
                            <p className="text-lg text-gray-800">{profile.email}</p>
                        </div>

                        {profile.phoneNumber && (
                            <div>
                                <h3 className="text-sm font-medium text-gray-500">Phone Number</h3>
                                <p className="text-lg text-gray-800">
                                    {profile.countryCode} {profile.phoneNumber}
                                </p>
                            </div>
                        )}

                        {profile.location && (
                            <div>
                                <h3 className="text-sm font-medium text-gray-500">Location</h3>
                                <p className="text-lg text-gray-800">{profile.location}</p>
                            </div>
                        )}
                    </div>

                    <div className="border-t pt-4 mt-4">
                        <h3 className="text-lg font-medium text-gray-700 mb-2">Spam Reports</h3>
                        <div className="grid grid-cols-2 gap-4">
                            <div className="bg-gray-50 p-3 rounded">
                                <h4 className="text-sm text-gray-500">Call Reports</h4>
                                <p className="text-xl font-semibold text-gray-800">
                                    {profile.numberOfSpamCallReports}
                                </p>
                            </div>
                            <div className="bg-gray-50 p-3 rounded">
                                <h4 className="text-sm text-gray-500">SMS Reports</h4>
                                <p className="text-xl font-semibold text-gray-800">
                                    {profile.numberOfSpamSMSReports}
                                </p>
                            </div>
                        </div>
                    </div>

                    <div className="flex items-center mt-4">
                        <span className={`px-3 py-1 rounded-full text-sm ${
                            profile.verified 
                                ? 'bg-green-100 text-green-800' 
                                : 'bg-yellow-100 text-yellow-800'
                        }`}>
                            {profile.verified ? 'Verified' : 'Not Verified'}
                        </span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;

// const Profile: React.FC = () => {
//     const { authenticatedFetch, user } = useAuth();
//     const [profile, setProfile] = useState<Profile>({
//         id: "",
//         email: "",
//         phoneNumber: "", // Optional
//         countryCode: "", // Optional
//         name: "",
//         location: "", // Optional
//         numberOfSpamCallReports: 0,
//         numberOfSpamSMSReports: 0,
//         timestamp: null,
//         verified: false,
//       });
//     const fetchProtectedData = async () => {
//         try {
//             const response = await authenticatedFetch('http://localhost:8080/api/protected-route');
//             const data = await response.json();
//             // Handle data
//         } catch (error) {
//             // Handle error
//         }
//     };

//     return (
        
//     );
// };