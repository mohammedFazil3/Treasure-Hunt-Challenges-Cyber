const { type } = require('os');
const persistence = require('./persistence')
const crypto = require('crypto')

//Retrieve all available courses.
async function allCourses() {
    return await persistence.getAllCourse()
}

//Retrieve all courses for a given student.
async function getStudentCourses(student_id){
    let courses= await persistence.getStudentCourses(student_id);
    if(courses.length==0){
        return undefined
    }
    return courses
}

// Retrieve all courses for a given instructor.
async function getInstructorCourses(student_id){
    let courses= await persistence.getInstructorCourses(student_id);
    if(courses.length==0){
        return undefined
    }
    return courses
}

// Find a specific course by its code.
async function findCourse(code){
    return await persistence.findCourse(code);
}

// Find a specific course of a student by its code.
async function studentFindCourse(code){
    return await persistence.studentFindCourse(code);
}

//Update the capacity of a course.
async function updateCapacity(code,cap){
    await persistence.updateCapacity(code,cap);
}

// Retrieve all users.
async function getUsers(){
    let getUsers= await persistence.getUsers();
    users=[]
    for(let c of getUsers){
        users.push(c.username);
    }
    return users;
}

//Formats a Date object to MySQL datetime format.
function formatDateForMySQL(date) {
    return date.toISOString().slice(0, 19).replace('T', ' ');
}

//session Mgmt
// Function to Start a new session for a user.
async function startSession(accountType,username){
    let sessionId = crypto.randomUUID();
    let sessionData = {
        sessionKey:sessionId,
        expiry: new Date(Date.now() + 30*60*1000),
        userType:accountType,
        username:username
    }
    await persistence.saveSession(sessionData.sessionKey,formatDateForMySQL(sessionData.expiry),sessionData.userType,sessionData.username)
    return sessionData;
}
// Retrieve session data for a given session key.
async function getSessionData(key) {
    return await persistence.getSessionData(key);
}
// Delete a session by its key.
async function deleteSession(key){
    return await persistence.deleteSession(key);
}

// Check if login credentials are valid.
async function checkLogin(username,password){
    let userDetails = await persistence.getUserDetails(username);
    if(!userDetails){
        return undefined
    }
    let storedPassword = userDetails.password;
    let hash = crypto.createHash('sha256');
    hash.update(password)
    let hashedPassword = hash.digest('hex');
    if (userDetails==undefined || storedPassword != hashedPassword){
        return undefined
    }
    return userDetails.userType;
}
// Update the password for a user.
async function updatePassword(username,password){
    let hash = crypto.createHash('sha256');
    hash.update(password)
    let hashedPassword = hash.digest('hex');
    return await persistence.updatePassword(username,hashedPassword);
}


module.exports = {
    allCourses,
    findCourse,
    updateCapacity,
    startSession,
    checkLogin,
    getSessionData,
    getStudentCourses,
    studentFindCourse,
    getUsers,
    updatePassword,
    getInstructorCourses,
    deleteSession
};