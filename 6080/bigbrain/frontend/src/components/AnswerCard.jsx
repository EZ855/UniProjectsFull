import React from 'react';
import { styled } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import { Typography } from '@mui/material';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';

import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
// Function component for displaying and editing an answer
// used in QuestionEdit.jsx
// props = {key, answer, updateAnswer, deleteAnswer, change, setChange}
export default function AnswerCard (props) {
  const [answer, setAnswer] = React.useState(props.answer);
  const [aCorrect, setACorrect] = React.useState((answer.correct && 'Correct') || (!answer.correct && 'Incorrect'));
  // Submits inputted info when enter is pressed
  const handleKeyDownAnswerName = e => {
    if (e.key === 'Enter') {
      if (answer.correct === true && props.numberOfCorrect === 1) {
        props.setError('You can\'t have more than 1 correct answer for a single choice question. Changes not saved');
        props.setShowError(true);
        return;
      }
      const answerCopy = answer;
      answerCopy.name = e.target.value;
      setAnswer(answerCopy);
      updateAnswer();
      // Hides input text area and radio
      // Displays current displayed info
      document.getElementById('answer-div-' + answer.id).style.display = 'block';
      document.getElementById('edit-answer-div-' + answer.id).style.display = 'none';
    }
  }
  // Displays input text area and radio for editing an answer
  // Hides current displayed info
  const editAnswerShow = (e) => {
    if ((e.type === 'keydown' && e.key === 'Enter') || e.type === 'click') {
      document.getElementById('answer-div-' + answer.id).style.display = 'none';
      document.getElementById('edit-answer-div-' + answer.id).style.display = 'block';
    }
  }

  // onChange function for radio input
  const answerTypeChange = e => {
    const answerCopy = answer;
    // Conversion of 'Correct' to true and 'Incorrect' to false
    if (e.target.value === 'Correct') {
      answerCopy.correct = true;
    } else {
      answerCopy.correct = false;
    }
    setAnswer(answerCopy);
    setACorrect(e.target.value);
  }
  // Using prop function 'deleteAnswer' found in QuestionEdit.jsx
  const deleteAnswer = e => {
    e.stopPropagation();
    props.deleteAnswer(answer.id);
  }
  // Same as above
  const updateAnswer = () => {
    props.updateAnswer(answer);
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

  return (
    <div id={'answer-' + answer.id}>
      <div id={'answer-div-' + answer.id}>
        <Item
          tabIndex={0}
          onKeyDown={editAnswerShow}
          onClick={editAnswerShow}
          style={{ cursor: 'pointer' }}
        >
          <Typography>
            {answer.name}
          </Typography>
          <Typography>
            {(answer.correct && 'CORRECT') || (!answer.correct && 'INCORRECT')}
          </Typography>
          <IconButton
            edge="end"
            aria-label="delete"
            onClick={deleteAnswer}
            >
              <DeleteIcon alt='Delete answer icon'/>
            </IconButton>
        </Item>
      </div>
      <div
        id={'edit-answer-div-' + answer.id}
        style={{ display: 'none' }}
      >
        <Item>
          <TextField
            variant="outlined"
            id='edit-answer-name-input'
            placeholder={answer.name}
            onKeyDown={handleKeyDownAnswerName}
          />
          <FormControl>
              <RadioGroup
                aria-labelledby="controlled-radio-buttons-group-answer"
                name="controlled-radio-buttons-group-answer"
                role="radio"
                value={aCorrect}
                onChange={answerTypeChange}
              >
                <FormControlLabel value="Correct" control={<Radio />} label="Correct" />
                <FormControlLabel value="Incorrect" control={<Radio />} label="Incorrect" />
              </RadioGroup>
            </FormControl>
            <IconButton
            edge="end"
            aria-label="delete"
            onClick={deleteAnswer}
            >
              <DeleteIcon alt='Delete answer icon'/>
            </IconButton>
        </Item>
      </div>
    </div>
  )
}
