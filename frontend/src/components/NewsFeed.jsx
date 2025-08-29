import React, { useState, useEffect } from 'react';
import { getNews } from '../api/auth';
import NewsCard from './NewsCard';

const NewsFeed = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // State for pagination
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  // --- NEW: State for filters ---
  const [query, setQuery] = useState('latest headlines');
  const [country, setCountry] = useState('us');
  // A temporary state to hold the input value before searching
  const [searchTerm, setSearchTerm] = useState('latest headlines');

  useEffect(() => {
    const fetchNews = async () => {
      setLoading(true);
      try {
        // Pass the current filter and page state to the API call
        const response = await getNews(query, country, page);
        const newArticles = response.data.content || [];
        
        // If it's the first page, replace the articles. Otherwise, add to them.
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
    // This effect now re-runs whenever the page, query, or country changes.
  }, [page, query, country]);

  // Handler for the search form submission
  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0); // Reset to the first page for a new search
    setArticles([]); // Clear old articles
    setQuery(searchTerm);
  };

  // Handler for country change
  const handleCountryChange = (e) => {
    setPage(0); // Reset to the first page
    setArticles([]); // Clear old articles
    setCountry(e.target.value);
  };

  if (error) {
    return <div className="text-center text-red-500 p-10">{error}</div>;
  }

  // A list of countries for the dropdown menu
  const countries = [
    { code: 'us', name: 'United States' },
    { code: 'tr', name: 'Turkey' },
    { code: 'gb', name: 'United Kingdom' },
    { code: 'de', name: 'Germany' },
    { code: 'fr', name: 'France' },
    // Add more countries as you wish
  ];

  return (
    <div className="container mx-auto p-4 sm:p-10">
      <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">
        Latest News
      </h2>
      
      {/* --- NEW: Filter Controls --- */}
      <div className="flex flex-col md:flex-row gap-4 mb-8 p-4 bg-white rounded-lg shadow">
        <form onSubmit={handleSearch} className="flex-grow">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search for news..."
            className="w-full p-2 border border-gray-300 rounded-lg"
          />
        </form>
        <select 
          value={country} 
          onChange={handleCountryChange}
          className="p-2 border border-gray-300 rounded-lg"
        >
          {countries.map(c => (
            <option key={c.code} value={c.code}>{c.name}</option>
          ))}
        </select>
      </div>
      {/* --------------------------- */}

      {loading && page === 0 ? (
         <div className="text-center p-10">Loading news...</div>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {articles.map((article) => (
              <NewsCard key={article.id} article={article} />
            ))}
          </div>
          
          {loading && page > 0 && <div className="text-center p-10">Loading more articles...</div>}

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

          {!loading && !hasMore && articles.length > 0 && (
            <div className="text-center mt-12 text-gray-500">
              You've reached the end of the news.
            </div>
          )}

          {!loading && articles.length === 0 && (
             <div className="text-center mt-12 text-gray-500">
              No articles found for your search.
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default NewsFeed;