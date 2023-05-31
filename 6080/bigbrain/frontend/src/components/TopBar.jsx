import React from 'react'
import { useNavigate } from 'react-router-dom';
import { fetchSetup } from '../pages/helper.js';
import Typography from '@mui/material/Typography';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import LogoutButton from './LogoutButton.jsx';

function TopBar () {
  const navigateTo = useNavigate();
  const token = localStorage.getItem('token');

  const logout = () => {
    try {
      fetchSetup('POST', 'admin/auth/logout', token, null);
      localStorage.removeItem('token');
      navigateTo('/');
    } catch (error) {
      console.log('error');
    }
  }
  const goToDashboard = () => {
    navigateTo('/dashboard');
  }

  const handleKeyDown = (event) => {
    if (event.key === 'Enter') navigateTo('/dashboard');
  }

  const entireStyle = {
    width: '100%',
    display: 'flex',
  }

  return (
    <AppBar position="static" id="top-bar" style={entireStyle}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Typography tabIndex={0} variant="h6" onKeyDown={handleKeyDown} onClick={goToDashboard} style={{ cursor: 'pointer' }}>
              BIG BRAIN TIME
          </Typography>
          <LogoutButton
            onClick={logout}
            variant="contained"
            color="error"
            id="top-bar-logout"
            text={'Logout'}
          ></LogoutButton>
        </Toolbar>
      </AppBar>
  )
}

export default TopBar
