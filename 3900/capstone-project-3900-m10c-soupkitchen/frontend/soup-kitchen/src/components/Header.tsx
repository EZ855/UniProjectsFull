import React from 'react';
import soupLogo from '../images/soup.png';
import soupLetters from '../images/Soup-Kitchen-Font.png';
import './Header.css'
import { List, Search } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';

function Header () {
    const navigate = useNavigate();
    const [burger, setBurger] = React.useState(false);
    const token = localStorage.token
    function landing () {
        navigate('/')
      }
    function login () {
        navigate('/login')
    }
    function profile () {
      navigate('/profile')
  }
    function create () {
    navigate('/create')
  }
  function search () {
    navigate('/search')
  }
    function logout () {
      fetch('http://localhost:8080/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({
            token: token,
        })
        })
        .then(r => {
            if (r.status === 200) {
                return r.json();
            }
            else {
                return "Server error"
            }
        })
        .then(success => {
            console.log(success)
            if (success === "Servor error") {
                alert("Server error, unable to log out")
            }
            else if (success.success) {
                localStorage.clear()
                navigate('/')
            }
            else {
                alert("Token unable to be deactivated. You will still be logged out.")
                localStorage.clear()
                navigate('/')
            }
        })
      navigate('/')
    }


    
    return (
      <header className={'nav_bar'}>
        <div className={'logoHeader'}>
          <img src={soupLetters} onClick={landing}/>
        </div>
        <div className={'search'}>
          <div className='searchPopUp'>
            <button className={'host_button'} onClick={search}> <Search className='mag'/>Search</button>
          </div>
        </div>
        <div className={'be_host'}>
        {!token && <button className={'host_button'} onClick={login}>Log in</button>}
        {token && <button className={'host_button'} onClick={logout}>Log out</button>}
        <button className={'host_button'} onClick={() => setBurger(!burger)}><List/></button>
        </div>
        {burger && 
        <div className={'burger'}>
          <button className={'host_button'} onClick={profile}>My Profile</button>
          <button className={'host_button'} onClick={create}>Create New Recipe</button>
        </div>}
      </header>
    );
  }

export default Header;
