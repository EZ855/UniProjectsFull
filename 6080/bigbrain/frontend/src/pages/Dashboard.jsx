import React from 'react';
import { fetchAPI, fetchSetup } from './helper.js';
import { useNavigate } from 'react-router-dom';
import QuizCard from '../components/QuizCard.jsx';
import TopBar from '../components/TopBar.jsx';
import AddIcon from '@mui/icons-material/Add';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';

const popupStyle = {
  width: '50%',
  position: 'fixed',
  top: '100px',
  left: '50%',
  transform: 'translateX(-50%)',
  zIndex: '10',
  fontSize: '13pt'
}

const inputFieldStyle = {
  marginTop: '20px',
  paddingBottom: '20px'
}

export default function Dashboard () {
  const token = localStorage.getItem('token');
  const navigateTo = useNavigate();

  const [games, setGames] = React.useState([]);
  const [added, setAdded] = React.useState(false); // For triggering re-render
  const [notification, setNotification] = React.useState('');
  const [showNotification, setShowNotification] = React.useState(false);
  const [notificationButton, setNotificationButton] = React.useState(''); // button that changes function
  const [sessionDetails, setSessionDetails] = React.useState({ gameID: null, session: null }); // for the buttons on the notification popup
  // const [gameActive, setGameActive] = React.useState([]); // stores whether a game is active

  React.useEffect(async () => {
    // Setting up fetch call
    const path = 'admin/quiz';
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token
      },
    };

    try {
      // Getting all quiz info (we want their IDs)
      const response = await fetchAPI(path, options);
      const quizzes = response.quizzes;
      const requests = quizzes.map(quiz =>
        // Requesting complete quiz info (we want each question number and total time to complete)
        fetchAPI(`admin/quiz/${quiz.id}`, options)
      );
      // Waiting until all promises are resolved
      Promise.allSettled(requests)
        .then(response =>
          // Removing need to type value
          response.map(response => response.value)
        )
        .then(data => {
          // For every quiz, adds quiz id to the
          // fetched quiz data objects (they don't have it)
          // and calculates max completion time
          for (let index = 0; index < data.length; index++) {
            data[index].id = quizzes[index].id;
            data[index].completionTime = 0;
            data[index].questions.forEach(question => {
              data[index].completionTime += question.time;
            })
          }
          // Finally sets state for rendering
          setGames(data);
        })
    } catch (error) {
      console.log(error);
    }
  }, [added]);

  // Handles when the user presses enter to create a new quiz
  const handleKeyDownNewQuiz = e => {
    if (e.key === 'Enter') {
      newQuiz();
    }
  }

  // Function for new quiz button
  const newQuiz = async () => {
    // Getting text area input value (name of quiz)
    const input = document.getElementById('create-quiz');
    try {
      // Pushes to backend
      await fetchSetup('POST', 'admin/quiz/new', token, { name: input.value });
      // Resetting text area value
      input.value = '';
      // Triggering re-render
      setAdded(!added);
    } catch (error) {
      console.log(error);
    }
  }

  // Handles the click on the notification popup button
  const handleClick = async () => {
    // Grab the game ID and the session ID
    const gameID = sessionDetails.gameID;
    const session = sessionDetails.session;
    if (notificationButton === 'Copy Link') {
      // THIS IS THE LINK FOR A GAME
      const URL = `http://localhost:3000/game?sessionID=${session}`
      await navigator.clipboard.writeText(URL);
      setNotificationButton('Copied');
    } else if (notificationButton === 'Yes') {
      // THIS IS THE ADMIN SCREEN FOR THE GAME
      navigateTo(`/admin/game?gameID=${gameID}&sessionID=${session}`);
    }
  }

  return (
    <div>
      <header>
        <TopBar/>
      </header>
      <main>
        {showNotification &&
          <Alert role="alert" style={popupStyle} onClose={ () => setShowNotification(false) } severity="info">
            <AlertTitle><strong>Notification</strong></AlertTitle>
            {notification}
            <Button
            onClick={handleClick}
            variant="outlined"
            aria-label="Handle notification"
            >{notificationButton}</Button>
          </Alert>
        }
        <Grid container direction="column" spacing={2} alignItems="center">
          <Grid item>
            <TextField
              id="create-quiz"
              label="Name your quiz!"
              variant="outlined"
              style={inputFieldStyle}
              onKeyDown={handleKeyDownNewQuiz}
            />
            <Button onClick={newQuiz} aria-label="Add new quiz" style={inputFieldStyle} variant="contained"><AddIcon alt='Add new quiz icon'/></Button>
          </Grid>
        </Grid>
            <Box sx={{ flexGrow: 1 }}>
              <Grid container spacing={2}>
                {games.map((game) =>
                  <><QuizCard game={game} setSessionDetails={setSessionDetails} setNotification={setNotification} setShowNotification={setShowNotification} setNotificationButton={setNotificationButton}>
                  </QuizCard></>
                )}
              </Grid>
            </Box>
      </main>
    </div>
  )
}
