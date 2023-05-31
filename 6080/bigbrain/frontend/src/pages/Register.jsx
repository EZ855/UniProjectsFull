import React from 'react';
import {
  useNavigate
} from 'react-router-dom';
import { fetchSetup } from './helper.js';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import ErrorBox from '../components/ErrorBox.jsx';
import Typography from '@mui/material/Typography';

export default function Register () {
  const navigateTo = useNavigate();

  const [name, setName] = React.useState('');
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [error, setError] = React.useState('');
  const [showError, setShowError] = React.useState(false);

  // Submits registration details to the backend
  const submit = async (event) => {
    event.preventDefault();
    const method = 'POST';

    if (!email || !password || !name) {
      setError('Please fill all fields');
      setShowError(true);
      return;
    }

    const body = {
      email,
      password,
      name
    }

    try {
      const response = await fetchSetup(method, 'admin/auth/register', null, body);
      // Add the user token to local storage then go to the dashboard
      localStorage.setItem('token', response.token);
      navigateTo('/dashboard');
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  // Go to the login screen if the user is already a member
  const login = () => {
    navigateTo('/');
  }

  return (
    <div>
        {showError && <ErrorBox errorMessage={error} closeError={ () => { setShowError(false) }} />}
        <form onSubmit={submit}>
          <Grid container direction="column" spacing={1} alignItems="center">
            <Grid item xs={12}>
              <Typography variant='h2'>BigBrain 🧠</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant='h4'>Register</Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                  id="register-name"
                  label="Name"
                  variant="outlined"
                  onChange={(event) => setName(event.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  id="register-email"
                  label="Email"
                  variant="outlined"
                  onChange={(event) => setEmail(event.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  id="register-password"
                  label="Password"
                  type="password"
                  onChange={(event) => setPassword(event.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <Button aria-label="Submit" type="submit" onClick={(event) => submit(event)} variant="contained">Sign Up</Button>
            </Grid>
            <Grid item xs={12}>
            <Typography variant='h6'>Already a member?</Typography>
            </Grid>
            <Grid item xs={12}>
              <Button aria-label="Login" onClick={() => login()} variant="contained">Login</Button>
            </Grid>
          </Grid>
        </form>
    </div>
  )
}
