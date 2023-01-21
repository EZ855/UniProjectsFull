import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap'
import './FormStyle.css'
import soupLogo from '../images/soup.png';


function Login () {
    const navigate = useNavigate();
    const [email, setEmail] = React.useState('');
    const [password, setPassword] = React.useState('');

    function forgot () {
        alert("Remember better next time")
      }
      function landing () {
        navigate('/')
      }
    function clickSignup(e: React.SyntheticEvent<EventTarget>) {
        navigate('/signup')
    }
    function login(e: React.SyntheticEvent<EventTarget>) { 
        fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password,
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
                alert("Server error, unable to log in")
            }
            else if (success.token) {
                localStorage.setItem('token', success.token);
                navigate('/')
            }
            else {
                alert("Username or password is incorrect. Unable to log in")
            }
        })
        e.preventDefault()
    }

      /*if (r.status === 200) {
                r.text().then(data => {
                    console.log(data)
                })
                console.log("Status was 200")
                localStorage.token = data.token
                localStorage.user_id = data.user_id
                alert('Logged in: check the console!')
            } else {
                console.log(r.status)
            r.json().then(data => {
                alert('No token received');
            })
            }*/
      return <>
      <div className='formWrapper'>
        <div className={'logo'}>
          <img src={soupLogo} onClick={landing}/>
          <h2 className={'brand'} onClick={landing}>Soup Kitchen</h2>
        </div>
        <div className='formBox'>
            <Form>
                <Form className='forms'>
                    <h2 className={'logTitle'}>Log In</h2>
                    <br />
                    <Form.Group controlId='loginEmail'>
                        <Form.Control value = {email} onChange={e => setEmail(e.target.value)} placeholder='Enter email*'/>
                    </Form.Group>
                    <p></p>
                    <Form.Group controlId='loginPass'>
                        <Form.Control type="password" value = {password} onChange={e => setPassword(e.target.value)} placeholder='Enter password*' />
                    </Form.Group>
                </Form>
                    <a className={'loginTxt'} onClick={forgot} type='submit'> Forgot your password? </a>
                    <br />
                    <Button onClick={login} className={'login_button'} type='submit'>
                    Login
                    </Button>
                    &nbsp;&nbsp;
                    <br />
                    <br />
                    <Button onClick={clickSignup} className={'other_button'} type='submit'>
                    Don't have an account? Sign up!
                    </Button>
                </Form>
            </div>
        </div>
      </>;
    }
    
    export default Login;
