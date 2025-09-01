import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createNews } from '../../api/auth';
import { Button, Label, TextInput, Textarea } from 'flowbite-react';

const CreateNews = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [category, setCategory] = useState('');
  const [country, setCountry] = useState('');
  const [city, setCity] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await createNews({ title, content, category, country, city });
      navigate('/'); 
    } catch (err) {
      setError('Failed to publish story. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gray-100 min-h-screen pt-24 font-inter">
      <div className="container mx-auto p-4 max-w-2xl">
        <form onSubmit={handleSubmit} className="bg-white p-8 rounded-lg shadow-md space-y-6">
          <h1 className="text-3xl font-bold text-center mb-6">Create a New Story</h1>
          <div>
            <Label htmlFor="title" value="Title" />
            <TextInput id="title" value={title} onChange={(e) => setTitle(e.target.value)} required />
          </div>
          <div>
            <Label htmlFor="content" value="Content" />
            <Textarea id="content" value={content} onChange={(e) => setContent(e.target.value)} required rows={10} />
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <Label htmlFor="category" value="Category (optional)" />
              <TextInput id="category" value={category} onChange={(e) => setCategory(e.target.value)} />
            </div>
            <div>
              <Label htmlFor="country" value="Country (optional)" />
              <TextInput id="country" value={country} onChange={(e) => setCountry(e.target.value)} />
            </div>
            <div>
              <Label htmlFor="city" value="City (optional)" />
              <TextInput id="city" value={city} onChange={(e) => setCity(e.target.value)} />
            </div>
          </div>
          {error && <p className="text-red-500 text-sm">{error}</p>}
          <Button type="submit" isProcessing={loading} disabled={loading}>
            Publish Story
          </Button>
        </form>
      </div>
    </div>
  );
};

export default CreateNews;