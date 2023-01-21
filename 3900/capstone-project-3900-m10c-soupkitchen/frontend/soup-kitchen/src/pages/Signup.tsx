import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap'
import { ArrowReturnLeft, ExclamationCircle } from 'react-bootstrap-icons';
import './FormStyle.css'
import './SignupStyle.css'
import soupLogo from '../images/soup.png';


function Signup () {
    const navigate = useNavigate();

    function landing () {
        navigate('/')
    }
    function login () {
        navigate('/login')
    }

    /* variables to be submitted */
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirm, setPasswordConfirm] = useState('');
    /* validity check variables */
    const [takenUsername, setTakenUsername] = useState(false);
    const [takenEmail, setTakenEmail] = useState(false);
    const [passNoMatch, setPassNoMatch] = useState(false);
    const [passTooSimple, setPassTooSimple] = useState(false);
    const [invalidEmail, setInvalidEmail] = useState(false);
    const [invalidUsername, setInvalidUsername] = useState(false);
    const [emptyUsername, setEmptyUsername] = useState(false);
    const [emptyEmail, setEmptyEmail] = useState(false);
    const [emptyPassword, setEmptyPassword] = useState(false);
    const [emptyPasswordConfirm, setEmptyPasswordConfirm] = useState(false);
    const [submitted, setSubmitted] = useState(false);
    const [firstRender, setFirstRender] = useState(true);

    function clickSignup () {
        setSubmitted(true);
    }
    async function sendRegistration () {
        setTakenEmail(false);
        setPassTooSimple(false);
        setTakenUsername(false);
        setInvalidEmail(false);
        setInvalidUsername(false);
        fetch('http://localhost:8080/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                username: username,
                password: password,
            })
            })
            .then(r => {
                if (r.status === 200) {
                r.json().then(data => {
                    if (data.success == true) {
                        console.log(data)
                        localStorage.token = data.token
                        navigate('/Signedup');
                    }
                    else {
                        console.log(data.reason)
                        if (data.reason.includes("email taken")) setTakenEmail(true);
                        if (data.reason.includes("username taken")) setTakenUsername(true);
                        if (data.reason.includes("password")) setPassTooSimple(true);
                        if (data.reason.includes("email")) setInvalidEmail(true);
                        if (data.reason.includes("username")) setInvalidUsername(true);
                        return;
                    }
                })
                } else {
                    console.log("Server error: " + r.status)
                    return;
                }
            })
    }

    /* sets states of fields to true if corresponding error occurs */
    function setStates() {
        if (username.length == 0) {
            setEmptyUsername(true);
        } else {
            setEmptyUsername(false);
        }
        if (email === '') {
            setEmptyEmail(true);
        } else {
            setEmptyEmail(false);
        }
        if (password === '') {
            setEmptyPassword(true);
        } else {
            setEmptyPassword(false);
        }
        if (passwordConfirm === '') {
            setEmptyPasswordConfirm(true);
        } else {
            setEmptyPasswordConfirm(false);
        }
        if (password === passwordConfirm) {
            setPassNoMatch(false);
        } else {
            setPassNoMatch(true);
        }
    }
    React.useEffect(() => {
        if (submitted === true && firstRender === false) {
            console.log("Sending registration")
            setStates();
            sendRegistration();
            setSubmitted(false)
        }
        else {
            console.log(submitted, firstRender)
            setFirstRender(false)
        }
    })

      return <>
      <body>
      <div className='formWrapper'>
        <div className={'logo'}>
          <img src={soupLogo} onClick={landing}/>
          <h2 className={'brand'} onClick={landing}>Soup Kitchen</h2>
        </div>
        <div className='formBox signupFormBox'>
        <Button onClick={login} className={'other_button returnButton'}> <ArrowReturnLeft></ArrowReturnLeft> </Button>
        <p></p>
            <Form className='forms'>
                <h2 className={'logTitle'}>Start exploring recipes today!</h2>
                <Form className='forms'>
                    <Form.Group controlId='signupUsername'>
                        <Form.Control onChange={e => setUsername(e.target.value)} placeholder='Username*'/>
                    </Form.Group>
                    {takenUsername && <h2 className="alert"><ExclamationCircle></ExclamationCircle> Username already taken. Please try another one.</h2> }
                    {invalidUsername && <h2 className="alert"><ExclamationCircle></ExclamationCircle> Invalid username. Please try another one.</h2> }
                    {emptyUsername && <h2 className="alert_orange"><ExclamationCircle></ExclamationCircle> Username field cannot be empty. </h2> }
                    <p></p>
                    <Form.Group controlId='signupEmail'>
                        <Form.Control value = {email} onChange={e => setEmail(e.target.value)} placeholder='Enter valid e-mail*'/>
                    </Form.Group>
                    {takenEmail && <h2 className="alert"><ExclamationCircle></ExclamationCircle> Email already exists. Try logging in.</h2> }
                    {invalidEmail && <h2 className="alert"><ExclamationCircle></ExclamationCircle> Email must be in following format "example@example.com".</h2> }
                    {emptyEmail && <h2 className="alert_orange"><ExclamationCircle></ExclamationCircle> Email field cannot be empty.</h2> }
                    <p></p>
                    <Form.Group controlId='signupPass'>
                        <Form.Control type="password" value = {password} onChange={e => setPassword(e.target.value)} placeholder='Password*' />
                    </Form.Group>
                    {passNoMatch && <h2 className="alert"> <ExclamationCircle></ExclamationCircle> Passwords must match.</h2> }
                    {passTooSimple && <h2 className="alert"> <ExclamationCircle></ExclamationCircle> Password must contain a capital letter, a number and a special character.</h2> }
                    {emptyPassword && <h2 className="alert_orange"><ExclamationCircle></ExclamationCircle> Password field cannot be empty.</h2> }
                    <div className={'important'}>Password must contain the following:
                        <ul>
                            <li> a capital letter</li>
                            <li> a number</li>
                            <li> a special character</li>
                        </ul>
                        
                    </div>
                    <p></p>
                    <Form.Group controlId='signupPassConfirm'>
                        <Form.Control type="password" value = {passwordConfirm} onChange={e => setPasswordConfirm(e.target.value)} placeholder='Re-enter password*' />
                    </Form.Group>
                    {emptyPasswordConfirm && <h2 className="alert_orange"><ExclamationCircle></ExclamationCircle> Re-enter password.</h2> }
                </Form>
                    <br />
                    <br />
                    <Button onClick={clickSignup} className={'other_button submitButton'}>
                    Create account
                    </Button>
                </Form>
            </div>
        </div>
        </body>
        </>
}
    
    export default Signup;