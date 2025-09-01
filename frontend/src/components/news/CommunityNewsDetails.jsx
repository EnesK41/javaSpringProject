import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getLocalNewsById, recordLocalNewsView } from '../../api/auth';
import { Clock, User, Award } from 'lucide-react'; // Clock is now used

const CommunityNewsDetails = ({ user }) => {
  const { id } = useParams();
  const [story, setStory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStory = async () => {
      setLoading(true);
      try {
        const response = await getLocalNewsById(id);
        setStory(response.data);
      } catch (err) {
        setError("Failed to load the story. It may have been removed or the link is incorrect.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchStory();
  }, [id]);

  useEffect(() => {
    const recordView = async () => {
      if (story && user) {
        try {
          await recordLocalNewsView(story.id);
          console.log(`View recorded for story ${story.id} by user ${user.id}`);
        } catch (err) {
          console.error("Failed to record news view:", err);
        }
      }
    };
    recordView();
  }, [story, user]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen bg-gray-100 pt-20">
        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center h-screen bg-gray-100 pt-20">
        <div className="text-center text-red-500 p-10">
          <h2 className="text-2xl font-bold mb-4">Error</h2>
          <p>{error}</p>
          <Link to="/" className="text-blue-500 hover:underline mt-4 inline-block">Go back to Home</Link>
        </div>
      </div>
    );
  }

  if (!story) {
    return null;
  }

  // Helper to format the date nicely
  const formattedDate = story.publishedAt 
    ? new Date(story.publishedAt).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' }) 
    : 'Date not available';

  return (
    <div className="bg-gray-100 pt-24 pb-12 font-inter">
      <div className="container mx-auto max-w-4xl bg-white rounded-lg shadow-xl p-8 md:p-12">
        
        <div className="border-b pb-6 mb-6">
          <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900 mb-4">{story.title}</h1>
          <div className="flex flex-wrap items-center text-gray-500 text-sm gap-x-6 gap-y-2">
            <div className="flex items-center">
              <User className="w-4 h-4 mr-2" />
              <span>By {story.publisherName || 'Unknown Author'}</span>
            </div>
            <div className="flex items-center">
              <Award className="w-4 h-4 mr-2" />
              <span>{story.views} Views</span>
            </div>
            {/* --- THIS IS THE FIX --- */}
            {/* We now display the publish date using the Clock icon */}
            <div className="flex items-center">
              <Clock className="w-4 h-4 mr-2" />
              <span>Published on {formattedDate}</span>
            </div>
          </div>
        </div>

        <div className="prose prose-lg max-w-none text-gray-800">
          <p className="whitespace-pre-wrap">{story.content}</p>
        </div>

        <div className="mt-12 border-t pt-8">
            <h3 className="text-2xl font-bold text-gray-800">Discussion</h3>
            <p className="text-gray-500 mt-2">Comments and reactions are coming soon.</p>
        </div>

      </div>
    </div>
  );
};

export default CommunityNewsDetails;