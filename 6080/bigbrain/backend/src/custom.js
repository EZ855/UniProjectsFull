/*
 For a given data structure of a question, produce another
 object that doesn't contain any important meta data (e.g. the answer)
 to return to a "player"
*/
export const quizQuestionPublicReturn = question => {
  return {
    name: question.name,
    id: question.id,
    time: question.time,
    multipleChoice: question.multipleChoice,
    singleChoice: question.singleChoice,
    points: question.points,
    video: question.video,
    photo: question.photo,
    answers: question.answers,
  };
};

/*
 For a given data structure of a question, get the IDs of
 the correct answers (minimum 1).
*/
export const quizQuestionGetCorrectAnswers = question => {
  const answers = [];
  for (let x = 0; x < question.answers.length; x+=1) {
    if (question.answers[x].correct === true) {
      answers.push(question.answers[x].name);
    }
  }

  return answers; // For a single answer
};

/*
 For a given data structure of a question, get the IDs of
 all of the answers, correct or incorrect.
*/
export const quizQuestionGetAnswers = question => {
  const answers = [];
  for (let x = 0; x < question.answers.length; x+=1) {
    answers.push(question.answers[x].id);
  }
  return answers; // For a single answer
};

/*
 For a given data structure of a question, get the duration
 of the question once it starts. (Seconds)
*/
export const quizQuestionGetDuration = question => {
  return question.time;
};
