import React, { useState, useEffect } from "react";
import { useLocation, Link, useNavigate } from "react-router-dom";
import { Company } from "./SearchCompanies";
import { useAuth } from './UseAuth.ts';
import getResponseData from "../util.tsx";
export interface Review {
  id?: string;
  companyName: string;
  reviewerEmail: string | null;
  review: string;
  rating: number;
  timestamp?: string;
}
enum Role {
  NORMAL = 'NORMAL',
  ADMIN = "ADMIN",
  MODERATOR = "MODERATOR",
  NO_ROLE_ASSIGNED = "NO_ROLE_ASSIGNED"
};
interface AuthorizationResponse {
  role: Role;
}
interface AuthorizationRequest {
  profileId?: string;
  companyId: string;
}

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
  useEffect(() => {
    const fetchReviews = async () => {
      if (!company) return;

      try {
        const response = await authenticatedFetch(
          `http://localhost:8080/review_api/reviews/getReviewsByCompanyName/${encodeURIComponent(company.name)}`, "GET"
        );
        if (!response.ok) {
          throw new Error("Failed to fetch reviews");
        }

        const data: Review[] = await response.json();
        setReviews(data);
      } catch (err) {
        setError((err as Error).message);
        setReviews(null);
      }
    };
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
        console.log("response : " + data["role"]);
        if (data["role"] === "ADMIN") {
          setRole(Role.ADMIN);
        } else if (data["role"] === "MODERATOR") {
          setRole(Role.MODERATOR);
        } else if (data["role"] === "NORMAL") {
          setRole(Role.NORMAL);
        } else {
          setRole(Role.NO_ROLE_ASSIGNED);
        }
        console.log("official role : " + role);
      } catch (err) {
        setError((err as Error).message);
        setReviews(null);
      }
    }
    getRole();
    fetchReviews();
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
      console.log("response status : "+response.status);
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

    if (!company || !user || !newReview) {
      return; // Handle missing data gracefully (e.g., display an error message)
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

      // Handle successful review registration (e.g., clear the form, update UI)
      console.log("Review registered successfully!");
      setNewReview({
        companyName: company.name,
        reviewerEmail: user?.email || "",
        review: "",
        rating: 0
      }); // Clear the form after successful submission
    } catch (err) {
      setError((err as Error).message);
    }
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

  return (
    <div className="container mx-auto p-4">
      <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
        <h2 className="text-2xl font-bold mb-4">{company.name}</h2>
        <p className="text-gray-600 mb-2">Current user role: {role}</p>
        <p className="text-gray-600 mb-2">Owner Email: {company.ownerEmail}</p>
        <p className="text-gray-600 mb-2">Description: {company.description}</p>
        <div className="flex items-center mb-2">
          <span className="text-gray-600 mr-2">Rating:</span>
          <span className="text-yellow-500">{company.rating.toFixed(1)}</span>
          <span className="text-gray-600 ml-2">({company.numberOfReviews} reviews)</span>
        </div>
      </div>

      <div className="flex flex-col items-center space-y-4">
        {/* Conditionally render the deleteCompany button based on role */}
        {role === Role.ADMIN && (
          <>
            <button onClick={handleDeleteCompany} className="bg-red-500 text-white px-6 py-3 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400">
              Delete Company
            </button>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Bot Token</label>
                <input
                  type="text"
                  name="botToken"
                  value={formData.botToken}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">Bot Username</label>
                <input
                  type="text"
                  name="botUsername"
                  value={formData.botUsername}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">Bot URL</label>
                <input
                  type="url"
                  name="botUrl"
                  value={formData.botUrl}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              {error && (
                <div className="text-red-500 text-sm">{error}</div>
              )}

              {success && (
                <div className="text-green-500 text-sm">{success}</div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 disabled:bg-blue-300 disabled:cursor-not-allowed"
              >
                {loading ? 'Registering...' : 'Register Bot'}
              </button>
            </form>
          </>
        )}
      </div>

      {user?.verified ? (
        <div className="mt-6">
          <h2 className="text-xl font-bold mb-4">Register a Review</h2>
          <form className="space-y-4" onSubmit={handleRegisterReview}>
            <textarea
              className="w-full border border-gray-300 rounded-lg p-2"
              placeholder="Write your review here..."
              name="review"
              value={newReview?.review || ""} // Set initial value from newReview
              onChange={handleReviewChange}
            />
            <input
              type="number"
              min="0"
              max="5"
              step="0.1"
              className="w-full border border-gray-300 rounded-lg p-2"
              placeholder="Rating (0-5)"
              name="rating"
              value={newReview?.rating || 0} // Set initial value from newReview
              onChange={handleReviewChange}
            />
            <button
              type="submit"
              className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
            >
              Submit Review
            </button>
          </form>
        </div>
      ) : (
        <p className="text-red-500 mt-6">
          Verify your phone number for account {user?.email} on Telegram bot <a href="https://t.me/MeheryOtpbot" className="text-blue-500 underline">https://t.me/MeheryOtpbot</a>.
        </p>
      )}

      <div className="mt-6">
        <h2 className="text-2xl font-bold mb-4">Reviews</h2>
        {error && <p className="text-red-500">Error: {error}</p>}
        {reviews ? (
          reviews.length > 0 ? (
            <ul className="space-y-4">
              {reviews.map((review) => (
                <li key={review.id} className="border border-gray-300 rounded-lg p-4 shadow-sm">
                  <p className="text-sm text-gray-600">{review.review}</p>
                  <p className="text-sm text-gray-500">Reviewer Email: {review.reviewerEmail || "Anonymous"}</p>
                  <p className="text-sm text-gray-500">Rating: {review.rating}</p>
                  <p className="text-sm text-gray-400">Timestamp: {new Date(review.timestamp).toLocaleString()}</p>
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-700">No reviews available for this company.</p>
          )
        ) : (
          <p className="text-gray-700">Loading reviews...</p>
        )}
      </div>

      <Link to="/companies" className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 mt-4 inline-block">
        Back to Search
      </Link>
    </div>
  );
};

export default CompanyProfile;