import React from 'react';
import { useSearchParams, useNavigate, createSearchParams } from 'react-router-dom';
import {PencilSquare, Trash} from 'react-bootstrap-icons';
import Header from '../components/Header'
import Card from '../components/Card'
import './CookBook.css'


function CookBook () {
    const navigate = useNavigate()
    function landing () {
        navigate('/')
    }
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


    const token = localStorage.token
    // Get cookbook_id
    const [searchParams, setSearchParams] = useSearchParams();
    const cookbookId = Number(searchParams.get("id"));

    const [cookbookName, setCookbookName] = React.useState('Default');
    const [cookbookCreator, setCookbookCreator] = React.useState(-1);
    const [cookBookRecipeIds,setCookBookRecipeIds]= React.useState<Number[]>([]);
    const [cookbookCreatorUsername, setCookbookCreatorUsername] = React.useState('Default user');
    const [recipeList,setRecipeList]= React.useState([]);
    const [subscribed, setSubscribed] = React.useState(false)


    async function fetchInitialData() {
        const response = await fetch('http://localhost:8080/cookbooks/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                cookbook_ids: [cookbookId]
            })
        })
        const rJson = await response.json()
        setCookbookName(rJson.cookbooks[0].name);
        setCookbookCreator(rJson.cookbooks[0].creator);
        setCookbookCreatorUsername(rJson.cookbooks[0].creator_username);
        setSubscribed(rJson.cookbooks[0].subscribed);
        setCookBookRecipeIds(rJson.cookbooks[0].recipe_ids)
        // recipesArrayString = recipeList.substring(1, recipeList.length - 1);
        // setCookBookRecipeIds(recipesArrayString.split(",").map(Number));
        // console.log('cookBookRecipeIds=', recipesArrayString.split(",").map(Number));
        // console.log('typeof cookBookRecipeIds=', typeof recipesArrayString.split(",").map(Number));
        fetchRecipeData(rJson.cookbooks[0].recipe_ids)
    }

    /* Fetch recipe data and update state */
    async function fetchRecipeData (idList : number[]) {
        console.log('idList', idList)
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
                setRecipeList(rJson.recipes)
            })
        }   
    /* Make list of card components */
    let cards = recipeList.map(item => makeCard(item))
    let deleteCards = recipeList.map(item => makeDeleteCard(item))
    React.useEffect(() => {
        fetchInitialData()
    }, [])

    
    
    /* Turn dictionary into recipe card */
    function makeCard (recipe : RecipeInterface) {
        return (
            <div onClick={event => handleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }
    /* Turn dictionary into recipe card */
    function makeDeleteCard (recipe : RecipeInterface) {
        return (
            <div className='cbCard' id='cardToHighlight' onClick={event => handleDeleteRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }
    

    
    
    // onClick functions
    function handleRecipe (id : String) {
        navigate({
            pathname: "/recipe",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
    }
    function handleClickCreator (id : Number) {
        if (id===-1) return;
        navigate({
            pathname: "/profile",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
        
    }
    function handleSubscribe () {
        setSubscribed(!subscribed)
        fetch('http://localhost:8080/cookbooks/subscribe', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
                cookbook_id: cookbookId
            })
        })
    }
    function handleUnsubscribe () {
        setSubscribed(!subscribed)
        fetch('http://localhost:8080/cookbooks/unsubscribe', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
                cookbook_id: cookbookId
            })
        })
    }
    function handleEdit () {
        fetch('http://localhost:8080/cookbooks/edit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
                cookbook: {
                    id: cookbookId,
                    name: cookbookName,
                    creator: cookbookCreator,
                    recipe_ids: cookBookRecipeIds
                }
            })
        })
        navigate(0)
    }
    function handleDelete () {
        if (window.confirm('Really delete this cookbook?')) {
            fetch('http://localhost:8080/cookbooks/delete', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    token: token,
                    cookbook_id: cookbookId
                })
            })
            navigate('/')
        }
    }

    function handleDeleteRecipe(id : String) {
        console.log('cookBookRecipeIds=', cookBookRecipeIds)
        if (cookBookRecipeIds) {
            const index = cookBookRecipeIds.indexOf(Number(id), 0);
            if (index > -1) {
                cookBookRecipeIds.splice(index, 1);
            }
        }
    }

    


    const [editMode, setEditMode] = React.useState(false)
    let owned = true;

    return <>
        <Header/>
        <div>
        <div className='cookbookHeader'>
            <span>
                <h1 className='cookbookTitle'> {cookbookName} </h1>
            </span>
            
            <span className='cbButtonSpan'>
                {!owned && <button className='cbButtonContainer'>
                    {!subscribed && <div className='subButton' onClick={() => handleSubscribe()}>Subscribe</div>}
                    {subscribed && <div className='unsubButton' onClick={() => handleUnsubscribe()}>Subscribed</div>}
                </button>}
                {owned && !editMode && <button className='cbButtonContainer' onClick={() => setEditMode(!editMode)}>Remove recipes <PencilSquare></PencilSquare></button>}
                &nbsp;
                {editMode && <button className='cancelButton' onClick={() => navigate(0)}> Cancel </button>}
                &nbsp;
                {editMode && <button className='saveButton' onClick={() => handleEdit()}> Save </button>}
                &nbsp;
                {editMode && <button className='cbdeleteButton' onClick={() => handleDelete()}> Delete Cookbook </button>}
            </span>
        </div>
        <a className={'cbUsername'} onClick={()=> {handleClickCreator(cookbookCreator)}}> {cookbookCreatorUsername} </a>
        </div>
        <div className={'container'}>
                {!editMode && cards}
                {editMode && deleteCards}
        </div>

    </>
}

    export default CookBook;
