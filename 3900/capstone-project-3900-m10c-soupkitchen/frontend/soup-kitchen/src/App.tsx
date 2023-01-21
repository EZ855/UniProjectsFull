import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Landing from './pages/Landing';
import Login from './pages/Login';
import Profile from './pages/Profile';
import Signup from './pages/Signup';
import Signedup from './pages/Signedup';
import Recipe from './pages/Recipe';
import CreateRecipe from './pages/CreateRecipe';
import Cookbooks from './pages/CookBooks';
import CookbooksList from './pages/CookBooksList';
import Cookbook from './pages/CookBook'
import RecipeCreated from './pages/RecipeCreated';
import OtherProfile from './pages/OtherProfile';
import SearchRecipes from './pages/SearchRecipes';


function App () {
  return (
    <>
      <Router>

        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/signedup" element={<Signedup />} />
          <Route path="/recipe" element={<Recipe />} />
          <Route path="/create" element={<CreateRecipe />} />
          <Route path="/cookbooks" element={<Cookbooks />} />
          <Route path="/cookbooksadd" element={<CookbooksList />} />
          <Route path="/cookbook" element={<Cookbook />} />
          <Route path="/recipecreated" element={<RecipeCreated />} />
          <Route path="/otherProfile" element={<OtherProfile />} />
          <Route path="/search" element={<SearchRecipes />} />

        </Routes>
      </Router>
    </>
  );
}

export default App;