
# University Management System - CTF Challenge

This repository contains the source code for a vulnerable University Management System web application developed as part of the University Campus IT Treasure Hunt Competition (UDST). The system simulates a university environment with different user roles, including admin, instructor, and student. The primary objective of this challenge is to identify and exploit vulnerabilities within the application.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Challenges](#challenges)
- [Application Structure](#application-structure)

## Installation

To run this application locally, you need to have Node.js installed. You can download Node.js from the following link:

[Download Node.js](https://nodejs.org/en/download/prebuilt-installer)

After installing Node.js, you need to install the necessary dependencies. Navigate to the main directory of this project and run:

```bash
npm install cookie-parser
npm install crypto
npm install express
npm install express-handlebars
npm install mysql
npm install mysql2
npm install nodemon
```

Make sure to install these using npm install in the terminal.

## Database & Table Setup

To set up the necessary databases and tables for this application, follow the steps below:

### 1. Create the `mgmt` Database and Its Tables

```sql
CREATE DATABASE IF NOT EXISTS mgmt;
USE mgmt;

-- Create the sessiondata table
CREATE TABLE IF NOT EXISTS sessiondata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    session_data VARCHAR(255) NOT NULL
);

-- Create the useraccounts table
CREATE TABLE IF NOT EXISTS useraccounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    userType VARCHAR(50) NOT NULL,
    email VARCHAR(100)
);

-- Insert sample data into useraccounts(hashed passwords)
INSERT INTO useraccounts (username, password, userType) VALUES
('6666', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'admin'),
('9164', '97c2d427769569994e2c3d568f6bb0a5d883a9b3b4e34da65a0c961d401641c', 'instructor'),
('9545', 'a87db2405d74cde842b4365731a902d4a8f920ab97785ba30ae8fe7f9efb5bf', 'instructor'),
('9854', 'fbee7c76e2a031abb20a4d945ba377fec20fb3f5c2e4f986f6b381161520ee4', 'instructor'),
('9991', '65f20ec2c193b909ab4407ad7d2ed2451b0ab0b90c85629fb5e9417749f4ab9f', 'instructor'),
('1234', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'student'),
('7875', '4fc87527e14ebc12c24d2cdb77be2eae928da94e845630887cfa377fa6abd429', 'student'),
('8901', '0c8fb74cb6304946f1120efc836f3c27ad4e96f7a6b1d86191397e8cf2116a08', 'student');
```

### 2. Create the `sqli` Database and Its Tables

```sql
CREATE DATABASE IF NOT EXISTS sqli;
USE sqli;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(50)
);

-- Insert sample data into users
INSERT INTO users (username, name, role) VALUES
('1234', 'White', 'student'),
('7875', 'Heisenberg', 'student'),
('8901', 'Gus', 'student'),
('9164', 'John', 'instructor'),
('9545', 'Salah', 'instructor'),
('9854', 'Maryam', 'instructor'),
('9991', 'David', 'instructor'),
('6666', 'Smith', 'admin');

-- Create the courses table
CREATE TABLE IF NOT EXISTS courses (
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255),
    capacity INT,
    instructor VARCHAR(50)
);

-- Insert sample data into courses
INSERT INTO courses (code, name, capacity, instructor) VALUES
('DACS2201', 'Intro to Cyber Security', 5, 'John'),
('DACS3101', 'Applied Cryptography', 8, 'Salah'),
('INFS1101', 'Introduction to Computing', 7, 'David'),
('INFS1201', 'Computer Programming', 6, 'David'),
('INFS2101', 'Web Technologies 1', 5, 'Maryam'),
('INFS2201', 'Database Management Systems', 7, 'John'),
('INFS3201', 'Web Technologies II', 4, 'Maryam'),
('INFT2101', 'Networking I', 5, 'David');

-- Create the enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(50),
    student_id VARCHAR(50)
);

-- Insert sample data into enrollments
INSERT INTO enrollments (course_code, student_id) VALUES
('DACS2201', '1234'),
('INFS2201', '7875'),
('INFS2201', '8901'),
('INFS3201', '7875'),
('INFS3201', '8901'),
('INFT2101', '7875'),
('INFT2101', '1234'),
('DACS3101', '1234');
```
### 3. Creating User Accounts for the Database with Necessary Privileges
```sql
-- Create the admin user with the specified global privileges
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';

-- Grant global privileges to the admin user
GRANT SELECT, INSERT ON *.* TO 'admin'@'localhost';

-- Grant specific database privileges to the admin user on the 'mgmt' database
GRANT USAGE ON mgmt.* TO 'admin'@'localhost';

-- Grant table-specific privileges to the admin user on the 'mgmt' database
GRANT DELETE ON mgmt.sessiondata TO 'admin'@'localhost';
GRANT USAGE ON mgmt.useraccounts TO 'admin'@'localhost';

-- Create the user 'user' with the specified privileges
CREATE USER 'user'@'localhost' IDENTIFIED BY '12user34';

-- Grant database-specific privileges to the user on the 'sqli' database
GRANT SELECT ON sqli.* TO 'user'@'localhost';

-- Apply the changes
FLUSH PRIVILEGES;
```

## Usage

To start the application, you can use either of the following commands:

```bash
node htmlpresentation.js
```

or

```bash
nodemon htmlpresentation.js
```

Once the server is running, you can access the application by visiting \`127.0.0.1:8000\` in your web browser.

## Challenges

This University Management System has been designed with several vulnerabilities to be exploited during the CTF challenge. The vulnerabilities present in the system include:

- **SQL Injection (SQLi)**
- **Privilege Escalation**
- **Cross-Site Scripting (XSS)**
- **Broken Access Control**
- **Business Logic Flaw**
- **Excessive Trust in Client-Side Controls**
- **Source Code Exposure**

Participants are required to identify and exploit these vulnerabilities to capture the flags hidden within the system.

## Application Structure

The project structure is organized as follows:

    . 
    ├── business.js # Business logic for the application 
    ├── htmlpresentation.js # Main server-side code for handling routes and views 
    ├── persistence.js # Database interaction and session management 
    ├── static/ # Static assets like CSS, JS, and images 
    └── templates/ # Handlebars templates for rendering views

### Public Routes
- \`/\`
- \`/aboutUs\`
- \`/FAQS\`
- \`/privacyPolicy\`
- \`/safety\`
- \`/contactUs\`

### Admin Routes
- \`/admin\`
- \`/admin/course\`
- \`/admin/course/:codeName\`

### Student Routes
- \`/student\`
- \`/student/course\`
- \`/student/course/:codeName\`
- \`/student/changePassword\`

### Instructor Routes
- \`/instructor\`
- \`/instructor/course\`
- \`/instructor/course/:codeName\`
- \`/instructor/changePassword\`
