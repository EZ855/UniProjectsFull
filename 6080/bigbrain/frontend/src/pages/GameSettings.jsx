import Button from '@mui/material/Button';
import React from 'react';
import { useLocation } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import TopBar from '../components/TopBar.jsx';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import GameResults from '../components/GameResults.jsx';
import ErrorBox from '../components/ErrorBox.jsx';

const topStyle = {
  marginTop: '30px'
}

export default function GameSettings () {
  const [gameEnded, setGameEnded] = React.useState(false);
  const [position, setPosition] = React.useState(-1);
  const [totalQuestions, setTotalQuestions] = React.useState(0);
  const [currentQuestion, setCurrentQuestion] = React.useState('');
  const [message, setMessage] = React.useState('');
  const [showQuestionDetails, setShowQuestionDetails] = React.useState(false);
  const [questionTime, setQuestionTime] = React.useState(-1);
  const [error, setError] = React.useState('');
  const [showError, setShowError] = React.useState(false);
  const token = localStorage.getItem('token');
  // Grab the search paramaters from the current URL
  const query = new URLSearchParams(useLocation().search);
  // Extract the gameID and sessionID
  const gameID = query.get('gameID');
  const sessionID = query.get('sessionID');

  // Used to advance the game to the next question
  const advanceGame = async () => {
    try {
      await fetchSetup('POST', `admin/quiz/${gameID}/advance`, token, null);
      let newPosition = 0;
      position === -1 ? newPosition = 1 : newPosition = position + 1;
      setPosition(newPosition);
      setShowQuestionDetails(true);
      setQuestionTime(-1);
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  // Used to rerender the page if the sessionID becomes null (i.e. the game is stopped)
  React.useEffect(async () => {
    try {
      const response = await fetchSetup('GET', `admin/session/${sessionID}/status`, token, null);
      const details = response.results
      // Set the current position and total questions
      setPosition(details.position);
      setTotalQuestions(Object.keys(details.questions).length);
      // This is if we haven't started advancing at all
      if (position === -1) {
        setMessage('Press "Advance Game" to move onto the first question!');
      // This is if we've reached the end of the questions
      } else if (position === totalQuestions) {
        setShowQuestionDetails(false);
        setMessage('Game is over!');
        setGameEnded(true);
      // This is if the quiz is running
      } else {
        if (details.active === true) {
          setGameEnded(false);
          setShowQuestionDetails(true);
          setMessage(`Game ${gameID} with sessionID ${sessionID} has started!`)
          setCurrentQuestion(details.questions[position].name)
        }
      }
      try {
        const response = await fetchSetup('GET', `admin/quiz/${gameID}`, token, null);
        // Set the question time on the settings screen
        if ((questionTime === -1) && (position !== -1) && (position < totalQuestions)) {
          setQuestionTime(parseInt(response.questions[position].time, 10));
        }
      } catch (error) {
        setError(error);
        setShowError(true);
      }
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }, [sessionID, position, message, questionTime, gameEnded])

  // Used to stop the game when the admin clicks a button
  const stopGame = async () => {
    try {
      await fetchSetup('POST', `admin/quiz/${gameID}/end`, token, null);
      setGameEnded(true);
      setShowQuestionDetails(false);
      setMessage('Game is over!');
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  return (
    <div>
      {showError && <ErrorBox errorMessage={error} closeError={ () => { setShowError(false) }} />}
      <header>
        <TopBar/>
      </header>
      <main>
        {gameEnded
          ? <GameResults message={message}/>
          : <Grid item xs={12} direction="column" spacing={2} alignItems="center">
              <Typography style={topStyle} variant="h6">{message}</Typography>
              {showQuestionDetails
                ? <><Typography variant="subtitle1"><i>Currently on question {position + 1} of {totalQuestions}</i></Typography>
                <Typography variant="h5"><b>Question: {currentQuestion}</b></Typography>
                <Typography variant="subtitle1">Time allowed: {questionTime} seconds</Typography></>
                : <></>
              }
              <Button
              fullWidth
              variant="contained"
              color="primary"
              onClick={advanceGame}
              aria-label="Advance game">Advance Game
              </Button>
              <Button
              fullWidth
              style={{ marginTop: '10px' }}
              variant="contained"
              color="error"
              onClick={stopGame}
              aria-label="Stop game">Stop Game
              </Button>
            </Grid>}
        </main>
    </div>
  )
}
