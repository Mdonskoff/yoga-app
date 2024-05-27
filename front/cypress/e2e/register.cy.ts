describe('Register test e2e', () => {

  it('should register a user', () => {
    cy.visit('/register')
    cy.intercept('POST', 'api/auth/register', [])

    cy.get('input[formControlName="firstName"]').type('Donald')
    cy.get('input[formControlName="lastName"]').type('Duck')
    cy.get('input[formControlName="email"]').type('dodu@test.com')
    cy.get('input[formControlName="password"]').type('test!123')

    cy.get('button[type="submit"').click()
  })

  it('should not register a user', () => {
    cy.visit('/register')

    cy.get('input[formControlName="firstName"]').type('Donald')
    cy.get('input[formControlName="lastName"]').type('Duck')
    cy.get('input[formControlName="email"]').type('dodu@test.com')
    cy.get('input[formControlName="password"]').type('test!123')

    cy.get('button[type="submit"').click()

    cy.get('span').contains('An error occurred')
  })

})
