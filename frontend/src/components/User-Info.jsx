import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { User, Award } from 'lucide-react';

const UserInfo = ({ user, getUserInfo }) => {
  const { id } = useParams();
  
  const [userInfo, setUserInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserDetails = async () => {
      
      if (!user) {
        return;
      }

      setLoading(true);
      try {
        
        const response = await getUserInfo(id);
        
        setUserInfo(response.data);
      } catch (err) {
        console.error("Failed to fetch user details:", err);
        if (err.response && err.response.status === 403) {
          setError("You do not have permission to view this page.");
        } else if (err.response && err.response.status === 404) {
          setError(`User with ID ${id} not found.`);
        } else {
          setError("An unexpected error occurred while fetching details.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserDetails();
  }, [id, user, getUserInfo]); 

  if (loading && !userInfo) { 
    return (
      <div className="flex items-center justify-center p-8 mt-12 pt-20">
        <div className="animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"></div>
        <p className="ml-4 text-gray-700 text-lg">Loading user data...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 font-inter antialiased pt-20">
      <div className="container mx-auto p-6 md:p-10">
        <div className="bg-white shadow-xl rounded-2xl max-w-md mx-auto mt-12 p-8 border border-gray-200">
          <h1 className="text-3xl font-extrabold text-gray-900 mb-6 text-center border-b pb-4">
            User Profile
          </h1>

          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-6 py-4 rounded-lg text-center text-lg">
              <strong className="font-bold">Error!</strong>
              <span className="block sm:inline ml-2">{error}</span>
            </div>
          )}

          {!error && userInfo && (
            <div className="space-y-5">
              
              <div className="flex items-center space-x-4 bg-gray-50 p-4 rounded-lg shadow-sm">
                <User className="w-8 h-8 text-indigo-600" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Name</p>
                  <p className="text-xl font-semibold text-gray-800">{userInfo.name}</p>
                </div>
              </div>

              <div className="flex items-center space-x-4 bg-gray-50 p-4 rounded-lg shadow-sm">
                <Award className="w-8 h-8 text-yellow-500" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Points</p>
                  <p className="text-xl font-semibold text-gray-800">{userInfo.point}</p>
                </div>
              </div>
              <div className="mt-10 pt-6 border-t flex justify-center">
                <Link
                  to={`/user/${user.id}/news`}
                  className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-full shadow-lg transition duration-300 ease-in-out transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75"
                >
                  View Bookmarked News by {userInfo.name}
                </Link>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserInfo;
