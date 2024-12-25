// In any component where you need to make an authenticated request
import React, { useState , useEffect } from "react";
// import { Link , useNavigate } from "react-router-dom";
import { useAuth } from './UseAuth.ts';
import "../App.css";
import getResponseData from "../util.tsx";

interface Profile {
    id: string;
    email: string;
    phoneNumber?: string;
    countryCode?: string;
    name: string;
    location?: string;
    numberOfSpamCallReports: number;
    numberOfSpamSMSReports: number;
    verified: boolean;
}

const Profile: React.FC = () => {
    const { authenticatedFetch, user } = useAuth();
    const [profile, setProfile] = useState<Profile | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [editableFields, setEditableFields] = useState({
        name: '',
        location: '',
        phoneNumber: '',
        countryCode: '',
    });
    const [isEditing, setIsEditing] = useState(false);
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

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setEditableFields(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSave = async () => {
        try {
            if (!profile) return;

            const updatedData: Profile = {
                ...profile,
                name: editableFields.name,
                location: editableFields.location,
                phoneNumber: editableFields.phoneNumber,
                countryCode: editableFields.countryCode,
            };

            const response = await authenticatedFetch(
                "http://localhost:8080/truecaller_api/profile/update", "POST", updatedData
            );
            if (!response.ok) {
                throw new Error('Failed to fetch profile');
            }
            const newProf: Profile = await getResponseData(response);
            setProfile(newProf);
            setIsEditing(false);
            setError(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        }
    };

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
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-2xl font-bold text-gray-800">Profile Information</h1>
                    <button
                        onClick={() => {
                            if (isEditing) {
                                handleSave();
                            } else {
                                // Populate editableFields with current profile values
                                setEditableFields({
                                    name: profile.name || '',
                                    location: profile.location || '',
                                    phoneNumber: profile.phoneNumber || '',
                                    countryCode: profile.countryCode || '',
                                });
                                setIsEditing(true);
                            }
                        }}
                        className={`px-4 py-2 rounded ${
                            isEditing
                                ? 'bg-green-500 hover:bg-green-600'
                                : 'bg-blue-500 hover:bg-blue-600'
                        } text-white`}
                    >
                        {isEditing ? 'Save Changes' : 'Edit Profile'}
                    </button>
                </div>

                <div className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <h3 className="text-sm font-medium text-gray-500">Name</h3>
                            {isEditing ? (
                                <input
                                    type="text"
                                    name="name"
                                    value={editableFields.name}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                            ) : (
                                <p className="text-lg text-gray-800">{profile.name}</p>
                            )}
                        </div>

                        <div>
                            <h3 className="text-sm font-medium text-gray-500">Email</h3>
                            <p className="text-lg text-gray-800">{profile.email}</p>
                        </div>

                        <div>
                            <h3 className="text-sm font-medium text-gray-500">Phone Number</h3>
                            {isEditing ? (
                                <div className="flex gap-2">
                                    <input
                                        type="text"
                                        name="countryCode"
                                        value={editableFields.countryCode}
                                        onChange={handleInputChange}
                                        placeholder="Country Code"
                                        className="mt-1 block w-1/3 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                    />
                                    <input
                                        type="text"
                                        name="phoneNumber"
                                        value={editableFields.phoneNumber}
                                        onChange={handleInputChange}
                                        placeholder="Phone Number"
                                        className="mt-1 block w-2/3 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                    />
                                </div>
                            ) : (
                                <p className="text-lg text-gray-800">
                                    {profile.countryCode} {profile.phoneNumber}
                                </p>
                            )}
                        </div>

                        <div>
                            <h3 className="text-sm font-medium text-gray-500">Location</h3>
                            {isEditing ? (
                                <input
                                    type="text"
                                    name="location"
                                    value={editableFields.location}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                            ) : (
                                <p className="text-lg text-gray-800">{profile.location}</p>
                            )}
                        </div>
                    </div>

                    {/* Spam Reports Section */}
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

                    {/* Verification Status */}
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