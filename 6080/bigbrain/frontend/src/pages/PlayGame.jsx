import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';

export default function PlayGame () {
  const navigateTo = useNavigate();
  const searchParams = new URLSearchParams(useLocation().search);
  const playerID = searchParams.get('playerID');
  // State variables we need
  const [questionDetails, setQuestionDetails] = React.useState({});
  const [questionID, setQuestionID] = React.useState({});
  const [questionName, setQuestionName] = React.useState('');
  const [questionTimer, setQuestionTimer] = React.useState(-1);
  const [questionStart, setQuestionStart] = React.useState('');
  const [questionAnswers, setQuestionAnswers] = React.useState([]);
  const [correctAnswer, setCorrectAnswer] = React.useState('');
  const [isDisabled, setIsDisabled] = React.useState(false);
  const [playerAnswers, setPlayerAnswers] = React.useState([]);

  React.useEffect(() => {
    const getQuestionDetails = async () => {
      try {
        const details = await fetchSetup('GET', `play/${playerID}/question`, null, null);
        setQuestionDetails(details.question);
        setQuestionID(questionDetails.id);
        setQuestionName(questionDetails.name);
        setQuestionAnswers(questionDetails.answers);
        setQuestionStart(questionDetails.isoTimeLastQuestionStarted);
        if (questionTimer === -1) setQuestionTimer(parseInt(questionDetails.time, 10));
        // If the timer hits 0, fetch the correct answer (we'll return it as a popup)
        if (questionTimer === 0) {
          try {
            const correct = await fetchSetup('GET', `play/${playerID}/answer`, null, null);
            setCorrectAnswer(correct);
            setIsDisabled(true);
            console.log(correctAnswer);
          } catch (error) {
            console.log(error);
          }
          // After the timer is done, starts an interval that runs every second
          setInterval(async () => {
            try {
              // fetches the current question details
              const newDetails = await fetchSetup('GET', `play/${playerID}/question`, null, null);
              // checks if the IDs are different - this would indicate that the question is different
              if (questionID !== newDetails.question.id) {
                // if so, set the new ID to rerender the page (coz useEffect)
                setQuestionID(newDetails.question.id);
              }
            } catch (error) {
              console.log(error);
            }
          }, 1000)
        }
      } catch (error) {
        console.log(error);
        if (error === 'Session ID is not an active session') {
          navigateTo(`/play/game/results/${playerID}`);
        }
      }
    }
    getQuestionDetails();
    // Run the timer for the question
    const ticks = setInterval(() => {
      if (questionTimer > 0) setQuestionTimer(questionTimer - 1);
    }, 1000);
    return () => clearInterval(ticks);
  }, [playerID, questionID, questionName, questionTimer, questionStart]);

  // console.log(questionDetails);
  // console.log(questionName);
  // console.log(questionStart);
  // console.log(questionAnswers);

  // Sends the user's answer to the server
  const handleClicks = async (answer) => {
    if (questionDetails.singleChoice === true) {
      // remove the answer if it's already in the array
      if (playerAnswers.includes(answer.id)) {
        setPlayerAnswers([]);
      } else {
        // otherwise add it to the array
        setPlayerAnswers(answer.id);
      }
    // handles the case where the question is multiple choice
    } else {
      // check if the answer is already in the array
      if (playerAnswers.includes(answer.id)) {
        // if so, remove it
        setPlayerAnswers(playerAnswers.filter((x) => x !== answer.id))
      } else {
        // otherwise, add it to the array
        setPlayerAnswers([...playerAnswers, answer.id])
      }
    }
    // Now send the answers to the backend
    try {
      await fetchSetup('PUT', `play/${playerID}/answer`, null, { answerIds: playerAnswers });
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <main>
      <Stack spacing={2} alignItems='center'>
        <Typography variant='h4'>{questionName}</Typography>
        <Typography role="timer" variant='h6'>{questionTimer} seconds left</Typography>
        <img
          src={questionDetails.photo}
          alt={questionName + ' photo'}
          style={{ height: '128px', width: '128px', border: 'solid 1px black' }}
        />
        {/* if the questionAnswers array is ready, map each answer to a button */}
        {questionAnswers && questionAnswers.map((answer) => {
          return (
          <Button key={answer.name} aria-label="Choose answer" disabled={isDisabled} variant="contained" onClick={() => handleClicks(answer)}
          color="primary">{answer.name}
          </Button>
          )
        })}
      </Stack>
    </main>
  )
}
