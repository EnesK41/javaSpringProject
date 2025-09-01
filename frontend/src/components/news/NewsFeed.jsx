import React, { useState, useEffect } from 'react';
import { getNews } from '../../api/auth';
import NewsCard from './NewsCard';
// FIX: Removed unused 'Dropdown', 'Checkbox', and 'Label' imports.
import { Button as FlowbiteButton } from 'flowbite-react';

const NewsFeed = () => {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(true); // Set initial loading to true
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const [query, setQuery] = useState('haber');
    const [country, setCountry] = useState('tr');
    const [searchTerm, setSearchTerm] = useState('haber'); // Default searchTerm to match query

    // FIX: Removed the unused isInitialLoad state.
    // The main useEffect now handles the initial load correctly.

    useEffect(() => {
        setLoading(true);
        setError(null);

        const fetchNews = async () => {
            try {
                const response = await getNews(query, country, page);
                const newArticles = response.data.content || [];
                
                // This logic correctly handles both initial load and new searches (page === 0)
                // and "Load More" (page > 0).
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
    }, [page, query, country]);

    const handleSearch = (e) => {
        e.preventDefault();
        // Only trigger a new search if the search term has actually changed
        if (query !== searchTerm) {
            setQuery(searchTerm);
            setPage(0);
        }
    };

    const handleCountryChange = (e) => {
        const newCountry = e.target.value;
        if (country !== newCountry) {
            setCountry(newCountry);
            setPage(0);
        }
    };

    if (error) {
        return <div className="text-center text-red-500 p-10">{error}</div>;
    }

    const countries = [
        { code: 'tr', name: 'Turkey' },
        { code: 'us', name: 'United States' },
        { code: 'gb', name: 'United Kingdom' },
        { code: 'de', name: 'Germany' },
        { code: 'fr', name: 'France' },
    ];

    return (
        <div className="container mx-auto p-4 sm:p-10">
            <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">
                Latest News
            </h2>
            
            <div className="flex flex-col md:flex-row gap-4 mb-8 p-4 bg-white rounded-lg shadow">
                <form onSubmit={handleSearch} className="flex-grow flex gap-2">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Search for news..."
                        className="w-full p-2 border border-gray-300 rounded-lg"
                    />
                    <FlowbiteButton type="submit">Search</FlowbiteButton>
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

            {/* This logic now correctly shows a main loading spinner only on the initial load */}
            {loading && page === 0 ? (
                <div className="text-center p-10">Loading news...</div>
            ) : (
                <>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                        {articles.map((article) => (
                            // Added a more unique key to prevent potential issues
                            <NewsCard key={`${article.id}-${article.url}`} article={article} />
                        ))}
                    </div>
                    
                    {loading && page > 0 && <div className="text-center p-10">Loading more articles...</div>}

                    {!loading && hasMore && (
                        <div className="text-center mt-12">
                            {/* FIX: Using the imported FlowbiteButton for consistency */}
                            <FlowbiteButton onClick={() => setPage(prevPage => prevPage + 1)}>
                                Load More
                            </FlowbiteButton>
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