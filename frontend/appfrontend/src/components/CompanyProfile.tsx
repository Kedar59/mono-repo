import React, { useState, useEffect } from "react";
import { useLocation, Link, useNavigate } from "react-router-dom";
import { Company } from "./SearchCompanies";
import { useAuth } from './UseAuth.ts';
import getResponseData from "../util.tsx";
import { ErrorResponse } from "../App.tsx";
export interface Review {
  id?: string;
  companyName: string;
  reviewerEmail: string | null;
  review: string;
  rating: number;
  timestamp?: string;
};
export enum Role {
  NORMAL = 'NORMAL',
  ADMIN = "ADMIN",
  MODERATOR = "MODERATOR",
  NO_ROLE_ASSIGNED = "NO_ROLE_ASSIGNED"
};
interface AuthorizationResponse {
  role: Role;
};
interface AuthorizationRequest {
  profileId?: string;
  companyId: string;
};

export interface CompanyBot {
  id?: string;
  companyName: string;
  botToken: string;
  botUsername: string;
  botUrl: string;
}
const CompanyProfile: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const company = location.state?.company as Company;
  const [reviews, setReviews] = useState<Review[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const { authenticatedFetch, user } = useAuth();
  const [newReview, setNewReview] = useState<Review>({
    companyName: company.name,
    reviewerEmail: user?.email || "",
    review: "",
    rating: 0
  });
  const [role, setRole] = useState<Role>(Role.NO_ROLE_ASSIGNED);
  const [formData, setFormData] = useState({
    companyName: company.name,
    botToken: '',
    botUsername: '',
    botUrl: ''
  });
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [description, setDescription] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [companyStats, setCompanyStats] = useState({
    rating: company.rating,
    numberOfReviews: company.numberOfReviews
  });

  const fetchReviews = async () => {
    if (!company) return null;

    try {
      const response = await authenticatedFetch(
        `http://localhost:8080/review_api/reviews/getReviewsByCompanyName/${encodeURIComponent(company.name)}`,
        "GET"
      );
      if (!response.ok) {
        throw new Error("Failed to fetch reviews");
      }

      const data: Review[] = await response.json();
      setReviews(data);
      return data;
    } catch (err) {
      setError((err as Error).message);
      setReviews(null);
      return null;
    }
  };

  useEffect(() => {
    const getRole = async () => {
      try {
        const authorizationRequest: AuthorizationRequest = {
          profileId: user?.id,
          companyId: company.id
        }
        const response = await authenticatedFetch(
          `http://localhost:8080/gateway/roles/findRole`,
          "POST",
          authorizationRequest
        );
        if (!response.ok) {
          throw new Error("Failed to fetch reviews");
        }

        const data: AuthorizationResponse = await response.json();
        if (data["role"] === "ADMIN") {
          setRole(Role.ADMIN);
        } else if (data["role"] === "MODERATOR") {
          setRole(Role.MODERATOR);
        } else if (data["role"] === "NORMAL") {
          setRole(Role.NORMAL);
        } else {
          setRole(Role.NO_ROLE_ASSIGNED);
        }
      } catch (err) {
        setError((err as Error).message);
        setReviews(null);
      }
    }
    getRole();
    fetchReviews(); // Initial fetch of reviews
  }, [company, user, authenticatedFetch, role]);

  const handleReviewChange = (event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
    const { name, value } = event.target;
    setNewReview({
      ...newReview,
      [name]: name === 'rating' ? Math.min(Math.max(parseFloat(value), 0), 5) : value,
    });
  };
  const handleDeleteCompany = async () => {
    // Here you can make an API call to register the bot.
    // For now, let's just log the companyBot object to the console.
    try {
      const response = await authenticatedFetch(
        `http://localhost:8080/review_api/company/profile/${user?.id}/company/${company.id}/delete`,
        "DELETE",
      );

      if (!response.ok) {
        throw new Error("Failed to register review");
      }
      const data = await getResponseData(response);
      if (data.deleted) {
        alert("Company deleted");
        navigate("/companies")
      } else {
        alert("Error deleting company");
      }
    } catch (err) {
      setError((err as Error).message);
    }
  };
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const response = await authenticatedFetch(
        `http://localhost:8080/review_api/companyBot/profile/${user?.id}/company/${company.id}/addCompanyBot`,
        "POST",
        formData
      );
      const data = await response.json();
      console.log(data);
      console.log("response status : " + response.status);
      if (response.status === 200) {
        setSuccess('Bot registered successfully!');
        setFormData(data);
      } else if (response.status === 201 || response.status === 202) {

        setError(`Error: ${data.message}`);
      } else {
        setError('An unexpected error occurred');
      }
    } catch (err) {
      setError('Failed to register bot. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  const handleRegisterReview = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);
    setSubmitSuccess('');

    if (!company || !user || !newReview) {
        setError('Missing required information');
        return;
    }

    try {
        const response = await authenticatedFetch(
            "http://localhost:8080/review_api/reviews/registerReview",
            "POST",
            newReview
        );

        if (!response.ok) {
            throw new Error("Failed to register review");
        }

        // Refresh reviews first
        await fetchReviews();

        // Calculate new stats locally while waiting for backend
        const newNumberOfReviews = companyStats.numberOfReviews + 1;
        const newRating = ((companyStats.rating * companyStats.numberOfReviews) + newReview.rating) / newNumberOfReviews;
        
        // Update stats immediately with calculated values
        setCompanyStats({
            rating: newRating,
            numberOfReviews: newNumberOfReviews
        });

        // Reset form
        setSubmitSuccess('Review submitted successfully!');
        setNewReview({
            companyName: company.name,
            reviewerEmail: user?.email || "",
            review: "",
            rating: 0
        });

        // Then fetch the actual stats from backend
        const statsResponse = await authenticatedFetch(
            `http://localhost:8080/review_api/reviews/getCompanyStats/${encodeURIComponent(company.name)}`,
            "GET"
        );

        if (statsResponse.ok) {
            const statsData = await statsResponse.json();
            setCompanyStats({
                rating: statsData.rating,
                numberOfReviews: statsData.numberOfReviews
            });
        }
    } catch (err) {
        setError((err as Error).message);
    }
  };

  const handleManageCompanyMembers = (company: Company) => {
    try {
        navigate("/manageCompanyMembers", { state: { company } });
    } catch (err) {
        setError('Failed to navigate to member management');
    }
  };
  const handleSave = async () => {
    setIsSaving(true);
    try {
      console.log("New description : " + description);
      const response = await authenticatedFetch(
        `http://localhost:8080/review_api/company/profile/${user?.id}/company/${company.id}/update`,
        "POST",
        {
          id: company.id,
          name: company.name,
          description: description
        }
      );
      if (!response.ok) {
        throw new Error('Failed to fetch profile');
      }
      if (response.status === 200) {
        navigate("/companies");
      } else if (response.status === 201 || response.status === 202) {
        const error: ErrorResponse = await getResponseData(response);
        setError(`Error: ${error.message}`);
      } else {
        setError('An unexpected error occurred');
      }
      
      setIsEditing(false);
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred');
    } finally {
      setIsSaving(false);
    }
  };
  const handleDescriptionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setDescription(value);
  };
  if (!company) {
    return (
      <div className="p-4">
        <h1 className="text-2xl font-bold">Company Profile</h1>
        <p className="text-red-500">No company data available.</p>
        <Link to="/" className="text-blue-500 hover:underline">Go back to search</Link>
      </div>
    );
  }

  const renderAdminControls = () => (
    <div className="bg-white rounded-lg shadow-md p-6 mb-6">
      <h2 className="text-xl font-bold mb-4">Administrative Controls</h2>
      <div className="space-y-4">
        {/* Description Editor */}
        <div className="mb-4">
          <button 
            onClick={() => {
                if (isEditing) {
                    handleSave();
                } else {
                    setDescription(company.description || '');
                    setIsEditing(true);
                }
            }}
            className="inline-flex items-center justify-center px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            {isEditing ? 'Save Description' : 'Edit Description'}
          </button>
          {isEditing && (
            <input
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="mt-2 w-full p-2 border rounded-lg"
              placeholder="Enter company description"
            />
          )}
        </div>

        {/* Company Management Buttons */}
        <div className="flex flex-wrap gap-4">
          <button 
            onClick={() => handleManageCompanyMembers(company)}
            className="inline-flex items-center justify-center px-6 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors"
          >
            Manage Members
          </button>
          <button 
            onClick={handleDeleteCompany}
            className="inline-flex items-center justify-center px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
          >
            Delete Company
          </button>
        </div>

        {/* Bot Registration */}
        <div className="mt-6">
          <h3 className="text-lg font-semibold mb-3">Telegram Bot Registration</h3>
          <form onSubmit={handleSubmit} className="space-y-3">
            <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Bot Token</label>
                <input
                    type="text"
                    name="botToken"
                    value={formData.botToken}
                    onChange={handleChange}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>
            <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Bot Username</label>
                <input
                    type="text"
                    name="botUsername"
                    value={formData.botUsername}
                    onChange={handleChange}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>
            <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Bot URL</label>
                <input
                    type="url"
                    name="botUrl"
                    value={formData.botUrl}
                    onChange={handleChange}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>
            <button
                type="submit"
                disabled={loading}
                className="w-full inline-flex items-center justify-center px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 disabled:bg-green-300 transition-colors"
            >
                {loading ? 'Registering...' : 'Register Bot'}
            </button>
            {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
            {success && <p className="text-green-500 text-sm mt-2">{success}</p>}
          </form>
        </div>
      </div>
    </div>
  );

  const renderModeratorControls = () => (
    <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 className="text-xl font-bold mb-4">Moderator Controls</h2>
        <div className="space-y-4">
            <div className="mb-4">
                {isEditing ? (
                    <div className="space-y-4">
                        <input
                            type="text"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            className="w-full p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="Enter company description"
                        />
                        <div className="flex gap-2">
                            <button 
                                onClick={handleSave}
                                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                            >
                                Save Description
                            </button>
                            <button 
                                onClick={() => setIsEditing(false)}
                                className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                ) : (
                    <button 
                        onClick={() => {
                            setDescription(company.description || '');
                            setIsEditing(true);
                        }}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                    >
                        Edit Description
                    </button>
                )}
            </div>
            {error && (
                <div className="text-red-500 text-sm mt-2">{error}</div>
            )}
            {success && (
                <div className="text-green-500 text-sm mt-2">{success}</div>
            )}
        </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        {/* Company Header */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-3xl font-bold mb-2">{company.name}</h1>
              <p className="text-gray-600">Owner: {company.ownerEmail}</p>
              <p className="text-gray-600">Your Role: {role}</p>
            </div>
            <div className="flex items-center space-x-2">
              <span className="text-2xl text-yellow-400">★</span>
              <span className="text-xl font-bold">{companyStats.rating.toFixed(1)}</span>
              <span className="text-gray-500">({companyStats.numberOfReviews} reviews)</span>
            </div>
          </div>
          <div className="mt-4">
            <h2 className="text-xl font-semibold mb-2">Description</h2>
            <p className="text-gray-700">{company.description || 'No description available.'}</p>
          </div>
        </div>

        {/* Role-based Controls */}
        {role === Role.ADMIN && renderAdminControls()}
        {role === Role.MODERATOR && renderModeratorControls()}

        {/* Review Section */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-xl font-bold mb-4">Reviews</h2>
          
          {/* Review Form - Only show for verified users */}
          {user?.verified ? (
            <form onSubmit={handleRegisterReview} className="mb-6">
              <textarea
                name="review"
                className="w-full p-3 border rounded-lg mb-3"
                placeholder="Write your review..."
                value={newReview.review}
                onChange={handleReviewChange}
                rows={4}
              />
              <div className="flex gap-4">
                <input
                  name="rating"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  className="w-24 p-2 border rounded-lg"
                  placeholder="Rating"
                  value={newReview.rating}
                  onChange={handleReviewChange}
                />
                <button
                  type="submit"
                  className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600"
                >
                  Submit Review
                </button>
              </div>
            </form>
          ) : (
            <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-6">
              <p className="text-yellow-700">
                Please verify your account on Telegram bot{' '}
                <a href="https://t.me/MeheryOtpbot" className="underline">@MeheryOtpbot</a>
                {' '}to submit reviews.
              </p>
            </div>
          )}

          {/* Reviews List */}
          <div className="space-y-4">
            {reviews?.map((review) => (
              <div key={review.id} className="border-b pb-4">
                <div className="flex justify-between items-start mb-2">
                  <div>
                    <p className="font-medium">{review.reviewerEmail || "Anonymous"}</p>
                    <div className="flex items-center">
                      <span className="text-yellow-400 mr-1">★</span>
                      <span>{review.rating}</span>
                    </div>
                  </div>
                  <span className="text-sm text-gray-500">
                    {new Date(review.timestamp!).toLocaleDateString()}
                  </span>
                </div>
                <p className="text-gray-700">{review.review}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Back Button */}
        <button
          onClick={() => navigate('/companies')}
          className="text-blue-500 hover:text-blue-600 font-medium flex items-center"
        >
          <span>← Back to Companies</span>
        </button>
      </div>
    </div>
  );
};

export default CompanyProfile;