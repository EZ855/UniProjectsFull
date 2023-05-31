import React from 'react'
import ErrorBox from './ErrorBox'

describe('<ErrorBox />', () => {
  it('renders no props', () => {
    cy.mount(<ErrorBox />);
    cy.contains('Error');
  })
  it('renders with props', () => {
    cy.mount(<ErrorBox
      errorMessage='TESTING123!!!'
      closeError={e => e.target.remove()}
    />);
    cy.contains('Error');
    cy.contains('TESTING123!!!');
  })
  it('prop function executes when clicked', () => {
    cy.mount(<ErrorBox
      errorMessage='TESTING123!!!'
      closeError={() => { document.getElementById('error-TESTING123!!!').remove() }}
    />);
    cy.contains('TESTING123!!!');
    cy.get('[id^=error-]').click(260, 20);
    cy.contains('TESTING123!!!').should('not.exist');
  })
})
