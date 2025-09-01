import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getApiNewsById, recordApiNewsView } from '../../api/auth';
import { ExternalLink } from 'lucide-react';

const ApiNewsDetails = ({ user }) => {
  const { id } = useParams();
  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchArticle = async () => {
      setLoading(true);
      try {
        const response = await getApiNewsById(id);
        setArticle(response.data);
      } catch (err) {
        setError("Failed to load the article. It may have been removed or the link is incorrect.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchArticle();
  }, [id]);

  useEffect(() => {
    const recordView = async () => {
      if (article && user) {
        try {
          await recordApiNewsView(article.id);
          console.log(`View recorded for API article ${article.id}`);
        } catch (err) {
          console.error("Failed to record API news view:", err);
        }
      }
    };

    recordView();
  }, [article, user]);

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

  if (!article) {
    return null;
  }

  return (
    <div className="bg-gray-100 pt-24 pb-12 font-inter">
      <div className="container mx-auto max-w-4xl bg-white rounded-lg shadow-xl p-8 md:p-12">
        
        <div className="border-b pb-6 mb-6">
          <a href={article.url} target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:underline font-semibold text-lg inline-block mb-4">
            Read Full Story at {article.source} <ExternalLink className="inline-block w-5 h-5 ml-1" />
          </a>
          <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900">{article.title}</h1>
        </div>

        <div className="prose prose-lg max-w-none text-gray-800">
          {article.imageUrl && (
            <img src={article.imageUrl} alt={article.title} className="rounded-lg mb-8" />
          )}
          <p>{article.description}</p>
        </div>

        <div className="mt-12 border-t pt-8">
            <h3 className="text-2xl font-bold text-gray-800">Discussion</h3>
            <p className="text-gray-500 mt-2">Comments and reactions for API news are coming soon.</p>
        </div>

      </div>
    </div>
  );
};

export default ApiNewsDetails;