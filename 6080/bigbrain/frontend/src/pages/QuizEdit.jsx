import React from 'react';
import { useParams } from 'react-router-dom';
import { fetchSetup } from '../pages/helper.js';
import TopBar from '../components/TopBar.jsx';
import QuestionCard from '../components/QuestionCard.jsx';
import AddQuestionCard from '../components/AddQuestionCard.jsx';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';

export default function QuizEdit () {
  const params = useParams();
  const token = localStorage.getItem('token');

  const [quiz, setQuiz] = React.useState({});
  const [change, setChange] = React.useState(false); // For triggering re-render

  React.useEffect(async () => {
    try {
      // Gets quiz and sets state for render usage
      const response = await fetchSetup('GET', 'admin/quiz/' + params.id, token, null);
      setQuiz(response);
    } catch (error) {
      console.log(error);
    }
  }, [change]);

  // Upload button function
  const handleUpload = e => {
    const reader = new FileReader();
    // Converts file to base64
    reader.readAsDataURL(e.target.files[0]);
    reader.onload = () => {
      // Pushes it to backend
      const quizCopy = quiz;
      quizCopy.thumbnail = reader.result;
      setQuiz(quizCopy);
      updateQuiz();
    };
    reader.onerror = function (error) {
      console.log('Error: ', error);
    };
  }
  // Saves inputted information on the quiz name input box
  // If user decides to press enter instead of clicking the save button
  const handleKeyDownQuizName = e => {
    if (e.key === 'Enter') {
      const quizCopy = quiz;
      quizCopy.name = e.target.value;
      setQuiz(quizCopy);
      updateQuiz();
      // Hiding input text areas
      // Displaying quiz title text
      document.getElementById('edit-quiz-name-button').style.display = 'block';
      document.getElementById('quiz-title').style.display = 'block';
      document.getElementById('edit-quiz-name-input-div').style.display = 'none';
    }
  }
  // Showing input text areas
  // Hiding quiz title text
  // Function for edit button
  const showEditName = () => {
    document.getElementById('edit-quiz-name-button').style.display = 'none';
    document.getElementById('quiz-title').style.display = 'none';
    document.getElementById('edit-quiz-name-input-div').style.display = 'block';
  }
  // Pushes state quiz object to backend
  const updateQuiz = () => {
    try {
      fetchSetup('PUT', 'admin/quiz/' + params.id, token, quiz);
      setChange(!change);
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <div>
      <header>
        <TopBar/>
      </header>
      <main>
        <Grid item xs={12} md={6}>
          <img
            src={quiz.thumbnail}
            alt={quiz.name + ' thumbnail'}
            style={{ height: '128px', width: '128px' }}
          ></img>
          <input
            accept="image/png, image/jpeg, image/jpg"
            className="hi"
            style={{ display: 'none' }}
            id="raised-button-file"
            multiple={false}
            type="file"
            onChange={handleUpload}
          />
          <label htmlFor="raised-button-file">
            <Button aria-label="Upload image" variant="contained" component="span" className="upload-button">
              Upload image
            </Button>
          </label>
          <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="quiz-title">
            {quiz.name}
          </Typography>
          <div id="edit-quiz-name-input-div" style={{ display: 'none' }}>
            <TextField
              label={quiz.name}
              variant="outlined"
              onKeyDown={handleKeyDownQuizName}
            />
          </div>
          <Button
            variant="contained"
            aria-label="Edit name"
            id="edit-quiz-name-button"
            onClick={showEditName}
          >
            Edit name
          </Button>
          <List>
            {quiz.questions && quiz.questions.map((q, i) =>
              <QuestionCard
                key={i}
                index={i}
                question={q}
                quizId={params.id}
                quizAll={quiz}
                change={change}
                setChange={setChange}
              />
            )}
            <AddQuestionCard
              quizId={params.id}
              quizAll={quiz}
              change={change}
              setChange={setChange}
            />
          </List>
        </Grid>
      </main>
    </div>
  )
}
