import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from 'react-bootstrap'
import './FormStyle.css'
import './SignupStyle.css'
import soupLogo from '../images/soup.png';


function RecipeCreated () {
    const navigate = useNavigate();

    function landing () {
        navigate('/')
      }
    function login () {
      navigate('/login')
    }
      return <>
      <div className='formWrapper'>
        <div className={'logo'}>
          <img src={soupLogo} onClick={landing}/>
          <h2 className={'brand'} onClick={landing}>Soup Kitchen</h2>
        </div>
        <div className='formBox'>
            <h2 className={'signupTitle'}>Recipe created!</h2>
            <br/>
            <Button onClick={landing} className={'other_button'} type='submit'>
            View your recipes
            </Button>
            <Button onClick={landing} className={'other_button'} type='submit'>
            return to homepage
            </Button>
            </div>
        </div>
      </>;
    }
    
    export default RecipeCreated;
