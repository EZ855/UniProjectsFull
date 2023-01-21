import React from 'react';
import { useNavigate, createSearchParams, useSearchParams } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap'
import { ArrowLeftCircleFill, ArrowRightCircleFill, PlusCircleFill, Dot } from 'react-bootstrap-icons';
import './CookBooks.css'
import Header from '../components/Header'
import CookbookCard from '../components/CookBookCard'

function CookBooks () {
    const navigate = useNavigate();
    const token = localStorage.token
    console.log(token)
    
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
    /* Interface for cookbook details */
    interface CookBookInterface {
        id: string;
        name: string;
        creator: string;
        recipe_ids: string;
        creator_username: string;
    }
    
    let cookbook_ids: number[];
    // Gets cookbook data from backend
    const [cookbooksReturned, setCookbooksReturned] = React.useState<CookBookInterface[]>([]);
    async function getCookBookData () {
        if (token) {
            fetch('http://localhost:8080/users/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
            })
            })
            .then(r => {
                if (r.status === 200) {
                r.json().then(data => {
                    cookbook_ids = data.users[0].cookbook_ids.map(Number);
                    fetch('http://localhost:8080/cookbooks/info', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept' : 'application/json'
                        },
                        body: JSON.stringify({
                            cookbook_ids: cookbook_ids
                        })
                    })
                    .then(r => {
                        return r.json();
                    })
                    .then(rJson => {
                        setCookbooksReturned(rJson.cookbooks);
                        rJson.cookbooks.forEach((cookbookData : any) => getRecipeData(cookbookData))
                    })
                })
                } else {
                    r.json().then(data => {
                        alert('cb fetch failed');
                        return false;
                    })
                }
            })
            
        }
    }

    const [recipesReturnedList, setRecipesReturnedList] = React.useState<RecipeInterface[][]>([]);
    async function getRecipeData(cookbook : CookBookInterface) {
        let recipesArray = cookbook.recipe_ids;
        // Gets recipe details for current cookbook
        fetch('http://localhost:8080/recipes/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                recipe_ids: recipesArray
            })
        })
        .then(r => {
            console.log('r.status=', r.status)
            return r.json();
        })
        .then(rJson => {
            recipesReturnedList.push(rJson.recipes)
        })
    }

    /* Update cookbook data whenever page refreshes */ 
    React.useEffect(()=>{
        getCookBookData()
    },[])
    // Evan bootleg workaround for when first loading page doesn't show recipes
    setTimeout(()=> getCookBookData(),50)



    // Make cookbook
    function makeCookBook (cbAndR : any) {

        let recipesList = cbAndR[1];
        let recipes = [];
        if (recipesList) {
            recipes = recipesList.map((recipe: any) => makeCookBookCard(recipe))
        }

        return (
            <div className='cookbook_container'>
                <h2 className='cookbooksSingleHeader' onClick={event => handleClickCookBook(cbAndR[0].id)}>
                        {cbAndR[0].name}
                </h2>
                <a className='cbsUsername' onClick={()=> {handleClickCreator(cbAndR[0].creator)}}> {cbAndR[0].creator_username} </a>
                {recipes}
            </div>
        )
    }
    // Makes a recipe in a cookbook
    function makeCookBookCard (recipe : any) {
        return (
            <div className='cbsbox' onClick={event => handleClickCookBookCard(recipe.id)}>
                {CookbookCard(recipe)}                
            </div>
        )
    }
    // function makeCookBookCardAdd() {
    //     return (
    //         <div className='addRecipes'><h1 className='addRecipesText'> Add recipes! <PlusCircleFill></PlusCircleFill></h1></div>
    //     )
    // }
    // function makeCookBookCardEnd() {
    //     return (
    //         <div className='addRecipesEnd'><h1 className='addRecipesEndText'> <Dot></Dot><Dot></Dot><Dot></Dot> </h1></div>
    //     )
    // }

    const [newCookbook, setNewCookbook] = React.useState(false);
    // Creates prompt for creating another cookbook
    function makeNewCookBook() {
        return (
            <div>
                {!newCookbook && <div className='cookbook_container' onClick={() => {setNewCookbook(true)}}>
                    <div className='newCB'>
                        <h1 className='newCBText'> Create a new cookbook! </h1>
                        <h1 className='newCBText'><PlusCircleFill></PlusCircleFill></h1>
                    </div>
                </div>}
            </div>
        )
    }

    const [cookbookTitle, setCookbookTitle] = React.useState('');
    // Creates form for creating another cookbook
    function makeNewCookBookForm() {
        return (
            <div>
                {newCookbook && <div className='cookbook_container addCookBookContainer'>
                    <Form.Control className='cbTitleInput' value = {cookbookTitle} onChange={e => setCookbookTitle(e.target.value)} placeholder='Enter title'></Form.Control>
                    <div className='cbAddButtons'>
                    <button className='cancelButton' onClick={() => {setNewCookbook(false)}}> Cancel </button>
                    <button className='saveButton'  onClick={handleNewCookBook}> Save </button>
                    </div>
                </div>}
            </div>
            )
    }

    let showButtons = false;
    /* Make list of card components */
    let cookbooksAndRecipes = [];
    let cookbooks = [];
    if (cookbooksReturned) {
        cookbooksAndRecipes = cookbooksReturned.map((e, i) => [e, recipesReturnedList[i]])

        console.log('cbAndR', cookbooksAndRecipes)
        cookbooks = cookbooksAndRecipes.map(cbAndR => makeCookBook(cbAndR))
        cookbooks.push(makeNewCookBook())
        cookbooks.push(makeNewCookBookForm())
        if(cookbooks.length > 5) showButtons = true;
    } else {
        cookbooks.push(makeNewCookBook())
        cookbooks.push(makeNewCookBookForm())
    }




    // onClick functions
    function handleClickCookBookCard (id : String) {
        navigate({
            pathname: "/recipe",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
    }
    function handleClickCreator (id : String) {
        navigate({
            pathname: "/profile",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
    }
    function handleClickCookBook (id : String) {
        navigate({
            pathname: "/cookbook",
            search: createSearchParams({
                id: '' + id
            }).toString()
        });
    }
    function handleNewCookBook () {
        if(!cookbookTitle) {
            alert('Type in a title!')
            return
        }
        fetch('http://localhost:8080/cookbooks/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
                cookbook: {
                    name: cookbookTitle,
                    recipe_ids: []
                }
            })
        }).then( () => {
            navigate(0)
        })
    }
    function handleButtonRight() {
        var doc = document.getElementById('cook_container');
        if (doc != null) {
            doc.scrollLeft += 350;
        }
    };
    function handleButtonLeft() {
        var doc = document.getElementById('cook_container');
        if (doc !== null) {
            doc.scrollLeft -= 350;
        }
    };
    
    

      return <>
      <Header/>
      <br></br>
      <div className='cookbooksHeader'>
        <h1 className='cookbookTitle'> My Cookbooks </h1>
      </div>
      <p></p>
        <div id='cook_container' className='overall_cookbook_container'>
            {showButtons && <div className='cookbookScrollButtonLeft'>
                <button className='cookbookButton' type='button' onClick={handleButtonLeft}><ArrowLeftCircleFill></ArrowLeftCircleFill> </button>
            </div>}
            {showButtons && <div className='cookbookScrollButtonRight'>
                <button className='cookbookButton' type='button' onClick={handleButtonRight}><ArrowRightCircleFill></ArrowRightCircleFill> </button>
            </div>}
                {cookbooks}            
        </div>
        
      </>;
    }
    
    export default CookBooks;