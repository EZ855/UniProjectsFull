import React from 'react';
import { useNavigate } from 'react-router-dom';
import { styled } from '@mui/material/styles';
import { fetchSetup } from '../pages/helper.js';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import StopIcon from '@mui/icons-material/Stop';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import ErrorBox from '../components/ErrorBox.jsx';
// Function component for displaying a quiz in Dashboard.jsx
// { game } is a quiz object
export default function QuizCard ({ game, setSessionDetails, setNotification, setShowNotification, setNotificationButton }) {
  const navigateTo = useNavigate();
  // Default value given if no props given
  if (game === undefined) {
    game = {
      questions: [],
      name: 'My first quiz',
      thumbnail: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=',
      completionTime: 0
    }
  }
  const [error, setError] = React.useState('');
  const [showError, setShowError] = React.useState(false);

  const token = localStorage.getItem('token');
  // Navigates to /quiz/id according to stored id in 'data-value'
  const editQuiz = (e) => {
    navigateTo(`/quiz/${e.currentTarget.getAttribute('data-value')}`);
  }
  // Deletes this quiz according to stored id
  const deleteQuiz = async (e) => {
    const quizId = e.currentTarget.getAttribute('data-value');
    try {
      await fetchSetup('DELETE', 'admin/quiz/' + quizId, token, null);
    } catch (error) {
      setError(error);
      setShowError(true);
    }
    document.getElementById(quizId).remove();
  }
  // Copied style from https://mui.com/material-ui/react-grid/
  // TODO fix this disgusting code
  const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  }));

  // Get the current session ID for a given quiz
  const getSessionID = async (quizID) => {
    try {
      const response = await fetchSetup('GET', `admin/quiz/${quizID}`, token, null);
      const sessionID = response.active;
      return sessionID;
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  // Starts the quiz when the admin presses the play button
  const startQuiz = async (e) => {
    const quizId = e.currentTarget.getAttribute('data-value');
    try {
      await fetchSetup('POST', `admin/quiz/${quizId}/start`, token, null);
      const quizSessionID = await getSessionID(quizId);
      // Shows a notification that allows you to copy the URL for the game
      setNotification(`Quiz started! The session ID is: ${quizSessionID}`);
      setSessionDetails({ gameID: quizId, session: quizSessionID })
      setShowNotification(true);
      setNotificationButton('Copy Link');
      // setGameActive(true);
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  // Stops the quiz when the admin presses the stop button
  const stopQuiz = async (e) => {
    const quizId = e.currentTarget.getAttribute('data-value');
    try {
      await fetchSetup('POST', `admin/quiz/${quizId}/end`, token, null);
      // Shows a notification asking if the admin wants to see the results
      setNotification('Quiz stopped! Would you like to view the results?');
      setShowNotification(true);
      setNotificationButton('Yes');
      // setGameActive(false);
    } catch (error) {
      setError(error);
      setShowError(true);
    }
  }

  // Goes the to the session settings page
  const sessionSettings = async (e) => {
    const quizId = e.currentTarget.getAttribute('data-value');
    const sessionID = await getSessionID(quizId);
    if (sessionID === null) {
      setError('No active sessions. Start a session first by pressing the play button.');
      setShowError(true);
      return
    }
    navigateTo(`/admin/game?gameID=${quizId}&sessionID=${sessionID}`);
  }

  return (
    <>
    {showError && <ErrorBox errorMessage={error} closeError={ () => { setShowError(false) }} />}
    <Grid item xs={4} id={game.id}>
    <Item>
        <div>{game.name}</div>
        <img
          src={game.thumbnail}
          alt={game.name + ' thumbnail'}
          style={{ height: '100px', width: '100px' }}
        ></img>
        <div>{game.questions.length} questions</div>
        <div>{game.completionTime} seconds to complete</div>
        <Button
          data-value={game.id}
          onClick={editQuiz}
          variant="outlined"
          id='edit-button'
          aria-label="Edit quiz"
        >
          <EditIcon alt='Quiz edit icon'/>
        </Button>
        <Button
          data-value={game.id}
          onClick={deleteQuiz}
          variant="outlined"
          aria-label="Delete quiz"
          id='delete-button'
        >
          <DeleteIcon alt='Quiz delete icon'/>
        </Button>
        <Button
          data-value={game.id}
          onClick={startQuiz}
          variant="outlined"
          aria-label="Start quiz"
        >
          <PlayArrowIcon alt='Start quiz session icon'/>
        </Button>
        <Button
          data-value={game.id}
          onClick={stopQuiz}
          variant="outlined"
          aria-label="Stop quiz"
        >
          <StopIcon alt='Stop quiz session icon'/>
        </Button>
        <Button
          data-value={game.id}
          onClick={sessionSettings}
          variant="outlined"
          aria-label="Session settings"
        >
          Session Settings
        </Button>
    </Item>
    </Grid>
    </>
  )
}
