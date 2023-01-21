import React from 'react';
import { createSearchParams, useSearchParams, useNavigate } from 'react-router-dom';
import { HandThumbsUp, HandThumbsUpFill, HandThumbsDown, HandThumbsDownFill, ChatLeftDots } from 'react-bootstrap-icons';
import soupLogo from '../images/soup.png';
import Header from '../components/Header'
import './Recipe.css'
import Card from '../components/Card';



function Recipe () {
    interface CustomRecipes {
        recipe_ids: number[]
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
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const recipe_id = searchParams.get("id")
    const [like, setLike] = React.useState(false)
    const [ingredList, setIngredList] = React.useState<string[]>();
    const [recipeList,setList]= React.useState([]);
    const [recipe, setRecipe]= React.useState<RecipeInterface>();
    const [drinks, setDrinks] = React.useState([]);
    const [comments, setComments] = React.useState([])


    /** Create nav links to author profile */
    function handleOtherProfile (uid : string | undefined) {
        navigate({
            pathname: "/otherProfile",
            search: createSearchParams({
                id: '' + uid
            }).toString()
        });
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
        refresh()
    }
    function refresh () : void {
        window.location.reload()
    }

    /** Parse ingredients to display line by line */
    function parseIngredients (ingred_array : ingredientList) {
        let ingred_stringList : string[] | undefined
        if (ingred_array) {
            ingred_stringList = ingred_array.map((ingred) => {
                return (ingred.quantity + ' ' + ingred.units + ' ' + ingred.name + "\n")
            } )
        }
        setIngredList(ingred_stringList)
    }
    

    /* Fetch recipe data and update state */
    async function getData () {
        const response = await fetch('http://localhost:8080/recipes/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept' : 'application/json'
            },
            body: JSON.stringify({
                recipe_ids: [recipe_id]
            })
        })
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.recipes[0]
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

    /** Get drink recommendations  */
    async function drinkInfo () {
        const drinkData = await getDrinks()
        let drinks : any;
        try {
            drinks = drinkData.split(',').map(function(item: string) {
                return parseInt(item, 10);
            });
        }
        catch {
            drinks = drinkData
        }
        const response = await fetch('http://localhost:8080/recipes/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            recipe_ids: drinks
        })})
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.recipes
            if (rJson) {
                return rJson
            } else {
                return Promise.reject(new Error(`No drinks found"`))
            }
        }
        else {
            const error = 'No drinks found'
            return Promise.reject(error)
        }        
    }

    async function getDrinks () {
        const response = await fetch('http://localhost:8080/recipes/drink', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept' : 'application/json'
            },
            body: JSON.stringify({
                recipe_id: recipe_id
            })
        })
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.recipe_ids
            if (rJson) {
                return rJson
            } else {
                return [19,24,25]
            }
        }
        else {
            const error = 'No recipes found'
            return Promise.reject(error)
        }        
    }

    async function similarInfo () {
        const similarData = await getSimilar()
        console.log(similarData)
        let similar_recipes : any
        try {
            similar_recipes = similarData.split(',').map(function(item: string) {
                return parseInt(item, 10);
            });
        }
        catch {
            similar_recipes = similarData
        }
        const response = await fetch('http://localhost:8080/recipes/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            recipe_ids: similar_recipes
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
    /* Fetch similar recipe data */
    async function getSimilar () {
        const response = await fetch('http://localhost:8080/recipes/similar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept' : 'application/json'
            },
            body: JSON.stringify({
                recipe_id: recipe_id
            })
        })
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.similar_recipes
            if (rJson) {
                return rJson
            } else {
                return [1,2,3]
            }
        }
        else {
            const error = 'No recipes found'
            return Promise.reject(error)
        }        
    }
    /** Handle commenting */
    function handleKeyDown(e : any){
        if(e.key !== 'Enter') return
        const value = e.target.value
        if(!value.trim()) return
        postComment(value)
        e.target.value = ''
        refresh()
    }

    /** Post a comment */
    async function postComment(comment : string) {
        const response = await fetch('http://localhost:8080/recipes/comments/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept' : 'application/json'
            },
            body: JSON.stringify({
                token: localStorage.token,
                comment: {
                    recipe: recipe_id,
                    comment: comment
                }
            })
            })
            const data = await response.json()
            console.log(data)
            if (response.ok) {
                const rJson = data?.success
                if (rJson) {
                    console.log(rJson)
                } else {
                    console.log("Comment invalid")
                }
            }
            else {
                const error = 'Comment invalid'
                return Promise.reject(error)
            }      
    }

    /** Fetch comment */ 
    async function commentInfo () {
        const response = await fetch('http://localhost:8080/recipes/comments/get', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({
            recipe_id: recipe_id,
        })
        })
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.comments
            if (rJson) {
                return rJson;
            } else {
                console.log("Like invalid")
                return [];
            }
        }
        else {
            const error = 'Like invalid'
            return Promise.reject(error)
        }      
    }

    /**Handle clicking like button */

    async function handleLike () {
        setLike(!like)
        const response = await fetch('http://localhost:8080/recipes/like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({
            recipe_id: recipe_id,
            token: localStorage.token,
            liked: true
        })
        })
        const data = await response.json()
        if (response.ok) {
            const rJson = data?.success
            if (rJson) {
                console.log(rJson)
            } else {
                console.log("Like invalid")
            }
        }
        else {
            const error = 'Like invalid'
            return Promise.reject(error)
        }      
    }


    /* fetch all data and set states */
    function renderPage () {
        Promise.all([getData(), similarInfo(), drinkInfo(), commentInfo()]).then(responses => {
            if (responses[0]) {
                parseIngredients(responses[0].ingredients)
                setRecipe(responses[0])
                setLike(responses[0].liked)
            }
            if (responses[1]) {
                setList(responses[1])
            }
            if (responses[2]) {
                setDrinks(responses[2])
            }
            if (responses[3]) {
                setComments(responses[3])
            }
        })
    }

    function makeCard (recipe : RecipeInterface) {
        return (
            <div onClick={() => HandleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }
    function makeComment (comment : any) {
        return (
            <div>
                <p className='comment'>{comment.author_username}:</p> <p>{comment.comment}</p>
            </div>
        )
    }

    /* Make list of similar card components */
    const similarCards = recipeList?.map(item => makeCard(item))
    const drinkCards = drinks?.map(item => makeCard(item))
    const commentCards = comments?.map(item => makeComment(item))
     /* Update state whenever page refreshes */ 
     React.useEffect(()=>{
        renderPage()
      },[])
      return <>
      <Header/>
      <div className='recWrapper'>
        <div className='leftCol'>
            <h2 style={{fontWeight: 'bold'}}>Recipe by:</h2>
            <button className='user_button' onClick={() => handleOtherProfile(recipe?.author_id)}>{recipe?.author_username}</button>

        </div>
        <div className='rightCol'>   
            <div className=''>
                <img className='food_image' src={'http://localhost:8080/recipes/image/get/' + recipe?.id}/>
                <h1 className='rec_title'>{recipe?.name}</h1>
                <h3 className='rec_heading'>Meal Type:</h3>
                <h3 className='rec_deet'>{recipe?.meal_type}</h3>
                <h3 className='rec_heading'>Method:</h3>
                <h3 className='rec_deet'>{recipe?.methods}</h3>
                <h3 className='rec_heading'>Ingredients:</h3>
                {ingredList?.map((ingred) => {
                    return (
                        <h3 className='rec_deet'>{ingred}</h3>
                    )
                })}
                {/* fix likes to get size of list, remove dislikes */}
                <span className='count_likes'>{recipe?.likes.length}</span><button className='like_button' onClick={() => handleLike()}>{!like && <HandThumbsUp/>}{like && <HandThumbsUpFill/>}</button>
                <h3 className='rec_heading' style={{paddingTop: 20}}>{<ChatLeftDots/>} Comments:</h3>
                <div className='commentBox'>
                    <input type='text' placeholder='Write a comment...' onKeyDown={e => handleKeyDown(e)}/>
                </div>
                <div>
                    {commentCards}
                </div>
            </div>
        </div>
    </div>
    <div className='recWrapper'>
        <div className='left_recommend'>
            <h2 style={{fontWeight: 'bold'}}> Similar Recipe Recommendations: </h2>
            {similarCards}
        </div>
        <div className='right_recommend'>
            <h2 style={{fontWeight: 'bold'}}> Drink Recommendations: </h2>
            {drinkCards}
        </div>
    </div>
      </>;
    }
    
    export default Recipe;
