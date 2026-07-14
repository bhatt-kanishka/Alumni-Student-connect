# Intelligent Alumni Connect Platform

A Java Swing based desktop application designed to connect students, alumni, and administrators.

## Features

- Role-based authentication system
- Student dashboard
- Alumni dashboard
- Admin dashboard
- Profile management
- Mentor-student interaction
- MySQL database integration

## Technologies Used

- Java
- Java Swing
- MySQL
- JDBC

## Project Structure
ALUMNI
│
├── src
│ ├── App.java
│ ├── LoginPage.java
│ ├── StudentDashboard.java
│ ├── AlumniDashboard.java
│ ├── AdminDashboard.java
│ ├── ProfilePage.java
│ ├── StudentProfilePage.java
│ ├── AlumniProfile.java
│ └── DBConnection.java
│
├── database
│ └── alumni_project.sql
│
└── README.md

## Database

- MySQL database is used for storing user information.
- JDBC is used for connecting Java application with MySQL.
- Database handles authentication, profiles, and user data.

## How to Run

1. Clone the repository.
2. Import the MySQL database.
3. Update database credentials in `DBConnection.java`.
4. Run `App.java`.