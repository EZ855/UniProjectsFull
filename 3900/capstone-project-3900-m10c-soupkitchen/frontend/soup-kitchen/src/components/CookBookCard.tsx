import React from 'react';
import { useNavigate } from 'react-router-dom';
import soupLogo from '../images/soup.png';

type ingredientList =  {
  name: string;
  quantity: number;
  units: string;
}[] | undefined

interface RecipeInterface {
  id: string;
  name: string;
  meal_type: string;
  tags: [string];
  ingredients: ingredientList;
  methods: string;
  photo: string;
  likes: number[];
  comments: [string];
  author_id: string;
  author_username: string;
  liked: boolean
}
interface CookBookInterface {
    id: number;
    name: string;
    creator: number;
    recipe_ids: [number];
    
}

function CookbookCard ({name, meal_type, tags, ingredients, methods, photo, likes, comments, author_id, author_username, liked}: RecipeInterface) { 

  return (
    <div className={'cbcContainer'}>
      <div className={'cbcText'}>
        <h2>{name}</h2>
        <h3>Type: {meal_type}</h3>
      </div>
    </div>
  )
}

export default CookbookCard;