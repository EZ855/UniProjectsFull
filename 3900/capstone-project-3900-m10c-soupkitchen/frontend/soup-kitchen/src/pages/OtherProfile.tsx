import React, { useEffect } from 'react';
import { createSearchParams, useNavigate, useSearchParams } from 'react-router-dom';
import soupLogo from '../images/soup.png';
import './Profile.css'
import Card from '../components/Card';

function OtherProfile () {

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
    
    const [user, setUser] = React.useState<User>()
    const [recipeList, setList] = React.useState([])
    const [searchParams, setSearchParams] = useSearchParams();
    const [following, setFollowing] = React.useState(false)
    const [followers, setFollowers] = React.useState([])
    const uid = searchParams.get("id")
    const navigate = useNavigate();
    const token = localStorage.token

    type singleUser = {
        id: number,
        username: string
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

    async function getMyRecipeInfo () {
        const curr_users = await fetchUser()
        const recipes = curr_users[0].recipe_ids
        let my_recipes : any
        try {
            my_recipes = recipes.split(',').map(function(item: string) {
                return parseInt(item, 10);
            });
        }
        catch {
            my_recipes = recipes
        }
        console.log(my_recipes)
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
                user_ids: [uid]
            })
            })
        const data = await response.json()
        if (response.ok) {
            const rUser = data?.users
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
    function makeCard (recipe : RecipeInterface) {
        return (
            <div onClick={() => HandleRecipe(recipe.id)}>
                {Card(recipe)}
            </div>
        )
    }
    const myCards = recipeList?.map(item => makeCard(item))

       

function handleFollow () {
    fetch('http://localhost:8080/users/follow', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            token: token,
            user_id: uid
        })
        })
    .then(r => {
        if (r.status === 200) {
        r.json().then(data => {
            setFollowing(true)
        })
        } else {
        r.json().then(data => {
            console.log('User follow failed');
            return false;
        })
        }
    })
}
function handleUnfollow () {
    fetch('http://localhost:8080/users/unfollow', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            token: token,
            user_id: uid
        })
        })
    .then(r => {
        if (r.status === 200) {
        r.json().then(data => {
            setFollowing(false)
        })
        } else {
        r.json().then(data => {
            console.log('User unfollow failed');
            return false;
        })
        }
    })
}
function handleOtherProfile (otherId : number | undefined) {
    navigate({
        pathname: "/otherProfile",
        search: createSearchParams({
            id: '' + otherId
        }).toString()
    });
}

async function renderUserButton () {
    const userData = await fetchUser()
    const followerIds = userData[0].follower_ids
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
            return followerDeets
        }
        else {
            return Promise.reject(new Error(`No user found"`))
        }
    }
}
function renderProfile () {
    Promise.all([fetchUser(), getMyRecipeInfo(), renderUserButton()]).then(responses => {
        if (responses[0]) {
            setUser(responses[0][0])
            if (responses[0][1].following_ids.includes(uid)) setFollowing(true)
        }
        if (responses[1]) {
            setList(responses[1])
        }
        if (responses[2]) {
            setFollowers(responses[2].users)

        }
    })
}
const buttonMap = followers.map((user : singleUser) => {
    return (
        <button className='user_button' onClick={() => handleOtherProfile(user.id)}>{user.username}</button>
    )
})
    useEffect(() => {
        renderProfile()
    }, [])
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
        </div>
        <div>
        {!following && <button className='user_button' onClick={() => handleFollow()}>Follow</button>}
        {following && <button className='user_button' onClick={() => handleUnfollow()}>Unfollow</button>}
            <h1>{user?.username}'s Cookbooks</h1>
            <p>No cookbooks to show!</p>
            <h1>{user?.username}'s Recipes</h1>
            {myCards}
            <h1>Following</h1>
            <p>{buttonMap}</p>
        </div>
    
    </div>
    
    </>
}

export default OtherProfile;

