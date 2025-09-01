import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getPublisherInfo, getNewsByPublisher, deleteNews } from '../api/auth';
import { Button, Modal } from 'flowbite-react'; 
import { HiOutlineExclamationCircle } from 'react-icons/hi'; 
import NewsCard from './news/NewsCard';

const PublisherInfo = ({ user }) => {
  const { id } = useParams();
  const [publisher, setPublisher] = useState(null);
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [showModal, setShowModal] = useState(false);
  const [articleToDelete, setArticleToDelete] = useState(null);

  const fetchPublisherData = useCallback(async () => {
      if (!user) return;
      setLoading(true);
      setError(null);
      try {
          const infoPromise = getPublisherInfo(id);
          const articlesPromise = getNewsByPublisher(id);
          const [infoResponse, articlesResponse] = await Promise.all([infoPromise, articlesPromise]);
          setPublisher(infoResponse.data);
          setArticles(articlesResponse.data);
      } catch (err) {
          setError("Failed to fetch publisher data.");
          console.error(err);
      } finally {
          setLoading(false);
      }
  }, [id, user]);

  useEffect(() => {
    fetchPublisherData();
  }, [fetchPublisherData]);

  const handleDeleteClick = (article) => {
    setArticleToDelete(article);
    setShowModal(true);
  };

  const confirmDelete = async () => {
    if (!articleToDelete) return;
    try {
        await deleteNews(articleToDelete.id);
        setArticles(prevArticles => prevArticles.filter(article => article.id !== articleToDelete.id));
    } catch (err) {
        setError('Failed to delete the story. Please try again.'); // Set error state instead of alert
        console.error(err);
    } finally {
        setShowModal(false);
        setArticleToDelete(null);
    }
  };

  if (loading) return <div className="text-center p-10 pt-24">Loading publisher data...</div>;
  
  return (
    <>
      <div className="min-h-screen bg-gray-100 pt-24 font-inter">
          {publisher && (
               <div className="container mx-auto p-4 text-center">
                  <h1 className="text-4xl font-bold">{publisher.name}'s Dashboard</h1>
                  <p className="text-lg text-gray-600">Points: {publisher.points}</p>
               </div>
          )}

          <div className="container mx-auto p-4 text-center">
               <Button as={Link} to="/publisher/create-news">Create New Story</Button>
          </div>

          {error && <div className="text-center text-red-500 p-4">{error}</div>}

          <div className="container mx-auto p-4 sm:p-10">
              <h2 className="text-3xl font-bold text-center my-8">My Stories</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                  {articles.map((article) => (
                      <div key={article.id} className="relative">
                          <NewsCard article={article} type="local" />
                          {user && user.id === publisher?.account?.id && (
                               <Button color="failure" size="xs" onClick={() => handleDeleteClick(article)} className="absolute top-4 left-4 z-20">
                                  Delete
                              </Button>
                          )}
                      </div>
                  ))}
              </div>
          </div>
      </div>

      <Modal show={showModal} size="md" onClose={() => setShowModal(false)} popup>
        <Modal.Header />
        <Modal.Body>
          <div className="text-center">
            <HiOutlineExclamationCircle className="mx-auto mb-4 h-14 w-14 text-gray-400 dark:text-gray-200" />
            <h3 className="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
              Are you sure you want to delete this story titled "{articleToDelete?.title}"?
            </h3>
            <div className="flex justify-center gap-4">
              <Button color="failure" onClick={confirmDelete}>
                {"Yes, I'm sure"}
              </Button>
              <Button color="gray" onClick={() => setShowModal(false)}>
                No, cancel
              </Button>
            </div>
          </div>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default PublisherInfo;
