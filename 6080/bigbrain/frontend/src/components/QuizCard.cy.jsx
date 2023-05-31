import React from 'react'
import QuizCard from './QuizCard'
import { BrowserRouter as Router } from 'react-router-dom';

describe('<QuizCard />', () => {
  it('renders with no props', () => {
    cy.mount(
      <Router>
         <QuizCard />
      </Router>
    );
    cy.get('img').should('have.attr', 'src', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=')
    cy.get('div').contains('My first quiz');
    cy.get('div').contains('0 questions');
    cy.get('div').contains('0 seconds to complete');
  })

  it('renders with game prop', () => {
    const game = {
      name: 'TESTING123!!!',
      owner: 'a',
      questions: [
        {
          name: 'asdf',
          id: 0,
          time: 20,
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
      thumbnail: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
      active: null,
      createdAt: '2023-04-16T12:41:53.985Z',
      oldSessions: [],
      id: 383007069,
      completionTime: 2000
    }
    cy.mount(
      <Router>
         <QuizCard game={game}/>
      </Router>
    );
    cy.get('img').should('have.attr', 'src', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==');
    cy.get('div').contains('TESTING123!!!');
    cy.get('div').contains('1 questions');
    cy.get('div').contains('2000 seconds to complete');
  })
  it('click edit button', () => {
    const game = {
      name: 'asdf',
      owner: 'a',
      questions: [
        {
          name: 'asdf',
          id: 0,
          time: 20,
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
      thumbnail: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
      active: null,
      createdAt: '2023-04-16T12:41:53.985Z',
      oldSessions: [],
      id: 383007069,
      completionTime: 0
    }
    cy.mount(
      <Router>
         <QuizCard game={game}/>
      </Router>
    );
    cy.get('#edit-button').click();
    cy.url().should('eq', 'http://localhost:8080/quiz/383007069');
  })
  it('click delete button', () => {
    const game = {
      name: 'asdf',
      owner: 'a',
      questions: [
        {
          name: 'asdf',
          id: 0,
          time: 20,
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
      thumbnail: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
      active: null,
      createdAt: '2023-04-16T12:41:53.985Z',
      oldSessions: [],
      id: 383007069,
      completionTime: 0
    }
    cy.mount(
      <Router>
         <QuizCard game={game}/>
      </Router>
    );
    // Below works if you're logged in
    // cy.get('#383007069').should('exist');
    // cy.intercept('DELETE', '/admin/quiz/383007069', []).as('fetchReceived');
    // cy.get('#delete-button').click();
    // cy.wait('@fetchReceived');
    // cy.get('#383007069').should('not.exist');
  })
})
