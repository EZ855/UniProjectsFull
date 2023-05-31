import React from 'react'
import LogoutButton from './LogoutButton'

describe('<LogoutButton />', () => {
  it('renders no props', () => {
    // see: https://on.cypress.io/mounting-react
    cy.mount(<LogoutButton />)
  })
  it('renders with props', () => {
    // see: https://on.cypress.io/mounting-react
    cy.mount(<LogoutButton
      variant="contained"
      color="error"
      text='Logout'
    />)
  })
})
