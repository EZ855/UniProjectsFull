import React, { useEffect } from 'react';
import { useNavigate, createSearchParams } from 'react-router-dom';
import soupLogo from '../images/soup.png';
import './Profile.css'
import Card from '../components/Card';

function Profile (this: any) {


    type User = {
        username: string;
        email: string;
        id: number;
        recipe_ids: number[];
        cookbook_ids: number[];
        follower_ids: number[];
        following_ids: number[];
        subscribed_ids: number[];
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
    type singleUser = {
        id: number,
        username: string
    }
    const [user, setUser] = React.useState<User>()
    const navigate = useNavigate();
    const token = localStorage.token
    const [recipeList, setList] = React.useState([])
    const [follows, setFollows] = React.useState([])

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
    function handleOtherProfile (otherId : number | undefined) {
        navigate({
            pathname: "/otherProfile",
            search: createSearchParams({
                id: '' + otherId
            }).toString()
        });
    }
    
    function handleFile (img: File) {
        const formData = new FormData();
        formData.append('file', img);
        fetch('http://localhost:8080/upload', {
            method: 'POST',
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            body: formData
        }).then(res => {
            if(res.ok) {
                console.log(res.status);
                alert("File uploaded successfully.")
            }
        });
    }
    async function getMyRecipeInfo () {
        const curr_user = await fetchUser()
        const recipes = curr_user.recipe_ids
        let my_recipes : any
        try {
            my_recipes = recipes.split(',').map(function(item: string) {
                return parseInt(item, 10);
            });
        }
        catch {
            my_recipes = recipes
        }
        const response = await fetch('http://localhost:8080/recipes/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            recipe_ids: my_recipes
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
        
    async function fetchUser () {
        const response = await fetch('http://localhost:8080/users/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
            })
            })
        const data = await response.json()
        if (response.ok) {
            const rUser = data?.users[0]
            if (rUser) {
                return rUser
            } else {
                return Promise.reject(new Error(`No user found"`))
            }
        } else {
            const error = 'No user found'
            return Promise.reject(error)
        }        
    }
    async function renderUserButton () {
        const userData = await fetchUser()
        const followerIds = userData.following_ids
        if (followerIds) {
           const response = await fetch('http://localhost:8080/users/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                user_ids: followerIds
            })
            })
            const followerDeets = await response.json()
            if (followerDeets) {
                console.log(followerDeets)
                return followerDeets
            }
            else {
                return Promise.reject(new Error(`No user found"`))
            }
        }
    }
    function makeCard (recipe : RecipeInterface) {
        return (
            <div onClick={() => HandleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }
    const buttonMap = follows.map((user : singleUser) => {
        return (
            <button className='user_button' onClick={() => handleOtherProfile(user.id)}>{user.username}</button>
        )
    })

    const myCards = recipeList?.map(item => makeCard(item))

    function renderProfile () {
        Promise.all([fetchUser(), getMyRecipeInfo(), renderUserButton()]).then(responses => {
            if (responses[0]) {
                setUser(responses[0])
            }
            if (responses[1]) {
                setList(responses[1])
            }
            if (responses[2]) {
                setFollows(responses[2].users)
            }
        })
    }
    useEffect(() => {
        renderProfile()
    },[])

    function landing () {
        navigate('/')
      }
    return <>
    <header className={'profile_nav'}>
        <div className={'logo_nav'}>
          <img src={soupLogo} onClick={landing}/>
          <h2 className={'brand'} onClick={landing}>Soup Kitchen</h2>
        </div>
    </header>

    <div className={'profile_wrapper'}>
        <div>
            <img className='profilePic' src={'http://localhost:8080/users/image/get/image'}/>
            <h1>{user?.username}</h1>
            <label>Email</label>
            <p>{user?.email}</p>
            <label>Upload Profile Picture</label>
            <br/>
            <input type="file" name="myImage" accept=".jpg, .png, .jpeg" onChange={(e) => {
                if (!e.target.files || e.target.files.length === 0) {
                    console.error("Select a file")
                }
                else {
                    handleFile(e.target.files[0])
                }
        }}
      />
        </div>
        <div>
            <h1>My Cookbooks</h1>
            <p>No cookbooks to show!</p>
            <h1>My Recipes</h1>
            {recipeList.length > 0 && myCards}
            {recipeList.length < 1 && <p>You haven't published a recipe yet!</p>}
            <h1>Following</h1>
            {follows.length > 0 && buttonMap}
            {follows.length < 1 && <p>You haven't followed anyone yet. Follow your favourite accounts to see them prioritised in the feed!</p>}
        </div>
    
    </div>
    
    </>
}

export default Profile

