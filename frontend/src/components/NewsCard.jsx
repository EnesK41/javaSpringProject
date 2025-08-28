import React from 'react';
import { Link } from 'react-router-dom';
import { ExternalLink, Feather } from 'lucide-react';
import { Card, Button, Badge } from "flowbite-react";

const NewsCard = ({ article, type = 'api' }) => {
  
  const isLocalStory = type === 'local';
  const imageUrl = article.imageUrl || 'https://placehold.co/600x400/2d3748/ffffff?text=News';
  const sourceName = isLocalStory ? (article.publisher?.name || 'Community Author') : article.source;

  return (
    <Card
      // This is where the magic happens. We change the background color based on the type.
      className={`h-full ${isLocalStory ? 'bg-rose-50 dark:bg-gray-800' : 'bg-sky-50 dark:bg-gray-700'}`}
      imgAlt={article.title}
      renderImage={() => (
        <div className="relative">
          {/* A badge for local stories */}
          {isLocalStory && (
            <Badge color="failure" icon={Feather} className="absolute top-2 right-2 z-10">
              Community Story
            </Badge>
          )}
          <img 
            src={imageUrl} 
            alt={article.title} 
            className="rounded-t-lg"
            onError={(e) => { e.target.onerror = null; e.target.src='https://placehold.co/600x400/2d3748/ffffff?text=Image+Not+Found'; }}
          />
        </div>
      )}
    >
      <p className="text-sm text-gray-500 dark:text-gray-400 -mt-2 mb-1">
        {sourceName}
      </p>
      <h5 className="text-xl font-bold tracking-tight text-gray-900 dark:text-white">
        {article.title}
      </h5>
      <p className="font-normal text-gray-700 dark:text-gray-400 text-sm">
        {article.description || article.content}
      </p>
      
      {/* The button also changes based on the type */}
      {isLocalStory ? (
        <Button as={Link} to={`/story/${article.id}`} color="failure" className="mt-auto">
          Read Story
        </Button>
      ) : (
        <Button as="a" href={article.url} target="_blank" rel="noopener noreferrer" className="mt-auto">
          Read More
          <ExternalLink className="ml-2 h-4 w-4" />
        </Button>
      )}
    </Card>
  );
};

export default NewsCard;