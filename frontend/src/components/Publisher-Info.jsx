import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { BookOpen, User, DollarSign } from 'lucide-react';

const PublisherInfo = ({ user, getPublisherInfo }) => {
  const { id } = useParams();
  const [publisher, setPublisher] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log("[Publisher-Info Effect] Effect is running. User is:", user);

    const fetchPublisherDetails = async () => {
      if (!user) {
        console.log("[Publisher-Info Effect] User is null, skipping API call.");

        return;
      }

      try {
        setLoading(true);
        setError(null);
        const response = await getPublisherInfo(id);
        console.log("[Publisher-Info Effect] User exists, attempting to fetch data...");

        setPublisher(response.data);
      } catch (err) {
        console.error("Failed to fetch publisher details:", err);
        if (err.response && err.response.status === 404) {
          setError(`Publisher with ID ${id} not found.`);
        } else if (err.response && err.response.status === 403) {
          setError("You don't have permission to view this publisher's details.");
        } else {
          setError("An unexpected error occurred while fetching publisher details.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchPublisherDetails();
  }, [id, user, getPublisherInfo]);

  return (
    <div className="min-h-screen bg-gray-100 font-inter antialiased">
      <div className="container mx-auto p-6 md:p-10">
        <div className="bg-white shadow-xl rounded-2xl overflow-hidden max-w-2xl mx-auto mt-12 p-8 border border-gray-200">
          <h1 className="text-4xl font-extrabold text-gray-900 mb-6 text-center border-b pb-4">
            Publisher Profile
          </h1>

          {loading && (
            <div className="flex items-center justify-center p-8">
              <div className="animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"></div>
              <p className="ml-4 text-gray-700 text-lg">Loading publisher data...</p>
            </div>
          )}

          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-6 py-4 rounded-lg relative text-center text-lg">
              <strong className="font-bold">Error!</strong>
              <span className="block sm:inline ml-2">{error}</span>
            </div>
          )}

          {!loading && !error && publisher && (
            <div className="space-y-6">
              <div className="flex items-center space-x-4 bg-gray-50 p-4 rounded-lg shadow-sm">
                <User className="w-8 h-8 text-indigo-600" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Name</p>
                  <p className="text-xl font-semibold text-gray-800">{publisher.name}</p>
                </div>
              </div>

              <div className="flex items-center space-x-4 bg-gray-50 p-4 rounded-lg shadow-sm">
                <DollarSign className="w-8 h-8 text-green-600" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Points</p>
                  <p className="text-xl font-semibold text-gray-800">{publisher.points}</p>
                </div>
              </div>

              <div className="flex items-center space-x-4 bg-gray-50 p-4 rounded-lg shadow-sm">
                <BookOpen className="w-8 h-8 text-purple-600" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Articles Published</p>
                  <p className="text-xl font-semibold text-gray-800">{publisher.newsCount}</p>
                </div>
              </div>

              <div className="mt-10 pt-6 border-t flex justify-center">
                <Link
                  to={`/publisher/${publisher.id}/news`}
                  className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-full shadow-lg transition duration-300 ease-in-out transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75"
                >
                  View All News by {publisher.name}
                </Link>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PublisherInfo;