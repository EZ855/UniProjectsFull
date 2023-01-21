import React from 'react';
import { useNavigate } from 'react-router-dom';
import soupLogo from '../images/soup.png';
import './Card.css'

type ingredientList =  {
  name: string;
  quantity: number;
  units: string;
}[] | undefined

type comment = {
  author_username: string;
  comment: string
}

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

function Card ({id, name, meal_type, tags, ingredients, methods, photo, likes, comments, author_id, author_username, liked}: RecipeInterface) { 
  return (
    <div className={'list_div'}>
      <div className={'image_div'}>
        <img className='food_image' src={'http://localhost:8080/recipes/image/get/' + id}/>
      </div>
      <div className={'item'}>
        <h2>{name}</h2>
        <h3>Type: {meal_type}</h3>
        <h3>Author: {author_username}</h3>
        <h3>Ingredients: {ingredients?.length}</h3>
        <h3>Likes: {likes.length}</h3>
      </div>
    </div>
  )
}

export default Card;
