import React, { useState , useEffect } from "react";
import { useLocation, Link } from "react-router-dom";
import { Company } from "./SearchCompanies";
import { useAuth } from './UseAuth.ts';
interface Review {
  id?: string;
  companyName: string;
  reviewerEmail: string | null;
  review: string;
  rating: number;
  timestamp?: string;
}
const CompanyProfile: React.FC = () => {
  const location = useLocation();
  const company = location.state?.company as Company;
  const [reviews, setReviews] = useState<Review[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const { authenticatedFetch , user } = useAuth();
  const [newReview, setNewReview] = useState<Review>({
    companyName:company.name,
    reviewerEmail: user?.email || "",
    review:"",
    rating: 0
  });
  useEffect(() => {
    const fetchReviews = async () => {
      if (!company) return;

      try {
        const response = await authenticatedFetch(
          `http://localhost:8080/review_api/reviews/getReviewsByCompanyName/${encodeURIComponent(company.name)}`,"GET"
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

    fetchReviews();
  }, [company , authenticatedFetch]);

  const handleReviewChange = (event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
    const { name, value } = event.target;
    setNewReview({
      ...newReview,
      [name]: name === 'rating' ? Math.min(Math.max(parseFloat(value), 0), 5) : value,
    });
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
      setNewReview(null); // Clear the form after successful submission
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
    <div className="p-4">
      <h1 className="text-3xl font-bold mb-4">{company.name}</h1>
      <p className="text-gray-700 mb-2">{company.description}</p>
      <p className="text-gray-500 mb-2">Owner Email: {company.ownerEmail}</p>
      <p className="text-gray-500 mb-4">Rating: {company.rating}</p>

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

      <Link to="/" className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 mt-4 inline-block">
        Back to Search
      </Link>
    </div>
  );
};

export default CompanyProfile;