import React, { useEffect } from 'react';
import { useNavigate, createSearchParams } from 'react-router-dom';
import soupLogo from '../images/soup.png';
import './SearchRecipes.css'
import soupLetters from '../images/Soup-Kitchen-Font.png';
import Card from '../components/Card';

function SearchRecipes () {
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
    const navigate = useNavigate()
    const [query, setQuery] = React.useState('')
    const [search, setSearch] = React.useState('pasta')
    const [recipeList, setRecipeList] = React.useState([]) 
    function landing () {
        navigate('/')
    }
    function renderPage () {
        Promise.all([getInfo()]).then(responses => {
            setRecipeList(responses[0])
        })
    }
    function handleSearch () {
        setSearch(query)
    }
  
     /* Handle onClick for recipe cards */
     function HandleRecipe (id : String) {
        console.log("Clicked")
        navigate({
            pathname: "/recipe",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
    }

    async function getInfo () {
        const searchData = await getSearch()
        let search_results : any
        try {
            search_results = searchData.split(',').map(function(item: string) {
                return parseInt(item, 10);
            });
        }
        catch {
            search_results = searchData
        }
        const response = await fetch('http://localhost:8080/recipes/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            recipe_ids: search_results
        })})
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.recipes
            if (rJson) {
                return rJson
            } else {
                return Promise.reject(new Error(`No recipes found"`))
            }
        }
        else {
            const error = 'No recipes found'
            return Promise.reject(error)
        }        
    }

    async function getSearch () {
        const response = await fetch ('http://localhost:8080/recipes/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept' : 'application/json'
            },
            body: JSON.stringify({
                query: search,
                title: true,
                ingredients: true,
                method: true,
                equipment: true,
                tags: true,
                mealtype: true,
                user: true,
            })
        })
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.recipe_ids
            if (rJson) {
                return rJson
            } else {
                return [1,2,3,4,10]
            }
        }
        else {
            const error = 'No recipes found'
            return Promise.reject(error)
        }        
    }
    function makeCard (recipe : RecipeInterface) {
        return (
            <div onClick={() => HandleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }


    const searchCards = recipeList?.map(item => makeCard(item))

    React.useEffect(() => {
        renderPage()
    },[search])

    return <>
    <div className='searchWrapper'>
        <div className='searchHeader'>
            <img className='searchLogo' onClick={landing} src={soupLetters}/>
            <h1 className='searchH1'>Search Recipes</h1>
            <br/>
            <br/>
            <input className='searchBox'placeholder='Pasta'type='text'onChange={e => setQuery(e.target.value)}></input>
            <button style={{'position': 'relative', 'left':'16.5%', 'top':'55px' }}className='user_button' onClick={() => handleSearch()}>Search</button>
        </div>
        <div className='searchBody'>
            <h1 className='searchH1'>Results</h1>
            {recipeList.length > 0 && searchCards}
            {recipeList.length === 0 && <h1 className='searchH1'>No results for search: {search}</h1>}
        </div>
    </div>
    
    </>


}

export default SearchRecipes;