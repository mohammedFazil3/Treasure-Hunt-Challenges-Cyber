const business = require('./business');
const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const handlebars = require('express-handlebars');
const crypto = require('crypto');

//express app setup
let app = express();
app.use(bodyParser.urlencoded());
app.use(cookieParser())


//Handlebars configuration + ifEquals helper to compare two arguments are equal or not
const hbs = handlebars.create({
    helpers: {
        ifEquals: function(arg1, arg2, options) {
            return (arg1 == arg2) ? options.fn(this) : options.inverse(this);
        }
    }
});

// Setting up the view engine with Handlebars
app.set('views',__dirname+'/templates');
app.set('view engine', 'handlebars');
app.engine('handlebars', hbs.engine);

//Static file serving
app.use("/static",express.static(__dirname+"/static"));
app.use('/robots.txt', express.static(__dirname+ '/robots.txt'));

//Routes

//Public Facing Routes
app.get("/",(req,res)=>{
    res.render("landing",{layout:'main2',title:'Home'});
})

app.get('/aboutUs',(req,res)=>{
    res.render('aboutUs',{
        title:'About Us',
        layout:'main2'
    })
})

app.get('/FAQS',(req,res)=>{
    res.render('FAQS',{
        title:'FAQS',
        layout:'main2'
    })
})

app.get('/privacyPolicy',(req,res)=>{
    res.render('privacyPolicy',{
        title:'Privacy Policy',
        layout:'main2'
    })
})

app.get('/safety',(req,res)=>{
    res.render('safety',{
        title:'Safety',
        layout:'main2'
    })
})

app.get('/contactUs',(req,res)=>{
    res.render('contactUs',{
        title:'Contact Us',
        layout:'main2'
    })
})

//Login & session mgmt
app.get('/login',async(req,res)=>{
    let msg = req.query.msg;
    let error = "";
    if(msg=="Invalid Credentials"){
        error = msg;
    }else if(msg=="Session Timed Out"){
        error=msg;
    }
    res.render('login',{layout:undefined,error:error})
})

app.post('/login',async(req,res)=>{
    let username=req.body.username;
    let password=req.body.password;

    let accountType = await business.checkLogin(username,password);

    if (!accountType){
        let sessionData =await business.startSession("","");

        res.redirect("/login?msg=Invalid Credentials");
        return        
    }else{
        let sessionData = await business.startSession(accountType,username)
        res.cookie('ctfKey',sessionData.sessionKey,{expires:sessionData.expiry})
        if (sessionData.userType=='admin'){
            res.redirect('/admin')
        }else if (sessionData.userType=='student'){
            res.redirect(`/student`)
        }else if (sessionData.userType=='instructor'){
            res.redirect('/instructor')
        }else{
            let sessionData =await business.startSession("","");
            res.cookie('ctfKey',sessionData.sessionKey,sessionData.expiry);
            res.redirect('/login?msg=Session Timed Out');
        }
    }
})

app.get('/logout',async (req,res)=>{
    let key = req.cookies.ctfKey;
    if(key){
        await business.deleteSession(key);
    }
    res.cookie('ctfKey','');
    res.redirect('/login');
})

//Flag & Flag Reveal route for XSS
const FLAG = 'flag{ctf_830294}';
app.post('/reveal', (req, res) => {
    if (req.headers['success'] === 'true') {
        res.send(FLAG);
    } else {
        res.send('No flag for you!');
    }
});

//admin Routes
app.get('/admin',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='admin'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
    }

    res.render('admin',{layout:'adminLayout'})
})

app.get('/admin/course',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='admin'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }

    let courses = await business.allCourses()
    if (courses){
        res.render("course",{
            layout:'adminLayout',
            courses:courses
        });        
    }else{
        res.status(404).render("error404", {layout:undefined})
    }
})

app.get('/admin/course/:codeName',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='admin'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }

    let code = req.params.codeName
    let course = await business.findCourse(code)
    let message = req.query.message
    let capRange=[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
    if (course && course.length>0){
        let cap = await course[0].capacity;
        res.render("codeDetail",{
            layout:'adminLayout',
            course:course,
            message:message,
            capRange:capRange,
            cap:cap
        })    
    }else{
        res.status(404).render("error404", {layout:undefined})
    }
})

app.post('/course/:courseCode/update',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='admin'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }

    let cap = req.body.capacity;
    let code = req.params.courseCode;
    await business.updateCapacity(code,cap);
    res.redirect(`/course/${code}`) 
})
//admin ends

//student Routes
app.get('/student',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='student'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
    }

    res.render('student',{layout:'studentLayout'})
})

app.get('/student/course',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='student'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }

    let courses = await business.getStudentCourses(sessionData.username)
    if (courses){
        res.render("studentCourse",{
            layout:'studentLayout',
            courses:courses
        });        
    }else{
        res.status(404).render("error404", {layout:undefined})
    }
})

app.get('/student/course/:codeName',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='student'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }

    let code = req.params.codeName
    let course = await business.studentFindCourse(code)
    let message = req.query.message
    let capRange=[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
    if (course){
        let cap = await course[0].capacity;
        res.render("codeDetail",{
            layout:'studentLayout',
            course:course,
            message:message,
            capRange:capRange,
            cap:cap
        })    
    }else{
        res.status(404).render("error404", {layout:undefined})
    }
})

app.get('/student/changePassword',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='student'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }
    let hash = crypto.createHash('sha256').update(sessionData.username).digest('hex');
    let msg;

    if(req.query.msg && req.query.msg=='success'){
        msg = "Password changed Successfully!!"
    }

    res.render('changePassword',{
        layout:'studentLayout',
        user:hash,
        msg:msg
    })
})
//student ends

//changePassword post request for all the roles
app.post('/changePassword',async (req,res)=>{
    let users = await business.getUsers();
    let hashes = []
    console.log(users)
    for (let user of users){
        let hash = crypto.createHash('sha256');
        hash.update(user.toString());
        let hashedUser = hash.digest('hex');
        hashes.push(hashedUser)
    }
    let password = req.body.password;
    let hash = req.body.token;

    if(!hashes.includes(hash)){
        res.status(404).render("error404", {layout:undefined})
        return
    }
    console.log(hashes)
    let userIndex = hashes.indexOf(hash);
    let username = users[userIndex];

    let update = await business.updatePassword(username,password)
    if(update){
        res.redirect('/student/changePassword?msg=success')
        return
    }

    res.send('Password Change Unsuccessful')
    return;
})

//instructor Routes
app.get('/instructor',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='instructor'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
    }

    res.render('student',{layout:'instructorLayout',title:'Instructor'})
})

app.get('/instructor/course',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='instructor'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }

    let courses = await business.getInstructorCourses(sessionData.username)
    if (courses){
        res.render("instructorCourse",{
            layout:'instructorLayout',
            courses:courses
        });        
    }else{
        res.status(404).render("error404", {layout:undefined})
    }
})

app.get('/instructor/course/:codeName',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='instructor'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }


    let code = req.params.codeName
    let course = await business.findCourse(code)
    let message = req.query.message
    let capRange=[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
    if (course){
        let cap = await course[0].capacity;
        res.render("codeDetail",{
            layout:'instructorLayout',
            course:course,
            message:message,
            capRange:capRange,
            cap:cap
        })    
    }else{
        res.status(404).render("error404", {layout:undefined})
    }
})

app.get('/instructor/changePassword',async (req,res)=>{
    let sessionId = req.cookies.ctfKey;
    let sessionData = await business.getSessionData(sessionId);

    if(!sessionData || sessionData.userType!='instructor'){
        let sessionData =await business.startSession('','');
        res.redirect('/login?msg=Session Timed Out');
        return
    }
    let hash = crypto.createHash('sha256').update(sessionData.username).digest('hex');
    let msg;

    if(req.query.msg && req.query.msg=='success'){
        msg = "Password changed Successfully!!"
    }

    res.render('changePassword',{
        layout:'instructorLayout',
        user:hash,
        msg:msg
    })
})
//instructor ends

//function to display 404 page
function function404(req, res) {
    res.status(404).render("error404", {layout:undefined})
}
app.use(function404)

//server listening port - 8000
app.listen(8000,()=>{
    console.log("Running")
})
