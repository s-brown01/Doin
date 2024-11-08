# Doin
Welcome to Doin!
## Table of Contents
- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Installation Instructions](#installation-instructions)
- [Usage](#usage)

## Project Description
Doin is a social media platform designed for event sharing and social interaction. 
Users can create events, join friends, and share experiences in real-time, fostering community engagement and collaboration.

## Technologies Used
<ul>
  <li>Java</li>
  <li>SpringBoot</li>
  <li>Gradle</li>
  <li>Angular</li>
  <li>JPA</li>
  <li>JWT</li>
  <li>SLF4J</li>
  <li>Logback</li>
  <li>BCrypt Encoder</li>
</ul>

# Installation Instructions
## Prerequisites
<ol>
  <li>Java 17 JDK</li>
  <li>Node.js and npm</li>
  <li>MySQL</li>
  <li>Angular CLI</li>
</ol>

### Installation Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
   Note: Make sure your MySQL is running before starting program and the database is configured properly
2. Add Security Questions
   In our program, we had 3 security questions "pet", "school", "city".
   They need to be added to the register page and the database. 


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
### Starting the front end
1. Navigate to the doin-frontend. EX: `cd doin-frontend`
2. Start Angular
   ```bash
   ng serve
   ```
### In Browser
In your favorite browser, open http://localhost:4200 to see Doin! From there, enjoy the app!
