import React, { useState, useEffect } from 'react';
import { getLocalNews } from '../../api/auth';
import NewsCard from './NewsCard'; 

const CommunityNews = () => {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchNews = async () => {
            try {
                setLoading(true);
                const response = await getLocalNews();
                setArticles(response.data.content || []);
            } catch (err) {
                setError("Failed to fetch community news.");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchNews();
    }, []);

    if (loading) return <div className="text-center p-10 pt-24">Loading community stories...</div>;
    if (error) return <div className="text-center text-red-500 p-10 pt-24">{error}</div>;

    return (
        <div className="bg-gray-100 min-h-screen pt-24 font-inter">
            <div className="container mx-auto p-4 sm:p-10">
                <h1 className="text-4xl font-bold text-center my-8">Community News</h1>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                    {articles.map((article) => (
                        <NewsCard key={article.id} article={article} type="local" />
                    ))}
                </div>
            </div>
        </div>
    );
};

export default CommunityNews;