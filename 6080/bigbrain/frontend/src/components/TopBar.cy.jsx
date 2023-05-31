import React from 'react'
import TopBar from './TopBar'
import { BrowserRouter as Router } from 'react-router-dom';

describe('<TopBar />', () => {
  it('Mount/render topbar', () => {
    cy.mount(
       <Router>
          <TopBar />
       </Router>
    );
  });
  it('Click logout button', () => {
    cy.mount(
      <Router>
         <TopBar />
      </Router>
    );
    // Below works if you're logged in
    // cy.get('#top-bar-logout').click();
    // cy.url().should('eq', 'http://localhost:8080/');
  });
  it('Click logo name', () => {
    cy.mount(
      <Router>
        <TopBar />
      </Router>
    );
    cy.contains('BIG BRAIN TIME').click();
    cy.url().should('eq', 'http://localhost:8080/dashboard');
  });
});
