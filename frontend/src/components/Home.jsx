import React from 'react';
import { Link } from 'react-router-dom';
import { Newspaper, ArrowRight } from 'lucide-react';

const Home = ({ user }) => {
  return (
    <div className="bg-gray-50 font-inter">
      {/* Hero Section */}
      <div className="relative bg-gradient-to-r from-gray-800 to-gray-900 text-white text-center py-20 md:py-32">
        <div className="container mx-auto px-4 z-10">
          <Newspaper className="w-24 h-24 mx-auto mb-6 text-blue-300" />
          <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight leading-tight mb-4">
            Welcome to NewsCentral
          </h1>
          <p className="text-lg md:text-xl text-gray-300 max-w-2xl mx-auto mb-8">
            Your one-stop destination for the latest articles from trusted publishers around the world.
          </p>
          <div className="flex justify-center items-center space-x-4">
            <Link
              to="/register"
              className="bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-8 rounded-full shadow-lg transition duration-300 ease-in-out transform hover:scale-105"
            >
              Get Started
            </Link>
            <Link
              to="#features" // This could link to a features section below
              className="flex items-center bg-white text-gray-800 font-bold py-3 px-8 rounded-full shadow-lg transition duration-300 ease-in-out transform hover:scale-105"
            >
              Learn More <ArrowRight className="w-5 h-5 ml-2" />
            </Link>
          </div>
        </div>
      </div>

      {/* Placeholder for future content, like a news feed */}
      <div id="features" className="container mx-auto p-10">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-12">
          Latest News
        </h2>
        <div className="text-center text-gray-500 p-8 border-2 border-dashed rounded-lg">
          <p>News articles will be displayed here soon.</p>
        </div>
      </div>
    </div>
  );
};

export default Home;