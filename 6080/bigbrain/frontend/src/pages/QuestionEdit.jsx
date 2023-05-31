import React from 'react';
import { useParams } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import TopBar from '../components/TopBar.jsx';
import AnswerCard from '../components/AnswerCard.jsx';
import AddAnswerCard from '../components/AddAnswerCard.jsx';
import ErrorBox from '../components/ErrorBox.jsx';

import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Slider from '@mui/material/Slider';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';

export default function QuestionEdit () {
  const params = useParams();
  const token = localStorage.getItem('token');

  const [quiz, setQuiz] = React.useState({});
  const [change, setChange] = React.useState(false); // For triggering re-render
  const [error, setError] = React.useState('');
  const [showError, setShowError] = React.useState(false);

  const [question, setQuestion] = React.useState({});
  const [qName, setQName] = React.useState('');
  const [qTime, setQTime] = React.useState(0);
  const [qPoints, setQPoints] = React.useState(1);
  const [qType, setQType] = React.useState('Multiple Choice');
  const [qPic, setQPic] = React.useState('');
  const [qURL, setQURL] = React.useState('');
  const [answers, setAnswers] = React.useState([]);
  const [numAnswers, setNumAnswers] = React.useState(0);

  React.useEffect(async () => {
    try {
      // Get quiz
      const response = await fetchSetup('GET', 'admin/quiz/' + params.quizId, token, null);
      setQuiz(response);
      // Find question
      for (let index = 0; index < response.questions.length; index++) {
        const element = response.questions[index];
        if ('' + element.id === params.questionId) {
          setQuestion(element);
          setQName(element.name);
          setQType((element.multipleChoice && 'Multiple Choice') || (element.singleChoice && 'Single Choice'));
          setQTime(element.time);
          setQPoints(element.points);
          setQPic(element.photo);
          setQURL(element.video);
          setAnswers(element.answers);
          setNumAnswers(element.answers.length);
        }
      }
    } catch (error) {
      console.log(error);
    }
  }, [change]);
  // vvvv State update functions vvvv
  const handleUpload = e => {
    const reader = new FileReader();
    reader.readAsDataURL(e.target.files[0]);
    reader.onload = () => {
      setQPic(reader.result);
    };
    reader.onerror = function (error) {
      console.log('Error: ', error);
    };
  }
  const questionURLChange = e => {
    setQURL(e.target.value);
  }
  const questionNameChange = e => {
    setQName(e.target.value);
  }
  const questionTimeChange = e => {
    setQTime(parseInt(e.target.value));
  }
  const questionPointsChange = e => {
    setQPoints(parseInt(e.target.value));
  }
  const questionTypeChange = e => {
    setQType(e.target.value);
  }
  // Hides displayed info and shows input text areas for editing question data
  const showEditQuestion = () => {
    document.getElementById('edit-question-name-button').style.display = 'none';
    document.getElementById('question-title').style.display = 'none';
    document.getElementById('edit-question-name-input-div').style.display = 'block';
    document.getElementById('question-time').style.display = 'none';
    document.getElementById('edit-question-time-input-div').style.display = 'block';
    document.getElementById('question-points').style.display = 'none';
    document.getElementById('edit-question-points-input-div').style.display = 'block';
    document.getElementById('question-URL').style.display = 'none';
    document.getElementById('edit-question-URL-input-div').style.display = 'block';
  }
  // Shows displayed info and hides input text areas after editing question data
  const hideEditQuestion = () => {
    document.getElementById('edit-question-name-input').value = '';
    document.getElementById('edit-question-name-button').style.display = 'block';
    document.getElementById('question-title').style.display = 'block';
    document.getElementById('edit-question-name-input-div').style.display = 'none';
    document.getElementById('question-time').style.display = 'block';
    document.getElementById('edit-question-time-input-div').style.display = 'none';
    document.getElementById('question-points').style.display = 'block';
    document.getElementById('edit-question-points-input-div').style.display = 'none';
    document.getElementById('question-URL').style.display = 'block';
    document.getElementById('edit-question-URL-input-div').style.display = 'none';
  }
  // vvvv Functions passed to AnswerCard object vvvv
  // Given an answer object, finds it in our question object's answers array
  const updateAnswer = (newAnswer) => {
    // Checks if we're trying to add a second correct answer to a single choice question.
    if ((question.singleChoice === true) && (countCorrect(question) === 1) && newAnswer.correct === true) {
      setError('You can\'t have more than 1 correct answer for a single choice question. Changes not saved');
      setShowError(true);
      return;
    }
    const questionCopy = question;
    // Find matching answer id
    const index = questionCopy.answers.findIndex(element => element.id === newAnswer.id);
    // Replace matching answer with new answer
    questionCopy.answers[index] = newAnswer;
    // Pushing changes to backend
    setQuestion(questionCopy);
    updateQuiz();
  }
  // Given an id, finds matching answer id in our question and deletes it
  // TODO!! THIS IS BUGGED IF YOU DELETE ANYTHING THAT'S NOT THE LAST ANSWER
  const deleteAnswer = (oldAnswerId) => {
    // Prevents having less than 2 answers
    if (numAnswers <= 2) {
      setError('Cannot have less than 2 answers!')
      setShowError(true);
      return;
    }
    const questionCopy = question;
    // Find matching answer id
    const index = questionCopy.answers.findIndex(element => element.id === oldAnswerId);
    // Deleting matching id by index
    questionCopy.answers.splice(index, 1);
    // Pushing changes to backend
    setQuestion(questionCopy);
    updateQuiz();
  }
  // Given an answer object, pushes it to current question's answer array
  const addAnswer = (newAnswer) => {
    // Trying to prevent more than 6 answers being created
    if (numAnswers >= 6) {
      return;
    }
    // Checks if we're trying to add a second correct answer to a single choice question.
    if ((question.singleChoice === true) && (countCorrect(question) === 1) && newAnswer.correct === true) {
      setError('You can\'t have more than 1 correct answer for a single choice question. Changes not saved');
      setShowError(true);
      return;
    }

    const questionCopy = question;
    questionCopy.answers.push(newAnswer);
    // Pushing changes to backend
    setQuestion(questionCopy);
    updateQuiz();
  }

  // Counts the correct number of answers in the given question
  const countCorrect = (question) => {
    const answers = question.answers;
    let count = 0;
    answers.forEach(answer => {
      if (answer.correct === true) {
        count++;
      }
    })
    return count;
  }

  // Gathers all states and puts it into a quiz object and pushes it to the backend
  const updateQuiz = () => {
    const questionCopy = question;
    questionCopy.name = qName;
    questionCopy.time = parseInt(qTime);
    questionCopy.points = parseInt(qPoints);
    questionCopy.photo = qPic;
    questionCopy.video = qURL;
    questionCopy.answers = answers;
    // Prints an error if the user tried to add more than 1 correct answer to a single choice question
    if (qType === 'Multiple Choice') {
      questionCopy.multipleChoice = true;
      questionCopy.singleChoice = false;
    } else {
      if ((countCorrect(questionCopy) > 1) && (answers.length === question.answers.length)) {
        setError('You can\'t have more than 1 correct answer for a single choice question. Changes not saved.');
        setShowError(true);
        return;
      }
      questionCopy.multipleChoice = false;
      questionCopy.singleChoice = true;
    }
    const quizCopy = quiz;
    // Finding our question in quiz object
    for (let index = 0; index < quizCopy.questions.length; index++) {
      if ('' + quizCopy.questions[index].id === params.questionId) {
        // Putting question data into quiz object
        quizCopy.questions[index] = questionCopy;
      }
    }
    try {
      fetchSetup('PUT', 'admin/quiz/' + params.quizId, token, quizCopy);
      // Triggering useEffect re-render
      setChange(!change);
    } catch (error) {
      console.log(error);
    }
  }
  // Save button function
  const handleSave = () => {
    updateQuiz();
    hideEditQuestion();
  }
  // Saves inputted information on any input box
  // If user decides to press enter instead of clicking the save button
  const handleKeyDownEdit = e => {
    if (e.key === 'Enter') {
      updateQuiz();
      hideEditQuestion();
    }
  }

  const marks = [
    {
      value: 1,
      label: '1s',
    },
    {
      value: 5,
      label: '5s',
    },
    {
      value: 10,
      label: '10s',
    },
    {
      value: 15,
      label: '15s',
    },
    {
      value: 20,
      label: '20s',
    },
    {
      value: 30,
      label: '30s',
    },
    {
      value: 45,
      label: '45s',
    },
    {
      value: 60,
      label: '60s',
    },
    {
      value: 90,
      label: '90s',
    },
  ];

  return (
    <div>
      <header>
        <TopBar/>
      </header>
      <main>
        <div>
        {showError && <ErrorBox errorMessage={error} closeError={ () => { setShowError(false) }} />}
          <Grid item xs={12} md={6}>
            <div>
              <img
                src={qPic}
                alt={qName + ' thumbnail'}
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
                <Button aria-label="Upload image" variant="contained" component="span" className="upload-button-question">
                  Upload image
                </Button>
              </label>
              <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="question-URL">
                <a href={qURL} target="_blank" rel="noreferrer" id="question-URL">Video URL</a>
              </Typography>
              <div id="edit-question-URL-input-div" style={{ display: 'none' }}>
                <TextField
                  variant="outlined"
                  id='edit-question-URL-input'
                  label='Video URL'
                  value={qURL}
                  onChange={questionURLChange}
                  onKeyDown={handleKeyDownEdit}
                />
              </div>
              <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="question-title">
                {question.name}
              </Typography>
              <div id="edit-question-name-input-div" style={{ display: 'none' }}>
                <TextField
                  variant="outlined"
                  id='edit-question-name-input'
                  label='Question'
                  value={qName}
                  onChange={questionNameChange}
                  onKeyDown={handleKeyDownEdit}
                />
              </div>
            </div>
            <div>
              <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="question-time">
                {'Time limit: ' + question.time}
              </Typography>
              <div id="edit-question-time-input-div" style={{ display: 'none', width: '500px' }}>
                <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="question-time">
                  {'Time limit:'}
                </Typography>
                <Slider
                  aria-label="seconds slider"
                  role="slider"
                  value={qTime}
                  onChange={questionTimeChange}
                  valueLabelDisplay="auto"
                  marks={marks}
                  min={1}
                  max={90}
                />
              </div>
            </div>
            <div>
              <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="question-points">
                {'Points: ' + question.points}
              </Typography>
              <div id="edit-question-points-input-div" style={{ display: 'none', width: '500px' }}>
                <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div" id="question-time">
                  {'Points:'}
                </Typography>
                <Slider
                  aria-label="points slider"
                  role="slider"
                  value={qPoints}
                  onChange={questionPointsChange}
                  valueLabelDisplay="auto"
                  marks
                  step={5}
                  min={0}
                  max={100}
                />
              </div>
            </div>
            <FormControl>
              <FormLabel id="controlled-radio-buttons-group">Question Type</FormLabel>
              <RadioGroup
                aria-labelledby="controlled-radio-buttons-group"
                name="controlled-radio-buttons-group"
                value={qType}
                role="radio"
                onChange={questionTypeChange}
              >
                <FormControlLabel value="Multiple Choice" control={<Radio />} label="Multiple Choice" />
                <FormControlLabel value="Single Choice" control={<Radio />} label="Single Choice" />
              </RadioGroup>
            </FormControl>
            <Button
              variant="contained"
              aria-label="Edit question"
              id="edit-question-name-button"
              onClick={showEditQuestion}
            >
              Edit question
            </Button>
            <Button
              variant="contained"
              aria-label="Save question"
              id="save-question-button"
              onClick={handleSave}
            >
              Save question
            </Button>
            <Grid>
              {answers && answers.map((a, i) => {
                return (
                  <AnswerCard
                    key={i}
                    answer={a}
                    updateAnswer={updateAnswer}
                    deleteAnswer={deleteAnswer}
                    change={change}
                    setChange={setChange}
                    setError={setError}
                    setShowError={setShowError}
                    numberOfCorrect={countCorrect(question)}
                  />
                )
              })}
              {numAnswers < 6 &&
                <AddAnswerCard
                question={question}
                addAnswerOrig={addAnswer}
                />
              }
            </Grid>
          </Grid>
        </div>
      </main>
    </div>
  )
}
