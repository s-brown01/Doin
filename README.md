# Doin

Welcome to Doin! This is a social media app made to help people connect with new friends and go on events together!

## Table of Contents
- [Project Description](#project-description)
   - [Technologies Used](#technologies-used)
     - [Backend](#backend)
     - [Frontend](#frontend)
     - [Dev-Tools](#dev-tools)
   - [Challenges](#challenges)
   - [Future Features](#future-features)
- [Installation Instructions](#installation-instructions)
   - [Prerequisites](#prerequisites)
   - [Installation Steps](#installation-steps)
   - [Usage](#usage)
     - [Backend](#starting-the-backend)
     - [Frontend](#starting-the-frontend)
     - [In Browser](#in-browser)
- [Tests](#tests)
- [Credits](#credits)
- [How to contribute](#how-to-contribute)
- [Licence](#licence)

## Project Description
Our goal when creating this site was to help users connect with friends and go on 
outdoor activities together. It allows users to create, join, and post photos to past events. 

Doin is a social media platform designed for event sharing and social interaction.
Users can create events, join friends, and share experiences in real-time,
fostering community engagement and collaboration.

Our program was created using Java 21, Gradle, Angular, JPA, JWT, SLF4J, Logback, and MySQL.


## Challenges
Some of the challenges that we faced were...

## Future Features
We were able to implement most of the features we wanted, but there are a few more we would have added if we 
had more time. Those features are ...

## Technologies Used

### Backend
- **Java 21**: Primary programming language for backend logic
- **Spring Boot**: Framework for building Java-based applications
- **JPA**: For object-relational mapping
- **JWT**: Token-based authentication
- **BCrypt Encoder**: For password hashing
- **MySQL**: Relational database

### Frontend
- **Angular**: Framework for building a dynamic web frontend

### Dev Tools
- **Gradle**: Build automation tool
- **SLF4J & Logback**: Logging


# Installation Instructions
## Prerequisites
<ol>
  <li>Java 21 JDK</li>
  <li>Node.js and npm</li>
  <li>MySQL</li>
  <li>Angular CLI</li>
</ol>

### Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/s-brown01/Doin.git
   ```
   <small>
      Note: Make sure your MySQL is running before starting program and the database is configured properly. <BR>
      For more information on how to start MySQL visit the official page: 
        <a href="https://dev.mysql.com/doc/mysql-getting-started/en/">here</a>.
   </small>
2. Database Credentials
   - Database name: Doin_db
   - Username: doinapp
   - Password: pass
3. Add Security Questions <BR>
   ```bash
   INSERT INTO security_questions(1, "pet");
   INSERT INTO security_questions(2, "school");
   INSERT INTO security_questions(3, "city"); 
   ```
   <small>
   In our program, we had 3 security questions "pet", "school", "city". <BR>
   They need to be added to the register page and the database. 
   </small>

## Usage
### Starting the backend
1. Change directories to doin-backend. EX: `cd doin-backend`
2. Start Gradle
   For Macs:
   ```bash
   ./gradle bootRun
   ```
  For Windows:
  ```bash
  gradlew bootRun
  ```
### Starting the frontend
1. Navigate to the doin-frontend. EX: `cd doin-frontend`
2. Start Angular
   ```bash
   ng serve
   ```
### In Browser
In your favorite browser, open http://localhost:4200 to see Doin! From there, enjoy the app!

# Tests


# Credits


# How To Contribute


# Licence