import React, { useState, useEffect } from 'react';
import { getNews } from '../api/auth';
import NewsCard from './NewsCard';

const NewsFeed = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    const fetchNews = async () => {
      setLoading(true);
      try {
        const response = await getNews(page);
        const newArticles = response.data.content || [];
        setArticles(prev => page === 0 ? newArticles : [...prev, ...newArticles]);
        setHasMore(!response.data.last);
      } catch (err) {
        setError("Failed to fetch news. Please try again later.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchNews();
  }, [page]);

  if (error) {
    return <div className="text-center text-red-500 p-10">{error}</div>;
  }

  return (
    <div className="container mx-auto p-4 sm:p-10">
      <h2 className="text-3xl font-bold text-center text-gray-800 mb-12">
        Latest News
      </h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {articles.map((article) => (
          // We render the API news, which will get the default 'api' type and bluish background.
          <NewsCard key={article.id} article={article} />
        ))}
      </div>
      
      {loading && <div className="text-center p-10">Loading more articles...</div>}

      {!loading && hasMore && (
        <div className="text-center mt-12">
          <button
            onClick={() => setPage(prevPage => prevPage + 1)}
            className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 px-8 rounded-full shadow-lg transition duration-300"
          >
            Load More
          </button>
        </div>
      )}

      {!hasMore && (
        <div className="text-center mt-12 text-gray-500">
          You've reached the end of the news.
        </div>
      )}
    </div>
  );
};

export default NewsFeed;