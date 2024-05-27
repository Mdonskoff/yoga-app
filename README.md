# Yoga-app

## Context
I have to produce the tests for this application to ensure the code quality. 
Coverage must be 80% for back-end, front-end and end-to-end tests.

## Technology
This project was generated with Angular CLI version 14.1.0 and Java 1.8

## Software and Tools
- IDE (IntelliJ and/or VsCode)
- Mysql (MySql workbench) or MySql databse installed
  
## Follow the procedure step by step
1. Clone the project and run the command `npm install` inside yoga-app/front folder
2. Connect to MySql Workbench, create a user with username `user` and password `123456` and give him the necessary rights
3. Log in with your user name and password
4. Create a schema with the name `test`
5. Import and execute the sql script  `script.sql`  in the `yoga-app/ressources/sql` folder to create users, teachers, session and participate tables
6. Open the back folder with intelliJ
7. Run the back application with Intellij
8. Open front folder in VsCode for example or IntelliJ in a different window
9. Run the front application with `ng serve`
10. Testing the application with a default account

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

## Information
- You can create another user with a different password. You'll need to modify the credentials in the application.properties file.
Change the line `spring.datasource.username=user` and `spring.datasource.password=123456`.
- You can change the schema name in the same file
  `spring.datasource.url=jdbc:mysql://localhost:3306/<CHANGE NAME HERE>?allowPublicKeyRetrieval=true`

## WARNING
Install the application correctly before running tests

## Test Front end
Go on main/front/README.md

## Test Back end
Go on main/back/README.md

## Author
Marushka LABORDE-DONSKOFF
