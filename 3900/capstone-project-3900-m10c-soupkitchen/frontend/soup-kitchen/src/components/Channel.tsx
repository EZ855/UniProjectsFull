import React from 'react';
import './Channel.css';
import Sidebar from './Sidebar'
import Card from './Card'
import { createSearchParams, useNavigate } from 'react-router-dom'

function Channel () {

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
    /* Initialise state for updating recipes in feed  */

    const [recipeList,setList]= React.useState([]);
    const navigate = useNavigate()
    const token = localStorage.token

    /* Handle */
    function HandleRecipe (id : String) {
        console.log(id)
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
            <div onClick={event => HandleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }

    //Get feed of recipe ids specific to the user
    async function getFeed () {
        if (token) {
            fetch('http://localhost:8080/recipes/feed', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept' : 'application/json'
                },
                body: JSON.stringify({
                    token: token
                })
            })
            .then(r => {
                if (r.status === 200) {
                    return r.json();
                }
                else return "No feed"
            })
            .then(rJson => {
                if (rJson == "No feed") {
                    getData([1,2,3,4,5,6,7,8,9,10])
                }
                else {
                    getData(rJson.recipe_ids)
                }
            })
        }
        else {
            fetch('http://localhost:8080/recipes/feed', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept' : 'application/json'
                },
                body: JSON.stringify({
                    token: 'what'
                })
            })
            .then(r => {
                if (r.status === 200) {
                    return r.json();
                }
                else return "No feed"
            })
            .then(rJson => {
                if (rJson == "No feed") {
                    getData([1,2,3,4,5,6,7,8,9,10])
                }
                else {
                    getData(rJson.recipe_ids)
                }
            })
        }
    }

    /* Fetch recipe data and update state */
    async function getData (idList : number[]) {
    fetch('http://localhost:8080/recipes/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({
            recipe_ids: idList
        })
        })
        .then(r => {
          return r.json();
        })
        .then(rJson => {
            console.log(rJson.recipes)
            setList(rJson.recipes)
        })
    }    

        /* Make list of card components */
        let cards = recipeList.map(item => makeCard(item))
        
       /* Update state whenever page refreshes */ 
      React.useEffect(()=>{
        getFeed()
      },[])

    return (
        <div className={'wrapper'}>
            <Sidebar/> 
            <div className={'container'}>
                {cards}
            </div>
        </div>
    );
}

export default Channel;