import React from 'react';
import { useNavigate, createSearchParams } from 'react-router-dom'
import './Sidebar.css';

function Sidebar () {

const token = localStorage.token
const navigate = useNavigate()

function handleFollowing () {
    if (token) {
        navigate('/profile');
    }
    else {
        navigate('/login');
    }
}
function handleCookbooks () {
    if (token) {
        navigate('/cookbooks');
    }
    else navigate('/login')
}
function handleMyRecipes () {
    if (token) {
        navigate('/profile')
    }
    else {
        navigate('/login');
    }
}

    return (
        <div className='sidebar'>
                <button className='side_button' onClick={handleFollowing}>Following</button>
                <button className='side_button'onClick={handleCookbooks}>My Cookbooks</button>
                <button className='side_button'onClick={handleMyRecipes}>My Recipes</button>
            </div>
    );
}

export default Sidebar;