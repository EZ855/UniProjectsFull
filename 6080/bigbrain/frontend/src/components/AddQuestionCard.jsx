import React from 'react';
import { fetchSetup } from '../pages/helper.js';
import ListItem from '@mui/material/ListItem';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';
import AddIcon from '@mui/icons-material/Add';
// Function component for displaying input to create a new question
// Used in QuizEdit.jsx
export default function AddQuestionCard ({ quizId, quizAll, change, setChange }) {
  const token = localStorage.getItem('token');
  // Submitting question data to backend
  const addQuestion = () => {
    // Making a probably unique ID for the question
    const quizCopy = quizAll;
    const lastQuestionInArray = quizCopy.questions[quizCopy.questions.length - 1];
    let newId = 0;
    // Making biggest id + 1 the new id
    if (lastQuestionInArray !== undefined) {
      newId = lastQuestionInArray.id + 1;
    }
    // Question format is as follows below
    const q = {
      name: document.getElementById('add-question').value,
      id: newId,
      time: 15,
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

    quizCopy.questions.push(q);

    try {
      // Send updated quiz to backend
      fetchSetup('PUT', 'admin/quiz/' + quizId, token, quizCopy);
      // Reset input value
      document.getElementById('add-question').value = '';
      // Updating state on parent page to ensure page update
      setChange(!change);
    } catch (error) {
      console.log(error);
    }
  }
  // Case if enter is pressed instead of save button
  const handleKeyDown = e => {
    if (e.key === 'Enter') {
      addQuestion();
    }
  }

  return (
    <ListItem
      secondaryAction={
        <IconButton
        edge="end"
        aria-label="add question"
        onClick={addQuestion}
        >
          <AddIcon alt='Add new question icon'/>
        </IconButton>
      }
    >
      <TextField
        id="add-question"
        label="Type a new question!"
        variant="outlined"
        onKeyDown={handleKeyDown}
      />
    </ListItem>
  )
}
