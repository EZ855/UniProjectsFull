import React from 'react';
import './Channel.css';
import Card from './Card'
import { createSearchParams, useNavigate } from 'react-router-dom'

interface CustomRecipes {
    recipe_ids: number[]
}
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
type ingredientList =  {
    name: string;
    quantity: number;
    units: string;
}[] | undefined

function CustomChannel ({recipe_ids}: CustomRecipes) {
        
    /* Initialise state for updating recipes in feed  */
    const [recipeList,setList]= React.useState([]);
    const navigate = useNavigate()

    /* Handle onClick for recipe cards */
    function HandleRecipe (id : String) {
        navigate({
            pathname: "/recipe",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
    }

    /* Turn dictionary into recipe card */
    function makeCard (recipe : RecipeInterface) {
        return (
            <div onClick={() => HandleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }

    /* Fetch recipe data and update state */
    async function getData () {
    fetch('http://localhost:8080/recipes/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({
            recipe_ids: recipe_ids
        })
        })
        .then(r => {
          return r.json();
        })
        .then(rJson => {
            return rJson.recipes
        })
        .then(rRecipes => {
            console.log(rRecipes)
            setList(rRecipes)
        })
    }    

        /* Make list of card components */
        const cards = recipeList?.map(item => makeCard(item))
        
       /* Update state whenever page refreshes */ 
      React.useEffect(()=>{
        console.log(recipe_ids)
        getData()
      },[])

    return (
        <div className={'wrapper'}>
            <div className={'container'}>
                {cards}
            </div>
        </div>
    );
}

export default CustomChannel;