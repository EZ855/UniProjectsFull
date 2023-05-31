import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import ErrorBox from '../components/ErrorBox.jsx';
import Typography from '@mui/material/Typography';

const spacingStyle = {
  marginLeft: '10px'
}

const buttonStyle = {
  marginLeft: '10px',
  marginTop: '20px'
}

export default function JoinGame () {
  const [playerName, setPlayerName] = React.useState('');
  const [error, setError] = React.useState('');
  const [showError, setShowError] = React.useState(false);
  const [session, setSession] = React.useState(null);
  const [isDisabled, setIsDisabled] = React.useState(false);
  const navigateTo = useNavigate();

  // Grab the search paramaters from the current URL
  const searchParams = new URLSearchParams(useLocation().search);
  // Extract the sessionID
  const sessionID = searchParams.get('sessionID');

  React.useEffect(() => {
    if (sessionID !== null) {
      setSession(sessionID);
      setIsDisabled(true);
      console.log(session);
      console.log(setIsDisabled);
    }
  }, [sessionID, session])

  const openSession = (event) => {
    event.preventDefault()
    navigateTo(`/game?sessionID=${document.getElementById('session-id').value}`);
  }

  const joinGame = async (event) => {
    event.preventDefault()
    const method = 'POST';
    const body = {
      name: playerName
    }

    try {
      const response = await fetchSetup(method, `play/join/${sessionID}`, null, body);
      localStorage.setItem('playerID', response.playerId);
      navigateTo(`/play/game/lobby?sessionID=${sessionID}&playerID=${response.playerId}`);
    } catch (error) {
      setError(error);
      setShowError(true);
      navigateTo('/game');
      setIsDisabled(false);
    }
  };

  return (
    <div>
      {showError && <ErrorBox errorMessage={error} closeError={ () => { setShowError(false) }} />}
      <Stack container direction="column" spacing={2} alignItems="center">
        <header>
          <Typography variant="h3">Join Game</Typography>
        </header>
        <main>
          <Box>
            <form onSubmit={openSession}>
                <TextField
                  id="session-id"
                  label="Session ID"
                  variant="outlined"
                  disabled={isDisabled}
                />
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  style={spacingStyle}
                  disabled={isDisabled}
                  aria-label="Join session"
                >
                  Join
                </Button>
            </form>
            <form onSubmit={joinGame}>
              <TextField
                id="player-name"
                label="Player Name"
                variant="outlined"
                style={{ marginTop: '20px' }}
                onBlur={(event) => setPlayerName(event.target.value)}
              />
              <Button
                type="submit"
                variant="contained"
                color="primary"
                style={buttonStyle}
                aria-label="Submit player name"
              >
                Submit
              </Button>
            </form>
          </Box>
          <Typography style={{ marginTop: '10px' }} variant="h6">{`The session you want to join is: ${sessionID}`}</Typography>
        </main>
      </Stack>
    </div>
  );
}
