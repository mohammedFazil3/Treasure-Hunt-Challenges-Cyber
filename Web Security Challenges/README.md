# University Management System - CTF Challenge

Welcome to the **University Management System CTF Challenge**! This repository contains the source code for a vulnerable web application designed as part of the **University Campus IT Treasure Hunt Competition (UDST)**. The system simulates a university environment with roles like admin, instructor, and student. Your objective is to identify and exploit the vulnerabilities within the application to capture the four flags.

## Table of Contents
1. [Installation](#installation)
2. [Usage](#usage)
3. [Challenges](#challenges)
4. [Application Structure](#application-structure)
5. [Assets](#assets)
6. [CTF Writeup](#ctf-writeup)

## Installation

To run this application locally, you need Node.js installed. Download it from [Node.js](https://nodejs.org/).

### Step 1: Install Dependencies
Navigate to the project's main directory and run the following commands:

```
npm install cookie-parser
npm install crypto
npm install express
npm install express-handlebars
npm install mysql
npm install mysql2
npm install nodemon
```

### Step 2: Database Setup

#### Create the `mgmt` Database:
```sql
CREATE DATABASE IF NOT EXISTS mgmt;
USE mgmt;

CREATE TABLE IF NOT EXISTS sessiondata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    session_data VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS useraccounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    userType VARCHAR(50) NOT NULL,
    email VARCHAR(100)
);

-- Insert sample data
INSERT INTO useraccounts (username, password, userType) VALUES
('6666', '<hashed-password>', 'admin'),
('1234', '<hashed-password>', 'student');
```

#### Create the `sqli` Database:
```sql
CREATE DATABASE IF NOT EXISTS sqli;
USE sqli;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(50)
);

INSERT INTO users (username, name, role) VALUES
('1234', 'White', 'student'),
('6666', 'Smith', 'admin');
```

### Step 3: Create Database Users:
```sql
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT SELECT, INSERT ON *.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
```

## Usage

Start the application using one of the commands below:

```bash
node htmlpresentation.js
```

or

```bash
nodemon htmlpresentation.js
```

Access the application in your browser at `http://127.0.0.1:8000`.

## Challenges

The application has been intentionally designed with the following vulnerabilities, each leading to one of the four flags:

- SQL Injection (SQLi)
- Privilege Escalation
- Cross-Site Scripting (XSS)
- Broken Access Control

## Application Structure

```
.
├── business.js          # Business logic for the application
├── htmlpresentation.js  # Main server-side code
├── persistence.js       # Database interaction and session management
├── static/              # Static assets (CSS, JS, images)
└── templates/           # Handlebars templates for rendering views
```

### Public Routes
- `/`
- `/aboutUs`
- `/contactUs`

### Admin Routes
- `/admin`
- `/admin/course`

### Student Routes
- `/student`
- `/student/course`

### Instructor Routes
- `/instructor`
- `/instructor/course`

## Assets

### Overall Application Overview
- **Video**: A video walkthrough of the application without revealing solutions is included in the `assets` folder.

### Challenge Solutions
- **Videos**: Four solution videos (one for each challenge) are included in the `assets` folder.

### Writeup
- **PDF**: A detailed CTF writeup document is included in the `assets` folder.

## CTF Writeup

### 1. XSS
The search functionality contains a reflected XSS vulnerability. Use an `<img>` tag with an `onerror` attribute to trigger the vulnerability.

**Payload:**
```html
<img src=x onerror=alert('XSS')>
```

**Assets**: The video solution and a detailed discussion of this vulnerability are available in the `assets` folder.

### 2. Source Code Exposure
Examine the JavaScript files requested while navigating the application. Identify and exploit parameters referenced in the source code to capture the flag.

**Assets**: The video solution and a detailed discussion of this vulnerability are available in the `assets` folder.

### 3. Broken Access Control
Access the `/robots.txt` file to find disallowed paths. Follow the paths to locate sensitive files and decode the hidden flags.

**Assets**: The video solution and a detailed discussion of this vulnerability are available in the `assets` folder.

### 4. Privilege Escalation
Exploit SQL injection vulnerabilities in course-related routes to enumerate database users. Use this information to reset the admin password via hash manipulation and gain admin access.

**Assets**: The video solution and a detailed discussion of this vulnerability are available in the `assets` folder.

---

Good luck, and happy hacking!
