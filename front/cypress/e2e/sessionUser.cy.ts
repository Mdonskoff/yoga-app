describe('Session user test e2e', () => {
  const mockDateSession = '2024-05-21'
  const mockTeacher = [
    {
      id: 1,
      lastName: "Wayne",
      firstName: "Bruce",
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      lastName: "Prince",
      firstName: "Diana",
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ];

  const mockSession = [{
    id: 1,
    name: "Zen Meditation",
    description: "Session de meditation zen",
    date: mockDateSession,
    teacher_id: 1,
    users: [0],
    createdAt: mockDateSession,
    updatedAt: mockDateSession,
  }]

  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })
  })

  beforeEach(() => {

    cy.intercept('POST', '/api/session/1/participate/1', {
      body: 'Participation successful',
      statusCode: 200,
    }).as('participationRequest');
  })

  beforeEach(() => {
    cy.intercept('GET', 'api/teacher', {
      body: mockTeacher,
    })

    cy.intercept('GET', 'api/teacher/1', {
      body: mockTeacher[0],
    })
  })


  it('Should login as user and participate to a session', () => {

    cy.intercept('GET', 'api/session/1', {
      body: mockSession[0],
    }).as('firstSession')

    cy.intercept('GET', 'api/session', {
      body: mockSession,
    })

    cy.visit('/login')

    cy.get('input[formControlName=email]').type("user@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.get('button').contains('Detail').click()


    cy.intercept('GET', 'api/session/1', ((req) => {
      mockSession[0].users[0] = 1
      req.body =  mockSession[0]
    }))

    cy.url().should('include', '/sessions/detail/1')


    cy.get('button').contains('Participate').click()

  })

  it('Should login as user unparticipate to a session', () => {
    const mockSessionUnparticipate = [{
      id: 1,
      name: "Zen Meditation",
      description: "Session de meditation zen",
      date: mockDateSession,
      teacher_id: 1,
      users: [1],
      createdAt: mockDateSession,
      updatedAt: mockDateSession,
    }]

    cy.intercept('GET', 'api/session/1', {
      body: mockSession[0],
    }).as('firstSession')

    cy.intercept('GET', 'api/session', {
      body: mockSession,
    })

    cy.visit('/login')

    cy.get('input[formControlName=email]').type("user@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.intercept('GET', 'api/session/1', {
      body : mockSessionUnparticipate[0]
    })

    cy.get('button').contains('Detail').click()




    cy.url().should('include', '/sessions/detail/1')


    cy.get('button').contains('Do not participate').click()

  })

})
