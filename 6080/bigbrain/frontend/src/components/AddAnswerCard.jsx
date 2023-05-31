import React from 'react';
import ListItem from '@mui/material/ListItem';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';
import AddIcon from '@mui/icons-material/Add';

import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
// Function component for displaying input to create a new answer
// Used in QuestionEdit.jsx
export default function AddQuestionCard ({ question, addAnswerOrig }) {
  // Radio state for whether the new answer will be correct or not
  const [aCorrect, setACorrect] = React.useState('Correct');
  // Default if no props given
  if (question === undefined) {
    question = {
      name: 'Default',
      id: 0,
      time: 0,
      multipleChoice: true,
      singleChoice: false,
      points: 1,
      video: null,
      photo: null,
      answers: [
        {
          name: 'Replace with your own answer!',
          id: 0,
          correct: true
        },
        {
          name: 'Replace with your own wrong answer!',
          id: 1,
          correct: false
        }
      ]
    }
  }
  const radioChange = e => {
    setACorrect(e.target.value);
  }
  // Pushing new answer to question then to quiz then to backend
  const addAnswer = () => {
    // Making a probably unique ID for the answer
    const questionCopy = question;
    const lastAnswer = questionCopy.answers[questionCopy.answers.length - 1];
    let newId = 0;
    if (lastAnswer !== undefined) {
      newId = lastAnswer.id + 1;
    }
    // New answer object
    const a = {
      name: document.getElementById('add-answer-' + question.id).value,
      id: newId,
      correct: (aCorrect === 'Correct') // converts to either true or false
    }
    // addAnswerOrig defined in QuestionEdit.jsx
    // it pushes answer object -> question -> quiz -> backend
    addAnswerOrig(a);
    // Reset input text area value
    document.getElementById('add-answer-' + question.id).value = '';
    // Reset radio state
    setACorrect('Correct');
  }
  // Function if enter is pressed instead of add button
  const handleKeyDownAnswer = e => {
    if (e.key === 'Enter') {
      addAnswer();
    }
  }

  return (
    <ListItem
      secondaryAction={
        <IconButton
        edge="end"
        aria-label="add-question"
        onClick={addAnswer}
        >
          <AddIcon alt='Add new answer icon'/>
        </IconButton>
      }
    >
      <TextField
        id={'add-answer-' + question.id}
        label="Type a new answer!"
        variant="outlined"
        onKeyDown={handleKeyDownAnswer}
      />
      <FormControl>
        <RadioGroup
          aria-labelledby="controlled-radio-buttons-group-answer-add"
          name="controlled-radio-buttons-group-answer-add"
          value={aCorrect}
          role="radio"
          onChange={radioChange}
        >
          <FormControlLabel value="Correct" control={<Radio />} label="Correct" />
          <FormControlLabel value="Incorrect" control={<Radio />} label="Incorrect" />
        </RadioGroup>
      </FormControl>
    </ListItem>
  )
}
