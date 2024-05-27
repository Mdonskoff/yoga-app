describe('Login test e2e', () => {

  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },[])
    .as('session')
  })

  it('should login successfull', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('should login and logout successfull', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('span').contains('Logout').as('btnLogout')
    cy.get('@btnLogout').click()
  })

  it('should login and show account admin', () => {
    cy.intercept('GET','/api/user/1',{
      body : {
        id: 1,
        email: "yoga@studio.com",
        lastName: "admin",
        firstName: "admin",
        admin: true,
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    })
    .as('me')

    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('span').contains('Account').as('btnAccount')

    cy.get('@btnAccount').click()
    cy.get('p').contains('You are admin')
  })

  it('should login and show account user', () => {
    cy.intercept('GET','/api/user/1',{
      body : {
        id: 1,
        email: "test@studio.com",
        lastName: "Mousse",
        firstName: "Minnie",
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    })
    .as('me')

    cy.intercept('delete','/api/user/1',[])
    .as('deleteMe')

    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('span').contains('Account').as('btnAccount')
    cy.get('@btnAccount').click()
    cy.get('button').contains('Detail').as('btnDetail')

    cy.get('@btnDetail').click()
  })
});
