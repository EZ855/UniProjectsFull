import React from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import ErrorBox from '../components/ErrorBox.jsx';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';

export default function Login () {
  const navigateTo = useNavigate();

  // State variables we need
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [error, setError] = React.useState('');
  const [showError, setShowError] = React.useState(false);

  // Submit login details to the backend
  const submit = async (event) => {
    event.preventDefault();
    const method = 'POST';
    const body = {
      email,
      password
    }

    try {
      const response = await fetchSetup(method, 'admin/auth/login', null, body);
      // Add the token to local storage then navigate to the dashboard
      localStorage.setItem('token', response.token);
      navigateTo('/dashboard')
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  // Sends the user to the register page
  const register = () => {
    navigateTo('/register');
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
              <Typography variant='h4'>Login</Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                  id="login-email"
                  label="Email"
                  variant="outlined"
                  onChange={(event) => setEmail(event.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  id="login-password"
                  label="Password"
                  type="password"
                  onChange={(event) => setPassword(event.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <Button type="submit" aria-label="Login" onClick={(event) => submit(event)} variant="contained">Login</Button>
            </Grid>
            <Grid item xs={12}>
              <Typography variant='h6'>Not a member?</Typography>
              </Grid>
            <Grid item xs={12}>
              <Button aria-label="Register" onClick={() => register()} variant="contained">Register</Button>
            </Grid>
        </Grid>
      </form>
    </div>
  )
}
