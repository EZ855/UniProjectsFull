import React from 'react'
import { useNavigate, useLocation } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import Typography from '@mui/material/Typography';

export default function GameLobby () {
  const navigateTo = useNavigate();
  const searchParams = new URLSearchParams(useLocation().search);
  const playerID = searchParams.get('playerID');
  const sessionID = searchParams.get('sessionID');

  React.useEffect(() => {
    // Checking if the game has started every second
    const tick = setInterval(async () => {
      try {
        const status = await fetchSetup('GET', `play/${playerID}/status`, null, null);
        if (status.started) {
          clearInterval(tick);
          navigateTo(`/play/game?sessionID=${sessionID}&playerID=${playerID}`);
        }
      } catch (error) {
        // If game has terminated
        if (error === 'Session ID is not an active session') {
          clearInterval(tick);
          navigateTo(`/play/game/results?sessionID=${sessionID}&playerID=${playerID}`);
        }
      }
    }, 1000);
    return () => clearInterval(tick);
  }, [])

  return (
    <div>
      <header>
        <Typography variant="h2">Game Lobby</Typography>
      </header>
      <main>
        <Typography variant="h6">Please wait for your game to start!</Typography>
        <iframe
          width="400"
          height="315"
          src="https://www.youtube.com/embed/hVtm2M08IX4?autoplay=1&mute=1"
          title="YouTube video player"
          allowfullscreen
        >
        </iframe>
        <Typography variant="subtitle1">Please review your course material while you wait 🤓🤓🤓</Typography>
      </main>
    </div>
  )
}
