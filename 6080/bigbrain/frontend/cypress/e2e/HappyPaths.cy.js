describe('Happy paths', () => {
  const name = 'Evan Zhang'
  const email = 'z5383657@ad.unsw.edu.au'
  const password = 'PASSword123!!!'
  const quizName = 'HAPPY PATH TEST'
  it('Happy path 1 - from spec', () => {

    // Login page
    cy.visit('http://localhost:3000/');

    // Go to register page
    cy.get('button').contains('Register').click();
    cy.url().should('eq', 'http://localhost:3000/register');

    // Input and submit details
    cy.get('#register-name')
      .focus()
      .type(name);

    cy.get('#register-email')
      .focus()
      .type(email);

    cy.get('#register-password')
      .focus()
      .type(password);

    cy.get('button').contains('Sign Up').click();

    // Automatically goes to dashboard
    cy.url().should('eq', 'http://localhost:3000/dashboard');

    cy.get('#create-quiz')
      .focus()
      .type(quizName)
      .type('{enter}');

    // Start quiz
    cy.get('[data-testid=PlayArrowIcon]')
      .click();

    // Close quiz started notification
    cy.get('[role=alert]')
      .find('button')
      .last()
      .click();

    // Stop quiz
    cy.get('[data-testid=StopIcon]')
      .click();
    // Click on results page button thru notification
    cy.get('[role=alert]')
      .find('button')
      .first()
      .click();

    // Uncomment when res page is done!! Confirming res page url change
    // cy.url().should('eq', 'RESPAGEURL');

    cy.get('.MuiToolbar-root > .MuiButtonBase-root')
      .click();
    
    // Log back in
    cy.get('#login-email')
      .focus()
      .type(email);

    cy.get('#login-password')
      .focus()
      .type(password);
    cy.get('button').contains('Login').click();

    // Back on dashboard
    cy.url().should('eq', 'http://localhost:3000/dashboard');
  })

  const name2 = 'Evan'
  const email2 = 'z@ad.unsw.edu.au'
  const password2 = 'Pword123!!!'
  const quizName2 = 'HAPPY2 TEST'
  const quizNameChanged = 'HAPPY PATH TEST CHANGED!!!'
  const q1 = 'QUESTION NUMERO UNO'
  const q2 = 'Two??'
  const q3 = 'queue three'
  const q4 = 'nummer 4'
  const url = 'https://www.youtube.com/watch?v=DQvwmy-dHTY&ab_channel=spoonkid2'
  const q4Changed = 'QUATRO'
  const newAnswer = 'HEHAHO'
  const changedAnswer = 'OIJIOJOIJ'

  it('Happy path 2 - described in TESTING.md', () => {
    // Login page
    cy.visit('http://localhost:3000/');
    // Go to register page
    cy.get('button').contains('Register').click();
    cy.url().should('eq', 'http://localhost:3000/register');

    // Input and submit details
    cy.get('#register-name')
      .focus()
      .type(name2);

    cy.get('#register-email')
      .focus()
      .type(email2);

    cy.get('#register-password')
      .focus()
      .type(password2);

    cy.get('button').contains('Sign Up').click();

    // Automatically goes to dashboard
    cy.url().should('eq', 'http://localhost:3000/dashboard');

    cy.get('#create-quiz')
      .focus()
      .type(quizName2)
      .type('{enter}');

    // Edit quiz
    cy.get('[data-testid=EditIcon]')
      .click();
    
    cy.contains('Upload image').selectFile('nem.png');

    cy.get('img').should('have.attr', 'src').and('not.equal', 'null');

    cy.get('button')
      .contains('edit name', {matchCase: false})
      .click();
    cy.get('[type=text]')
      .first()
      .focus()
      .type(quizNameChanged)
      .type('{enter}');
    cy.get('#quiz-title')
      .contains(quizNameChanged);
    cy.get('[type=text]')
      .last()
      .focus()
      .type(q1)
      .type('{enter}')
      .type(q2)
      .type('{enter}')
      .type(q3)
      .type('{enter}')
      .type(q4)
      .type('{enter}');
    cy.get('#question-card-0')
      .contains(q1);
    cy.get('#question-card-1')
      .contains(q2);
    cy.get('#question-card-2')
      .contains(q3);
    cy.get('#question-card-3')
      .contains(q4)
      .click();

    cy.url().should('include', '/question/3');

    cy.contains('Upload image').selectFile('nem.png');

    cy.get('img').should('have.attr', 'src').and('not.equal', 'null');

    cy.get('button')
      .contains('edit question', {matchCase: false})
      .click();
    cy.get('[type=text]')
      .first()
      .focus()
      .type(url);
    cy.get('#edit-question-name-input')
      .focus()
      .type('{selectAll}{backspace}')
      .type(q4Changed);
    cy.get('#edit-question-time-input-div')
      .find('input')
      .focus()
      .click(120, 70, {force:true});
    cy.get('#edit-question-points-input-div')
      .find('input')
      .focus()
      .click(120, 70, {force:true});
    cy.get(':nth-child(4) > .MuiFormGroup-root > :nth-child(2) > .MuiButtonBase-root > .PrivateSwitchBase-input')
      .click();
    // Adding new answer
    cy.get('.MuiListItem-root > .css-1nrlq1o-MuiFormControl-root > .MuiFormGroup-root > :nth-child(2) > .MuiButtonBase-root > .PrivateSwitchBase-input')
      .click();
    cy.get('#add-answer-3')
      .focus()
      .type(newAnswer)
      .type('{enter}');
    // Editing second default answer
    cy.get('#answer-div-1 > .MuiPaper-root')
      .click();
    cy.get('#edit-answer-div-1 > .MuiPaper-root > .MuiTextField-root > .MuiInputBase-root > #edit-answer-name-input')
      .focus()
      .type(changedAnswer);
    cy.get('#edit-answer-div-1 > .MuiPaper-root > .MuiTextField-root > .MuiInputBase-root > #edit-answer-name-input')
      .focus()
      .type('hi there')
      .type('{enter}');
    // Deleting first answer
    cy.get('#answer-div-0 > .MuiPaper-root > .MuiButtonBase-root')
      .click();
    // Saving question details
    cy.get('#save-question-button')
      .click();
    // Checking question video URL
    cy.get(':nth-child(4) > #question-URL')
      .should('have.attr', 'href').and('equal', url);
    // Checking question title
    cy.get('#question-title')
      .contains(q4Changed);
    // Checking time limit
    cy.get('.MuiGrid-item > :nth-child(2) > [style="display: block;"]')
      .contains(35);
    // Checking points
    cy.get('#question-points')
      .contains(25);
    // Checking changed answer
    cy.get('#answer-div-1 > .MuiPaper-root > :nth-child(1)')
      .contains('hi there');
    // Checking changed answer correctness
    cy.get('#answer-div-1 > .MuiPaper-root > :nth-child(2)')
      .contains('incorrect', {matchCase: false});

    // Click logo to go to dashboard
    cy.get('.MuiToolbar-root > .MuiTypography-root')
      .click();
    cy.url().should('eq', 'http://localhost:3000/dashboard');
    // Start quiz
    cy.get('[data-testid=PlayArrowIcon]')
      .click();

    // Close quiz started notification
    cy.get('[role=alert]')
      .find('button')
      .last()
      .click();

    // Stop quiz
    cy.get('[data-testid=StopIcon]')
      .click();
    // Click on results page button thru notification
    cy.get('[role=alert]')
      .find('button')
      .first()
      .click();

    // Uncomment when res page is done!! Confirming res page url change
    // cy.url().should('eq', 'RESPAGEURL');

    cy.get('.MuiToolbar-root > .MuiButtonBase-root')
      .click();
    
    // Log back in
    cy.get('#login-email')
      .focus()
      .type(email2);

    cy.get('#login-password')
      .focus()
      .type(password2);
    cy.get('button').contains('Login').click();

    // Back on dashboard
    cy.url().should('eq', 'http://localhost:3000/dashboard');

    // Go into Edit quiz
    cy.get('[data-testid=EditIcon]')
      .click();

    cy.get('img').should('have.attr', 'src').and('not.equal', 'null');
    // Checking question names
    cy.get('#question-card-0')
      .contains(q1);
    cy.get('#question-card-1')
      .contains(q2);
    cy.get('#question-card-2')
      .contains(q3);
    cy.get('#question-card-3')
      .contains(q4Changed)
      .click();

    // Checking question video URL
    cy.get(':nth-child(4) > #question-URL')
      .should('have.attr', 'href').and('equal', url);
    // Checking question title
    cy.get('#question-title')
      .contains(q4Changed);
    // Checking time limit
    cy.get('.MuiGrid-item > :nth-child(2) > :nth-child(1)')
      .contains(35);
    // Checking points
    cy.get('#question-points')
      .contains(25);
    // Checking changed answer
    cy.get('#answer-div-1 > .MuiPaper-root > :nth-child(1)')
      .contains('hi there');
    // Checking changed answer correctness
    cy.get('#answer-div-1 > .MuiPaper-root > :nth-child(2)')
      .contains('incorrect', {matchCase: false});
    
    
  })
})