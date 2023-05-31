import React from 'react'
import QuestionCard from './QuestionCard'
import { BrowserRouter as Router } from 'react-router-dom';

describe('<QuestionCard />', () => {
  it('renders with no props', () => {
    cy.mount(
      <Router>
         <QuestionCard />
      </Router>
    );
  })
  const q = {
    name: 'TESTING123!!!',
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
  const quiz = {
    name: 'asdf',
    owner: 'a',
    questions: [
      {
        name: 'asdf',
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
      },
      {
        name: 'asdf',
        id: 1,
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
      },
      {
        name: 'asdf',
        id: 2,
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
      },
      {
        name: 'asdf',
        id: 3,
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
      },
      {
        name: 'sdf',
        id: 4,
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
    ],
    thumbnail: null,
    active: null,
    createdAt: '2023-04-16T12:41:53.985Z',
    oldSessions: []
  }
  it('renders with props', () => {
    cy.mount(
      <Router>
         <QuestionCard
                key={0}
                index={0}
                question={q}
                quizId={383007069}
                quizAll={quiz}
                change={false}
                setChange={() => { document.getElementById('question-card-' + 0).remove() }}
              />
      </Router>
    );
    cy.get('[data-value=0]').contains('1. TESTING123!!!');
  })
  it('clicking question', () => {
    cy.mount(
      <Router>
         <QuestionCard
                key={0}
                index={0}
                question={q}
                quizId={383007069}
                quizAll={quiz}
                change={false}
                setChange={() => { document.getElementById('question-card-' + 0).remove() }}
              />
      </Router>
    );
    cy.get('#question-card-0').click();
    cy.url().should('eq', 'http://localhost:8080/quiz/383007069/question/0');
  })
  it('clicking delete', () => {
    cy.mount(
      <Router>
         <QuestionCard
                key={0}
                index={0}
                question={q}
                quizId={383007069}
                quizAll={quiz}
                change={false}
                setChange={() => { document.getElementById('question-card-' + 0).remove() }}
              />
      </Router>
    );
    // // Below works if you're logged in
    // cy.get('#question-card-0').should('exist');
    // cy.intercept('DELETE', '/admin/quiz/383007069', []).as('fetchReceived');
    // cy.get('[aria-label=delete]').click();
    // cy.wait('@fetchReceived');
    // cy.get('#question-card-0').should('not.exist');
  })
})
