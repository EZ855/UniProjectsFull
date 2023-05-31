import React from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchSetup } from '../pages/helper.js';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
// Function component for displaying and editing questions in QuizEdit.jsx
export default function QuestionCard ({ key, index, question, quizId, quizAll, change, setChange }) {
  const navigateTo = useNavigate();
  const token = localStorage.getItem('token');
  console.log({ key, index, question, quizId, quizAll, change, setChange });
  // Default value given if no props given
  if (index === undefined) {
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
    index = 6079;
  }
  // When this component is clicked,
  // navigates to relevant question edit page
  const navToQuestion = (e) => {
    if ((e.type === 'keydown' && e.key === 'Enter') || e.type === 'click') {
      navigateTo(`/quiz/${quizId}/question/${e.currentTarget.getAttribute('data-value')}`)
    }
  };

  // Delete button function
  const deleteQuestion = async (e) => {
    e.stopPropagation();
    // Finds question's index in quiz
    const index = quizAll.questions.indexOf(question);
    // Removing question by index
    if (index > -1) { quizAll.questions.splice(index, 1) }
    try {
      // Pushes changes to backend
      fetchSetup('PUT', 'admin/quiz/' + quizId, token, quizAll);
      // Triggers re-render
      setChange(!change);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <ListItem
      id={'question-card-' + question.id}
      tabIndex={0}
      data-value={question.id}
      onClick={navToQuestion}
      onKeyDown={navToQuestion}
      style={{ cursor: 'pointer' }}
      secondaryAction={
        <IconButton
        edge='end'
        aria-label='delete'
        onClick={deleteQuestion}
        >
          <DeleteIcon alt='Delete question icon'/>
        </IconButton>
      }
    >
      <ListItemText
        primary={index + 1 + '. ' + question.name}
      />
    </ListItem>
  )
}
