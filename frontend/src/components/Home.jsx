import React from 'react';
import { Link } from 'react-router-dom';
import NewsFeed from './news/NewsFeed';
import logo from './assets/logo.png'; 

const Home = ({ user }) => {
  return (
    <div className="bg-gray-100 font-inter pt-20">
      <div className="relative bg-gradient-to-r from-gray-800 to-gray-900 text-white text-center py-20 md:py-32">
        <div className="container mx-auto px-4 z-10">

          {/* 2. Add the logo image here */}
          <img 
            src={logo} 
            alt="SecretNews Logo" 
            className="w-[250px] h-[250px] mx-auto mb-6" // Centers the logo and adds space below it
          />

          <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight leading-tight mb-4">
            Welcome to SecretNews
          </h1>
          <p className="text-lg md:text-xl text-gray-300 max-w-2xl mx-auto mb-8">
            What if the headlines are only half the story? Your destination for the articles you were never meant to find.
          </p>
          {!user && (
            <div className="flex justify-center items-center space-x-4">
              <Link
                to="/register"
                className="bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-8 rounded-full shadow-lg transition duration-300 ease-in-out transform hover:scale-105"
              >
                Get Started
              </Link>
            </div>
          )}
        </div>
      </div>

      <NewsFeed />
      
    </div>
  );
};

export default Home;
