# Doin

Welcome to Doin! This is a social media app made to help people connect with new friends and go on events together!

## Table of Contents
- [Project Description](#project-description)
    - [Technologies Used](#technologies-used)
        - [Backend](#backend)
        - [Frontend](#frontend)
        - [Dev Tools](#dev-tools)
    - [Challenges](#challenges)
    - [Future Features](#future-features)
- [Installation Instructions](#installation-instructions)
    - [Prerequisites](#prerequisites)
    - [Installation Steps](#installation-steps)
        - [Cloning Repository](#cloning-repository)
        - [Java 21 Installation](#java-21-installation)
        - [MySQL Setup Instructions](#mysql-setup-instructions)
            - [Start MySQL](#1-start-mysql)
            - [Create the Application Database](#2-create-the-application-database)
            - [Create a New MySQL User](#3-create-a-new-mysql-user)
            - [Test the New User](#5-test-the-new-user)
            - [Insert Security Questions](#6-inserting-security-questions)
        - [Angular Setup](#angular-setup)
          - [Install Node.js and npm](#1-install-nodejs-and-npm)
          - [Install Angular CLI](#2-install-angular-cli)
          - [Verify Angular CLI Install](#3-verify-angular-cli-installation)
          - [Install Dependencies](#4-install-project-dependencies)
- [Usage](#usage)
    - [Starting the Backend](#starting-the-backend)
    - [Starting the Frontend](#starting-the-frontend)
    - [In Browser](#in-browser)
- [Tests](#tests)
- [Credits](#credits)
- [How to Contribute](#how-to-contribute)
- [License](#license)


## Project Description
Our goal when creating this site was to help users connect with friends and go on 
outdoor activities together. It allows users to create, join, and post photos to past events. 

Doin is a social media platform designed for event sharing and social interaction.
Users can create events, join friends, and share experiences in real-time,
fostering community engagement and collaboration.

Our program was created using Java 21, Gradle, Angular, JPA, JWT, SLF4J, Logback, and MySQL.


## Challenges
We had some various challenges while building this site. One of the biggest ones was learning how to use the many 
different coding languages in coordination with each other, especially Angular and Spring Boot. 

## Future Features
We were able to implement most of the features we wanted, but there are a few more we would have added if we 
had more time. Those features are ...

## Technologies Used

### Backend
- **Java**: Primary programming language for backend logic
  - We used Java 21.0.4
- **Spring Boot**: Framework for building Java-based applications
  - We used version 3.3.3 of SpringBoot
- **JPA**: For object-relational mapping
- **JWT**: Token-based authentication
  - com.auth0:java-jwt:4.4.0
- **BCrypt Encoder**: For password hashing
  - org.springframework.boot:spring-boot-starter-security
  - org.springframework.boot:spring-boot-starter-oauth2-resource-server
- **MySQL**: Relational database
  - Server version: 8.0.39 MySQL Community Server - GPL

### Frontend
- **Angular**: Framework for building a dynamic web frontend
  - Angular CLI: 18.2.2
- **Node.js** 
  - Node: 20.10.0
- **NPM**
  - Package Manager: npm 10.8.3

### Dev Tools
- **Gradle**: Build automation tool
  - Version 3.3.3
- **SLF4J & Logback**: Logging
  - org.slf4j:slf4j-api:2.0.0
  - ch.qos.logback:logback-classic:1.4.12

# Installation Instructions
## Prerequisites
<ol>
  <li>Java 21 JDK</li>
  <li>Node.js and npm</li>
  <li>MySQL</li>
  <li>Angular CLI</li>
</ol>

## Installation Steps
Your operating system (Windows/MacOS/Linux) should not affect how Doin runs, but may affect setting up 
and installing the different tools.

### Cloning Repository
1. Clone the repository:
   ```bash
   git clone https://github.com/s-brown01/Doin.git
   ```
### Java 21 Installation
1. Install Java 21
   - We used Java 21.0.4 for our program, but it should work with any newer edition.
   ```bash
   java 21.0.4 2024-07-16 LTS
   Java(TM) SE Runtime Environment (build 21.0.4+8-LTS-274)
   Java HotSpot(TM) 64-Bit Server VM (build 21.0.4+8-LTS-274, mixed mode, sharing)
    ```
    - To download Java, visit:
      - <a href="https://www.java.com/en/download/help/download_options.html">Oracle Java Installation Guide</a> for instructions on how to download it
      - <a href="https://www.oracle.com/java/technologies/javase-downloads.html">Oracle Java Downloads</a> for the files to install

### MySQL Setup Instructions

**Database Credentials:** Our database is named `doin_db` with a user of `doinapp`, whose password is `pass`. In order 
to change any of these credentials, make sure that you change it when following the code below and change it 
in the `doin-backend/src/main/resources/application.properties`
```properties
# database name ("doin_db") and port to listen to (3306)
spring.datasource.url=jdbc:mysql://localhost:3306/doin_db
# user's username ("doinapp")
spring.datasource.username=doinapp
# user's password ("pass")
spring.datasource.password=pass
```

#### 0. Installing MySQL

We used **Server version: 8.0.39 MySQL Community Server**, but it should work with any later version.

For instructions on how to install MySQL and set up a root user, we found 
<a href="https://www.mysqltutorial.org/getting-started-with-mysql/install-mysql/"> MySqlTutorial </a> 
and <a href="https://www.w3schools.in/mysql/setup/"> W3Schools </a> very useful.

Follow these steps to start MySQL, create a database, and configure a new user for the application:

#### 1. **Start MySQL**
- **On Windows**:
    1. Open the Command Prompt or PowerShell.
    2. Start the MySQL server:
       ```bash
       net start mysql
       ```
    3. Log in to MySQL:
       ```bash
       mysql -u root -p
       ```
       Enter the root password when prompted.

- **On macOS/Linux**:
    1. Open a terminal.
    2. Start the MySQL server (if not already running):
       ```bash
       sudo service mysql start
       ```
    3. Log in to MySQL:
       ```bash
       mysql -u root -p
       ```
       Enter the root password when prompted.

#### 2. **Create the Application Database**
1. After logging into the MySQL shell, create a new database:
   ```MySQL
   CREATE DATABASE doin_db;
   ```
2. Verify the database was created:
   ```MySQL
   SHOW DATABASES;
   ```

#### 3. **Create a New MySQL User**
1. Create a new user with a username and password for the application:
   ```MySQL
   CREATE USER 'doinapp'@'localhost' IDENTIFIED BY 'pass';
   ```
2. Grant privileges to the new user for the `doin_db` database:
   ```sql
   GRANT ALL PRIVILEGES ON doin_db.* TO 'doinapp'@'localhost';
   ```
3. Apply the changes:
   ```MySQL
   FLUSH PRIVILEGES;
   ```

#### 4. **Exit MySQL**
- Exit the MySQL shell:
  ```MySQL
  EXIT;
  ```

#### 5. **Test the New User**
- Log in to MySQL using the new user credentials:
  ```bash
  mysql -u doinapp -p
  ```
  Enter the password (`pass`) when prompted.
- Switch to the `doin_db` database to ensure everything is set up correctly:
  ```MySQL
  USE doin_db;
  ```

#### 6. Inserting Security Questions

We used 3 different security questions to give the user a way to reset their password and verify their identity. Their
values are "pet", "school", and "city". These 3 values need to be added to the database and the frontend's register 
HTML page (`doin-frontend\src\app\register\register.component.html`). The values must match exactly on both the 
HTML and in the database in order for the app to function correctly.

**IMPORTANT:** Before inserting data into the tables, the tables must first be created.

There are 2 ways to create the table:

- **Option 1: Automatically generate the tables with Gradle**  
  Start Gradle (See [Starting the Backend](#starting-the-backend)). All tables (including the `security_questions` 
  table) will be automatically generated. The questions will *not* be automatically added into the database.
  - The easiest way to insert the data is using following **Insert the 3 Security Questions** below. 

- **Option 2: Manually create the SecurityQuestion table and insert the data**  
  If you choose to create the table manually, use the following SQL commands:

    1. **Create the `security_questions` table:**

        ```mysql
        CREATE TABLE security_questions (
            id INT AUTO_INCREMENT PRIMARY KEY,
            question VARCHAR(255) NOT NULL
        );
        ```

    2. **Insert the 3 Security Questions:**

        ```mysql
        INSERT INTO security_questions (question) VALUES ('pet');
        INSERT INTO security_questions (question) VALUES ('school');
        INSERT INTO security_questions (question) VALUES ('city');
        ```

  To run the SQL commands:

    1. Open your terminal or command prompt.
    2. Log into MySQL:
       ```bash
       mysql -u doinapp -p
       ```
       Enter your password when prompted (`pass`).
    3. Select the `doin_db` database:
       ```mysql
       USE doin_db;
       ```
    4. Run the SQL commands one by one. You can copy-paste the commands into the terminal.

After executing these commands, the `security_questions` table will have the three required questions, and 
the application will work correctly for user registration and password recovery.

To change or add security questions: edit the insert statements and edit the HTML page.

**Reminder**: After setting up MySQL, make sure that the database credentials in `doin-backend/src/main/resources/application.properties` match those you set up in MySQL.


### Summary of Commands
Use this quick reference for setup:
```bash
# Start MySQL
mysql -u root -p

# Inside the MySQL shell
CREATE DATABASE doin_db;
CREATE USER 'doinapp'@'localhost' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON doin_db.* TO 'doinapp'@'localhost';
FLUSH PRIVILEGES;
EXIT;

# Test the new user
mysql -u doinapp -p
USE doin_db;

# OPTIONAL: to create the security_question table
CREATE TABLE security_questions (
            id INT AUTO_INCREMENT PRIMARY KEY,
            question VARCHAR(255) NOT NULL
);
  
# Inserting the data
INSERT INTO security_questions (question) VALUES ('pet');
INSERT INTO security_questions (question) VALUES ('school');
INSERT INTO security_questions (question) VALUES ('city');
````

### Angular Setup

#### 1. Install Node.js and npm
Angular requires **Node.js** and **npm** (Node Package Manager) for dependency management and running the Angular CLI.

- **Download Node.js and npm** from the official website: <a href="https://nodejs.org/"> Node.js </a>
- After installation, verify that both Node.js and npm are installed correctly by running:
  ```bash
  node -v
  npm -v
  ```
    - If you encounter issues with Node.js or npm installation, check the official Node.js installation guide at 
    the <a href="https://nodejs.org/en/download/package-manager/"> Node.js Website</a>.

  
#### 2. Install Angular CLI
Angular CLI (Command Line Interface) helps you create and manage Angular applications. To install Angular CLI
globally on your machine, run:
```bash
npm install -g @angular/cli
```

#### 3. Verify Angular CLI Installation
To check if Angular CLI was installed correctly, run:
```bash
ng version
```

#### 4. Install Project Dependencies
Once you've cloned the repository, navigate to the frontend directory (`doin-frontend`) and 
install the necessary npm dependencies:
```bash
cd doin-frontend
npm install
```
- This installs all the required dependencies listed in the `package.json` file 
(located in the `doin-frontend` directory).

## Usage
### Starting the backend
1. Open a command line
2. Change directories to doin-backend. EX: `cd doin-backend`
3. Start Gradle <BR>
    ```bash
   # If you don't have Gradle installed globally, use the wrapper (gradlew)
   ./gradle bootRun # For MacOS and Linux
   gradlew bootRun  # For windows (CMD)
    ```
   - A functioning output will look similar to:
       ```bash
       <==========---> 80% EXECUTING [10s]`. In order for `bootRun
       ```
     - To be functioning correctly, it should be ongoing and running uninterrupted.
- If gradle outputs something similar to: `"BUILD SUCCESSFUL in 14s 4 actionable tasks: 3 executed, 1 up-to-date `
  make sure that the MySQL database is running. 
  - See [above](#1-start-mysql) for how to start MySQL and check if the `doin_db` is running.
- If gradle builds unsuccessfully (likely something wrong with the backend's code), you will see an output similar to 
  `BUILD FAILED in 3s`

### Starting the frontend
1. Navigate to the doin-frontend. EX: `cd doin-frontend`
2. Start Angular
   ```bash
   ng serve
   ```
- A successful build will look like 
```bash
  Application bundle generation complete. [3.352 seconds] Watch mode enabled.
  Watching for file changes...
  ➜  Local:   http://localhost:4200/
```
### In Browser
**Important:** Both the backend and frontend must be running for the application to function properly.

In your favorite browser, open http://localhost:4200 to see Doin! From there, enjoy the app!
- If the app doesn't load, make sure the frontend and backend are be running!

# How To Contribute
You are welcome to contribute! Please submit a pull request and credit our project!

# License
MIT License

Copyright (c) 2024 Sean-Paul Brown and Levani Pilpani

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
