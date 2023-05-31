import React from 'react'
import AddAnswerCard from './AddAnswerCard'

describe('<AddQuestionCard />', () => {
  it('renders no props', () => {
    // see: https://on.cypress.io/mounting-react
    cy.mount(<AddAnswerCard />)
  })
  const question = {
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
  it('renders with props', () => {
    // see: https://on.cypress.io/mounting-react
    cy.mount(<AddAnswerCard
      question={question}
      addAnswerOrig={a => {
        const answer = document.createElement('div');
        answer.id = a.id;
        answer.name = a.name;
        answer.correct = a.correct;
        document.appendChild(answer);
      }}
    />)
  })
  it('create answer completely working', () => {
    // see: https://on.cypress.io/mounting-react
    cy.mount(<AddAnswerCard
      question={question}
      addAnswerOrig={a => {
        console.log(a);
        const answer = document.createElement('div');
        answer.innerText = a.id + ' ' + a.name + ' ' + a.correct;
        document.getElementById('add-answer-0').appendChild(answer);
      }}
    />);
    cy.get('#add-answer-0').type('TESTING NEW ANSWER');
    cy.get('[aria-labelledby=controlled-radio-buttons-group-answer-add]').click(20, 70);
    cy.get('[aria-label=add-question]').click();
  })
})
