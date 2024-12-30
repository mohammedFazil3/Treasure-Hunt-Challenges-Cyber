const mysql = require('mysql2/promise');
const vulnMysql = require('mysql');
const util = require('util');

//function to securely connect to mysql database
async function connectDatabase(){
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'admin',
        password: 'admin'
    });    

    return connection;
}

//vulnerable connection to mysql database for sqli 
function vulnconnectDatabase(){
    const connection = vulnMysql.createConnection({
        host: 'localhost',
        user: 'user',
        password: '12user34',
        database: 'sqli'
    });

    connection.query = util.promisify(connection.query);

    return connection;
}

//session mgmt
//saves session to the database
async function saveSession(sessionKey, expiry, userType, username) {
    const connection = await connectDatabase();
    const query = "INSERT INTO mgmt.sessionData (sessionKey, expiry, userType, username) VALUES (?, ?, ?, ?)";
    const regex = /^[a-zA-Z0-9]+$/;
    let values;

    if (regex.test(userType) && regex.test(username)) {
        values = [sessionKey, expiry, userType, username];
    } else {
        values = [sessionKey, expiry, "", ""];
    }

    try {
        await connection.query('USE mgmt');
        await connection.query(query, values);
        console.log("Session saved successfully!");
    } catch (error) {
        console.error("Error saving session:", error);
    } finally {
        await connection.end();
    }
}
//function to delete a session
async function deleteSession(key){
    const connection = await connectDatabase();
    
    try {
        const query = "DELETE FROM mgmt.sessionData WHERE sessionKey = ?";
        
        await connection.query('USE mgmt');
        const [result] = await connection.query(query, [key]);

        if (result.affectedRows > 0) {
            console.log("Session deleted successfully!");
        } else {
            console.log("No session found with the provided sessionKey.");
        }
    } catch (error) {
        console.error("Error deleting session:", error);
    } finally {
        await connection.end();
    }    
}
//Retrieves session data for a given session key
async function getSessionData(key) {
    const connection = await connectDatabase();

    try {
        await connection.query('USE mgmt');
        const query = 'SELECT sessionKey, expiry, userType, username FROM sessionData WHERE sessionKey = ?';
        const [rows] = await connection.execute(query, [key]);

        if (rows.length > 0) {
            return rows[0]; 
        } else {
            return null; 
        }
    } catch (err) {
        console.error('Database query error:', err);
        return null;
    } finally {
        await connection.end(); 
    }
}

//Retrieves all user from the users table - sqli database
async function getUsers(){
    const connection = await connectDatabase();

    try {
        await connection.query('USE sqli');
        const query = 'SELECT username FROM users';
        const [rows] = await connection.execute(query);
        if (rows.length > 0) {
            return rows; 
        } else {
            return null; 
        }
    } catch (err) {
        console.error('Database query error:', err);
        return null;
    } finally {
        await connection.end();
    }    
}

// Retrieve user details by username - from userAccounts table in mgmt
async function getUserDetails(userName) {
    const connection = await connectDatabase();

    try {
        await connection.query('USE mgmt');
        const query = 'SELECT * FROM userAccounts WHERE username = ?';
        const [rows] = await connection.execute(query, [userName]);
        if (rows.length > 0) {
            return rows[0]; 
        } else {
            return null; 
        }
    } catch (err) {
        console.error('Database query error:', err);
        return null;
    } finally {
        await connection.end();
    }
}

//function to update password of a user in userAccounts table in mgmt database
async function updatePassword(username, password) {
    const connection = await connectDatabase();
    let boolean;
    try {
        await connection.query('USE mgmt');

        const query = 'UPDATE userAccounts SET password = ? WHERE username = ?';
        const [result] = await connection.execute(query, [password, username]);

        if (result.affectedRows > 0) {
            console.log(`Password updated successfully for username: ${username}`);
            boolean=true;
        } else {
            console.log(`No user found with username: ${username}`);
            boolean=false;
        }
    } catch (err) {
        console.error('Database query error:', err);
        boolean=false;
    } finally {
        await connection.end();
        return boolean;
    }
}

//Retrieve all courses from the table courses -sqli database
async function getAllCourse() {
    const connection = await connectDatabase();
    try {
        await connection.query('USE sqli');
        const [courses] = await connection.execute('SELECT * FROM courses');
        await connection.end();
        console.log('Connection closed');
        return courses;
    } catch (err) {
        await connection.end();
        console.log('Connection closed');
        console.error(err);
    }
}
 
//Retrieve a specific course by its code from the table courses -sqli database
async function findCourse(code) {
    const connection = await connectDatabase();
    try {
        await connection.query('USE SQLi');
        const query = 'SELECT * FROM courses WHERE code = ?';
        const [results] = await connection.execute(query, [code]);
        console.log('Connection closed');
        await connection.end();
        return results;
    } catch (err) {
        await connection.end();
        console.error('Connection error:', err);
    }
}

//Retrieve a specific course for a student by its code (vulnerable to SQL injection)
async function studentFindCourse(code){
    const connection = vulnconnectDatabase(); 
    try {
        const query = `SELECT * FROM courses WHERE code = '${code}'`;  // Vulnerable to SQL injection
        const results = await connection.query(query); 
        console.log('Connection closed');
        await connection.end();
        return results;
    } catch (err) {
        connection.end();
        console.error('Connection error:', err);
    }
}

// Retrieve all courses for a specific student
async function getStudentCourses(student_id){
    const connection = await connectDatabase();
    try {
        await connection.query('USE sqli');
        const [students] = await connection.execute(
            'SELECT c.* FROM courses c JOIN enrollments e ON c.code = e.course_code WHERE e.student_id = ?',
            [student_id]
        )
        await connection.end();
        console.log('Connection closed');
        return students;       
    }catch (err) {
        await connection.end();
        console.log('Connection closed');
        console.error(err);
    }
}

// Retrieve all courses for a specific instructor
async function getInstructorCourses(username){
    const connection = await connectDatabase();
    try {
        await connection.query('USE sqli');
        const [instructors] = await connection.execute(
            'SELECT c.* FROM courses c JOIN users u ON c.instructor = u.name WHERE u.username = ?',
            [username]
        )
        await connection.end();
        console.log('Connection closed');
        return instructors;       
    }catch (err) {
        await connection.end();
        console.log('Connection closed');
        console.error(err);
    }
}

// Update the capacity of a course
async function updateCapacity(code, cap) {
    const connection = await connectDatabase();
    try {
        await connection.query('USE sqli');
        const [result] = await connection.execute(
            'UPDATE courses SET capacity = ? WHERE code = ?',
            [cap, code]
        );

        await connection.end();
        console.log('Connection closed');
        return result;
    } catch (err) {
        await connection.end();
        console.log('Connection closed');
        console.error(err);
    }
}

module.exports = {
    getAllCourse,
    findCourse,
    updateCapacity,
    getUserDetails,
    saveSession,
    getSessionData,
    getStudentCourses,
    studentFindCourse,
    getUsers,
    updatePassword,
    getInstructorCourses,
    deleteSession
};
