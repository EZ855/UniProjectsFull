import { BACKEND_PORT } from './config.js';
// A helper you may want to use when uploading new images to the server.
import { fileToDataUrl,
         fetch_api,
         fetch_setup } from './helpers.js';

// Global variables for user details.
let id = null;
let token = null;
let body = null;
let current_details = {};
let current_job_id = null;
let number_of_jobs = 0;

// Stores the email for the user profile we last looked at - used for unwatching the user.
let email_of_user_profile = null;

// Variables for the main sections of the page.
const login_page = document.getElementById('login-page');
const home_page = document.getElementById('home-page');
const footer = document.getElementById("footer");
const error_modal = document.getElementById('error-modal');
const error_modal_text = document.getElementById('error-modal-text');
const error_modal_title = document.getElementById('error-modal-title');
const error_modal_button = document.getElementById('error-modal-button');

// Close the error modal when the user clicks close.
error_modal_button.onclick = () => {
    error_modal.style.display = "none";
}

// This function displays the input text in the error modal.
function display_error(text) {
    error_modal.style.display = "block";
    error_modal_title.innerText = "Error!";
    error_modal_text.innerText = text;
}

// This function hijacks the error modal
function display_message(title, text) {
    error_modal.style.display = "block";
    error_modal_title.innerText = title;
    error_modal_text.innerText = text;
}

// Polling every 3 seconds.
setInterval(update_likes_and_comments, 1000);
setInterval(() => check_new_jobs(0), 5000);

/////////////////
// MILESTONE 1 //
/////////////////

let login_form = document.getElementById('login-form');
let register_form = document.getElementById('register-form');
let login_register_button = document.getElementById('toggle-login-register');

// Toggles the login and register forms when the user clicks the button.
login_register_button.onclick = () => {
    if (login_register_button.innerText === "Register") {
        login_form.style.display = "none";
        register_form.style.display = "flex";
        login_register_button.innerText = "Login";
        document.getElementById("login-question").innerText = "Already a member?";
        document.getElementById("login-page-container").style.height = "560px";
    } else {
        login_form.style.display = "flex";
        register_form.style.display = "none";
        login_register_button.innerText = "Register";
        document.getElementById("login-question").innerText = "Not a member?";
        document.getElementById("login-page-container").style.height = "420px";
    }
}


// Handles the case where a user submits login credentials.
login_form.addEventListener("submit", (event) => {

    // Stop the submit button from reloading the page.
    event.preventDefault();

    // Grab the email and password inputs.
    let login_email = document.getElementById('login-email').value;
    let login_password = document.getElementById('login-password').value;
    
    // Store them in JSON form to verify with the backend.
    body = {
        "email": login_email,
        "password": login_password
    }

    // Send the POST request with the login credentials.
    fetch_setup("POST", "auth/login", null, body)
    .then(result => {

        // Once the request resolves, store the token and id in their global variables.
        token = result.token;
        id = result.userId;

        // Remove the login page and display the user's home page.
        login_page.style.display = "none";
        home_page.style.display = "block";
        footer.style.display = "none";
        display_feed();
    })
    .catch(result => {
        // Create a popup using the error text returned.
        display_error(result);
    })
});

// Handles the case where a user submits registration data.
register_form.addEventListener("submit", (event) => {

    // Stop the submit button from reloading the page.
    event.preventDefault();

    // Grab the registration detail inputs.
    let register_name = document.getElementById('register-name').value;
    let register_email = document.getElementById('register-email').value;
    let register_password = document.getElementById('register-password').value;
    let register_password2 = document.getElementById('register-password2').value;

    // Check that the user filled all fields.
    if (!register_name || !register_email || !register_password) {
        // Display an error if not.
        display_error("Please fill all fields!");
        return;
    }

    // Check that the passwords entered match.
    if (register_password !== register_password2) {
        // Display an error saying the passwords don't match.
        display_error("Passwords do not match!");
        return;
    }

    body = {
        "email": register_email,
        "password": register_password,
        "name": register_name
    }

    // Send the POST request with the login credentials.
    fetch_setup("POST", "auth/register", null, body)
    .then(result => {

        // Store the user's token and id.
        token = result.token;
        id = result.userId;

        // Remove the login page and display the user's home page.
        login_page.style.display = "none";
        home_page.style.display = "block";
        footer.style.display = "none";
        display_feed();
    })
    .catch(result => {
        // Create a popup using the error text returned.
        display_error(result);
    })
});

/////////////////
// MILESTONE 2 //
/////////////////

function display_feed () {
    numJobs = 5;
    addJobsToFeed(0);
}

function addJobsToFeed(start) {
    fetch_setup("GET", `job/feed?start=${start}`, token, null)
    .then(result => {
        // Sorts job feed by date
        result.sort((job1, job2)=>{
            return new Date(job2.date) - new Date(job1.date);
        })

        // Creating each job
        for (const job of result) {
            const jobDiv = create_job(job);
            add_likes(jobDiv, job);
            add_comments (jobDiv, job);
            jobDiv.appendChild(document.createElement("hr"));
        }
    })
    .catch(result => {
        // console.logs any error
        console.log(result);
    })
}

function create_job (job) {
    const jobDiv = document.createElement("div");
    const jobTextDiv = document.createElement("div");

    // Calculating and inserting date/hours since a job was posted
    const todayDate = new Date();
    const yesterdayDate = new Date();
    yesterdayDate.setDate(yesterdayDate.getDate() - 1);
    const jobDate = new Date(job.createdAt);
    // If job was created within 24 hours
    //Append a span with hours and seconds since job was posted
    if (yesterdayDate - jobDate < 0 && todayDate - jobDate > 0) {
        var dateString = "posted ";
        var seconds = Math.floor((new Date() - jobDate) / 1000);
        dateString += Math.floor(seconds / 3600) + "h ";
        dateString += Math.floor((seconds / 3600 - Math.floor(seconds / 3600)) * 60) + "m ago\n";
        const dateSpan = document.createElement("span");
        dateSpan.innerText = dateString;
        dateSpan.style.fontStyle = "italic";
        dateSpan.className = "animation";
        jobTextDiv.appendChild(dateSpan);
    }
    // If job was not created within 24 hours
    // Append a span with date job was posted in DD/MM/YYYY format
    else {
        const dateSpan = document.createElement("span");
        dateSpan.innerText = "posted on " + jobDate.getDate() + "/" + (jobDate.getMonth() + 1) + "/" + jobDate.getFullYear() + "\n";
        dateSpan.style.fontStyle = "italic";
        jobTextDiv.appendChild(dateSpan);
    }

    // Insert image
    const image = new Image();
    image.src = job.image;
    image.alt = "Job image";
    image.style.width = "100px";
    image.style.height = "100px";
    image.style.borderRadius = "5px";
    jobDiv.appendChild(image);

    // Insert job title
    const jobTitleSpan = document.createElement("span");
    jobTitleSpan.innerText = job.title + "\n";
    jobTitleSpan.style.fontSize = "15pt";
    jobTitleSpan.style.fontWeight = "bold";
    jobTextDiv.appendChild(jobTitleSpan);

    // Insert job description
    const jobDescSpan = document.createElement("span");
    jobDescSpan.innerText += job.description + "\n";
    jobTextDiv.appendChild(jobDescSpan);

    // Insert job start date
    const startDateSpan = document.createElement("span");
    const startDate = new Date(job.start);
    startDateSpan.innerText += "Start on " + startDate.getDate() + "/" + (startDate.getMonth() + 1) + "/" + startDate.getFullYear() + "\n";
    jobTextDiv.appendChild(startDateSpan);

    // Insert who job post was made by
    fetch_setup("GET", `user?userId=${job.creatorId}`, token, null)
    .then(result => {
        const jobPosterSpan = document.createElement("span");
        const jobPosterNameSpan = document.createElement("span");

        // ADDED JOBPOSTERNAME SPAN ELEMENT TO SEPARATE ELEMENT (FOR MS 4)
        jobPosterNameSpan.id = `user-${job.creatorId}`;
        jobPosterSpan.innerText += "Posted by ";
        jobPosterNameSpan.innerText = result.name + "\n";
        jobPosterNameSpan.style.fontWeight = "bold";
        jobPosterNameSpan.style.cursor = "pointer";
        jobPosterSpan.appendChild(jobPosterNameSpan);
        jobTextDiv.appendChild(jobPosterSpan);
    });

    // Add entire job posting to DOM
    jobDiv.appendChild(jobTextDiv);
    jobDiv.style.display = "flex";
    jobDiv.style.flexDirection = "column";
    jobDiv.style.gap = "10px";

    // CHANGED TO JOB FEED SECTION OF HOME PAGE
    document.getElementById("job-feed").appendChild(jobDiv);

    return jobDiv;
}

/////////////////
// MILESTONE 3 //
/////////////////

function add_likes (jobDiv, job) {
    // 2.3.1 show likes on a job
    const likesDiv = document.createElement("div");
    const likesAmount = document.createElement("div");
    likesAmount.id = "job-likes-" + job.id;
    likesAmount.className = "user-profile-button";
    const likesNameDiv = document.createElement("div");
    likesNameDiv.id = `job-likes-name-${job.id}`;
    // Get number of likes
    if (job.likes.length === 1) {
        likesAmount.innerText = "1 like\n";
    } else {
        likesAmount.innerText = job.likes.length + " likes\n";
    }
    let liked = false;
    // Make list of users who liked post
    for (const like of job.likes) {
        let likesName = document.createElement("span");
        likesName.id = "user-" + like.userId;
        likesName.innerText = like.userName;
        likesName.style.border = "1px solid lightgrey";
        likesName.style.borderRadius = "10px";
        likesName.style.padding = "5px";
        likesNameDiv.appendChild(likesName);
        if (like.userId == id) {
            liked = true;
        }
    }
    // Displays people who liked when clicked
    likesAmount.addEventListener("click", ()=>{
        document.getElementById("job-modal-body").removeChild(document.getElementById("job-modal-body").firstChild);
        document.getElementById("job-modal").style.display = "block";
        document.getElementById("job-modal-title").innerText = "Likes";
        document.getElementById("job-modal-footer").style.display = "none";
        document.getElementById("job-modal-body").appendChild(likesNameDiv);
    });
    likesDiv.appendChild(likesAmount);

    // 2.3.3 liking a job
    const likeButton = document.createElement("button");
    likeButton.className = "user-profile-button";
    // Changes text if user already liked job post
    if (liked) {
        likeButton.innerText = "Unlike 😔";
    }
    else {
        likeButton.innerText = "Like 👍";
    }
    // Changes text and sends like to backend
    likeButton.addEventListener("click", ()=>{
        body = {
            "id": job.id,
        }
        if (likeButton.innerText == "Like 👍") {
            likeButton.innerText = "Unlike 😔";
            body.turnon = true;
        }
        else {
            likeButton.innerText = "Like 👍"
            body.turnon = false;
        }
        // Sends like
        fetch_setup("PUT", "job/like", token, body)
    });
    
    likesDiv.appendChild(likeButton);
    likesDiv.style.display = "flex";
    likesDiv.style.alignItems = "center";
    likesDiv.style.gap = "20px";
    jobDiv.appendChild(likesDiv);
}

function add_comments (jobDiv, job) {
    // Creating div housing all comment related material
    const commentDiv = document.createElement("div");
    commentDiv.id = "job-comments-" + job.id;
    commentDiv.className = "user-profile-button";
    commentDiv.style.textAlign = "center";
    if (job.comments.length === 1) {
        commentDiv.innerText = "1 comment\n";
    } else {
    commentDiv.innerText = job.comments.length + " comments\n";
    }
    const commentDisplayDiv = document.createElement("div");
    commentDisplayDiv.id = "job-comments-item-" + job.id;

    // // Creating div housing text box for adding comments
    // const addCommentDiv = document.createElement("div");
    // const addCommentTextArea = document.createElement("textarea");
    // addCommentTextArea.id = "comment" + job.id;
    // addCommentTextArea.placeholder = "Add a comment...";
    // addCommentDiv.appendChild(addCommentTextArea);
    // // Creating post comment button
    // const addCommentConfirmButton = document.createElement("button");
    // addCommentConfirmButton.data = job.id;
    // addCommentConfirmButton.innerText = "Post comment";
    // addCommentDiv.appendChild(addCommentConfirmButton);
    // addCommentConfirmButton.addEventListener("click", (event)=> {
    //     const comment = document.getElementById("comment" + event.target.data);
    //     if (comment.length != 0) {
    //         postComment(event.target.data, comment.value);
    //         comment.value = "";
    //     }
    //     else {
    //         display_error("Comment cannot be empty.")
    //     }
    // });


    //document.getElementById("job-modal-footer").appendChild(addCommentDiv);
    
    // Makes list of users + comments
    for (const c of job.comments) {
        let commentPoster = document.createElement("span");
        commentPoster.style.border = "1px solid lightgrey";
        commentPoster.style.borderRadius = "10px";
        commentPoster.style.padding = "5px";
        let commentText = document.createElement("p");
        commentPoster.id = "user-" + c.userId;
        commentPoster.innerText = c.userName;
        commentText.style.margin = "5px 0px 10px 5px";
        
        commentText.innerText = c.comment;
        
        commentDisplayDiv.appendChild(commentPoster);
        commentDisplayDiv.appendChild(commentText)
    }

    // Displays people who liked when like div clicked
    commentDiv.addEventListener("click", ()=>{
        document.getElementById("job-modal-body").removeChild(document.getElementById("job-modal-body").firstChild);
        document.getElementById("job-modal").style.display = "block";
        document.getElementById("job-modal-title").innerText = "Comments";
        document.getElementById("job-modal-footer").style.display = "block";
        current_job_id = job.id;
        document.getElementById("job-modal-body").appendChild(commentDisplayDiv);
    });

    jobDiv.appendChild(commentDiv);
}

document.getElementById("post-comment").addEventListener("click", () => {
    const comment = document.getElementById("comment-box");
    if (comment.value.length != 0) {
        postComment(current_job_id, comment.value);
        comment.value = "";
    }
    else {
        display_error("Comment cannot be empty.")
    }
})

// Closes the job modal and clears its contents.
document.getElementById("job-modal-close").addEventListener("click", () => {
    document.getElementById("job-modal").style.display = "none";
})

/////////////////
// MILESTONE 4 //
/////////////////

// Retrieve some elements from the DOM.
const job_feed = document.getElementById("job-feed");
const job_modal = document.getElementById("job-modal");
const user_profile_page = document.getElementById("user-profile-page");
const user_profile_watch = document.getElementById("user-profile-watch");
const user_profile_edit = document.getElementById("user-profile-edit");
const profile_modal = document.getElementById("profile-modal");
const profile_icon = document.getElementById("top-bar-profile");
const profile_modal_save = document.getElementById("profile-modal-save");
const watched_by_div = document.getElementById("user-profile-watched-by");
const jobs_list_div = document.getElementById("user-profile-jobs");

// Clears all the jobs in a user's feed - used for updating the feed.
function clear_job_feed() {
    while (job_feed.firstChild) {
        job_feed.removeChild(job_feed.firstChild);
    }
}

// Checks for clicks in the job feed section of the page - did we click on a user's name?
job_feed.addEventListener("click", (event => {
    if (!event.target.id.includes("user-")) return;
    // Checks that the id contains a number.
    if (isNaN(event.target.id.split("-")[1])) return;

    // Grab the user ID for the name we clicked on.
    const user_id = event.target.id.split("-")[1];

    if (parseInt(user_id) === id) {
        profile_icon.click();
        return;
    }

    display_profile(user_id);
}))

// Checks for clicks in the job modal section of the page - did we click on a user's name?
job_modal.addEventListener("click", (event => {
    if (!event.target.id.includes("user-")) return;
    // Checks that the id contains a number.
    if (isNaN(event.target.id.split("-")[1])) return;

    // Grab the user ID for the name we clicked on.
    const user_id = event.target.id.split("-")[1];
    
    job_modal.style.display = "none";
    
    if (parseInt(user_id) === id) {
        profile_icon.click();
        return;
    }

    // Clear the contents of the modal and hide it.
    job_modal.style.display = "none";
    display_profile(user_id);
}))

// Checks for clicks in the user profile section of the page - did we click on a user's name?
user_profile_page.addEventListener("click", (event => {

    if (!event.target.id.includes("user-")) return;
    // Checks that the id contains a number.
    if (isNaN(event.target.id.split("-")[1])) return;

    // Grab the user ID for the name we clicked on.
    const user_id = event.target.id.split("-")[1];

    if (parseInt(user_id) === id) {
        profile_icon.click();
        return;
    }

    // Clears the "watched by" section so we can fill it out for the new user.
    if (watched_by_div.firstChild) watched_by_div.removeChild(watched_by_div.firstChild);

    // Clears the "jobs" section so we can fill it out for the new user.
    if (jobs_list_div.firstChild) jobs_list_div.removeChild(jobs_list_div.firstChild);

    display_profile(user_id);
}))

// Creates and returns a span that houses all of the users watching a particular user.
function generate_watched_by(watched_by_list) {

    // Container for all of the user's names.
    const watched_by_span = document.createElement("span");
    watched_by_span.id = "watched-by-span";

    // Runs through the list of all users watching a particular user.
    for (const user of watched_by_list) {

        // Obtain the user's name, given their ID.
        fetch_setup("GET", `user?userId=${user}`, token, null)
        .then(result => {
            // Create a span to house the specific user.
            let user_watching = document.createElement("span");
            user_watching.innerText = result.name;
            user_watching.style.border = "solid 1px lightgrey";
            user_watching.style.borderRadius = "10px";
            user_watching.style.padding = "5px";

            // Set the ID to include that user's ID.
            user_watching.id = `user-${user}`;
            watched_by_span.appendChild(user_watching);
        })
        .catch(result => {
            console.log(result);
        })
    }
    return watched_by_span;
}

// Creates and returns a div that houses all of the jobs posted by a user.
function generate_jobs_list(jobs_list) {

    // Container for all of the jobs posted by the user.
    const jobs_outer_container = document.createElement("div");

    // Runs through all of the jobs the user posted and creates/appends each element.
    for (let job of jobs_list) {
        let jobs_inner_container = document.createElement("div");
        jobs_inner_container.id = "" + job.id;

        const jobsMediumContainer = document.createElement("div");
        jobsMediumContainer.appendChild(jobs_inner_container);

        const job_image_container = document.createElement("div");
        const job_text = document.createElement("div");
        const job_details = document.createElement("div");

        let job_image = document.createElement("img");
        job_image.src = job.image;
        job_image.style.width = "100px";
        job_image.style.height = "100px";
        job_image.style.borderRadius = "5px";
        job_image_container.appendChild(job_image);
        job_image_container.style.display = "flex";
        job_image_container.style.flexDirection = "column";
        job_image_container.style.justifyContent = "center";
        job_image_container.style.gap = "10px";


        let job_title = document.createElement("span");
        job_title.innerText = job.title +"\n";

        let job_start = document.createElement("span");
        let job_date = new Date(job.start);
        job_start.innerText = "Start on " + job_date.getDate() + "/" + (job_date.getMonth() + 1) + "/" + job_date.getFullYear() + "\n";

        let job_description = document.createElement("span");
        job_description.innerText = "Description: " + job.description + "\n";

        let job_created = document.createElement("span");
        job_date = new Date(job.createdAt);
        job_created.innerText = "Posted on " + job_date.getDate() + "/" + (job_date.getMonth() + 1) + "/" + job_date.getFullYear() + "\n";
        let jobEditButton = null;

        job_text.appendChild(job_title);
        job_text.appendChild(job_created);
        job_text.appendChild(job_description);
        job_text.appendChild(job_start);

        // Add button for editing the job if the user is the creator of the job
        if (parseInt(job.creatorId) === id) {
            jobEditButton = document.createElement("button");
            jobEditButton.innerText = "Edit Job 📝";
            jobEditButton.style.fontSize = "11pt";
            jobEditButton.className = "user-profile-button";
            jobEditButton.id = `job-edit-button-${job.id}`;
            jobEditButton.data = "" + job.id;
            jobEditButton.addEventListener("click", (event)=> {
                const curJob = document.getElementById("" + event.target.data);
                const curJobEdit = document.getElementById("edit" + event.target.data);
                curJob.style.display = "none";
                curJobEdit.style.display = "block";
            });
        }

        job_details.appendChild(job_image_container);
        job_details.appendChild(job_text);
        job_details.style.display = "flex";
        job_details.style.gap = "10px";

        jobs_inner_container.appendChild(job_details);

        if (jobEditButton) {
            job_image_container.appendChild(jobEditButton);
            addJobEdit(job.id, jobsMediumContainer, job, job_created);
        }
        jobs_inner_container.appendChild(document.createElement("hr"));
        jobEditButton = null;


        // Append to the container.
        jobs_outer_container.appendChild(jobsMediumContainer);
    }

    return jobs_outer_container;
}

// Given a user's ID, displays a page with their profile information.
function display_profile(userID) {

    // This selects all of the children of the user profile div.
    const user_profile_params = document.querySelectorAll("#user-details-container > *");

    // Makes the following adjustments to contents of the page if it's your own profile...
    const user_profile_heading = document.getElementById("user-profile-page-heading");
    if (userID === id) {
        // Change the heading to "your profile".
        user_profile_heading.innerText = "your profile 😎";
        // Remove the "watch user" button and display the "edit profile" button instead.
        user_profile_params[8].style.display = "none";
        user_profile_params[9].style.display = "inline-block";
    } else {
        user_profile_heading.innerText = "user profile 👤";
        user_profile_params[8].style.display = "inline-block";
        user_profile_params[9].style.display = "none";
    }
    
    // Fetch the user information.
    fetch_setup("GET", `user?userId=${userID}`, token, null)
    .then(result => {

        // Add all the info into the tags in the pre-existing user-details-container div.
        if ("image" in result) {
            user_profile_params[0].src = result.image;
        } else {
            user_profile_params[0].src = "";
            user_profile_params[0].alt = "No Profile Photo"
        }
        user_profile_params[2].innerText = result.name + "\n";
        user_profile_params[3].innerText = result.email + "\n";
        user_profile_params[4].innerText = "watched by " + result.watcheeUserIds.length + " - ";
        // Generate and append the watched by list and jobs list - easier to remove.
        user_profile_params[5].appendChild(generate_watched_by(result.watcheeUserIds));
        user_profile_params[7].appendChild(generate_jobs_list(result.jobs));

        // Store the email of the user we're currently looking at - used for unwatching the user.
        email_of_user_profile = result.email;

        // Change the button for watching/unwatching the user appropriately.
        result.watcheeUserIds.includes(id) ? user_profile_params[8].innerText = "Unwatch User 🙈" : user_profile_params[8].innerText = "Watch User 👀";

        // Remove the job feed and display the user profile page.
        user_profile_page.style.display = "block";
        document.getElementById("job-feed-container").style.display = "none";
    })
    .catch(result => {
        display_error(result);
    })
}

// Checks for clicks on the watch/unwatch user button.
user_profile_watch.onclick = () => {

    let turnon_val = false;

    // Change the button based on what it is set to currently.
    if (user_profile_watch.innerText === "Unwatch User 🙈") {
        user_profile_watch.innerText = "Watch User 👀";
    } else {
        turnon_val = true;
        user_profile_watch.innerText = "Unwatch User 🙈"
    }

    // Use the email we stored globally to watch/unwatch the user.
    body = {
        "email": email_of_user_profile,
        "turnon": turnon_val
    }

    fetch_setup("PUT", "user/watch", token, body)
    .then(result => {
        console.log(result);
    })
    .catch(result => {
        display_error(result);
    })
}

// Closes the user profile page and returns to the job feed page.
document.getElementById("user-profile-return").onclick = () => {

    // Remove all the span elements we created for the watched by and jobs list span.
    watched_by_div.removeChild(watched_by_div.firstChild);
    jobs_list_div.removeChild(jobs_list_div.firstChild);

    // Re-render the job feed in case we watched/unwatched a user.
    clear_job_feed();
    display_feed();

    user_profile_page.style.display = "none";
    document.getElementById("job-feed-container").style.display = "block";
}

// Checks for clicks on the user profile icon.
profile_icon.onclick = () => {

    // Clears the "watched by" section so we can fill it out for the new user.
    if (watched_by_div.firstChild) watched_by_div.removeChild(watched_by_div.firstChild);

    // Clears the "jobs" section so we can fill it out for the new user.
    if (jobs_list_div.firstChild) jobs_list_div.removeChild(jobs_list_div.firstChild);

    display_profile(id);
}

// User details within the user profile modal.
const profile_params = document.querySelectorAll("#user-profile-body > *");

// 2.4.3 Updating your profile.
user_profile_edit.onclick = () => {

    // Retrieve the user's details and fill in the modal.
    fetch_setup("GET", `user?userId=${id}`, token, null)
    .then(result => {
        // Add user's image if they have one.
        if (result.hasOwnProperty("image")) {
            profile_params[0].children[0].src = result.image;
            profile_params[0].children[0].style.width = "60px";
            profile_params[0].children[0].style.height = "60px";
            profile_params[0].children[0].style.borderRadius = "5px";
            current_details["image"] = result.image;
        } else {
            profile_params[0].children[0].alt = "No image";
            current_details["image"] = null;
        }

        // Display their details in the input field.
        profile_params[1].children[1].value = result.name;
        profile_params[2].children[1].value = result.email;

        // Update the current details in the global object.
        current_details["name"] = result.name;
        current_details["email"] = result.email;

        // Display the modal.
        profile_modal.style.display = "block";
    })
    .catch(result => {
        console.log(result);
    })
}

// Checks for when the save button is clicked to update the user's details.
profile_modal_save.onclick = () => {

    // TODO:
    // Error checking for user input.
    if (!profile_params[1].children[1].value || !profile_params[2].children[1].value) {
        display_error("You can't leave the name or email fields blank!");
        return;
    }

    // Used to store updated profile details.
    let new_name = undefined;
    let new_email = undefined;
    let new_password = undefined;
    let new_image = undefined;

    // If the user uploaded a file, pass it to fileToDataUrl to process.
    if (profile_params[0].children[1].files[0]) {
        fileToDataUrl(profile_params[0].children[1].files[0])
        .then(result => {
            new_image = result;
            // Then update the user details.
            update_details(new_name, new_email, new_password, new_image);
            return;
        })
        .catch(result => {
            display_error(result);
        })
    }
    
    update_details(new_name, new_email, new_password, new_image);
}

document.getElementById("profile-modal-close").onclick = () => {
    // Hide the modal.
    profile_modal.style.display = "none";
}

// Updates the user's profile details.
function update_details(new_name, new_email, new_password, new_image) {

    // Check if the user changed the name and email details.
    current_details["name"] !== profile_params[1].children[1].value ? new_name = profile_params[1].children[1].value : new_name = undefined;
    current_details["email"] !== profile_params[2].children[1].value ? new_email = profile_params[2].children[1].value : new_email = undefined;

    // If the user added a new password, set the new_password variable.
    profile_params[3].children[1].value ? new_password = profile_params[3].children[1].value : new_password = undefined;

    body = {
        "email": new_email,
        "password": new_password,
        "name": new_name,
        "image": new_image
    }

    // Pass all the details to the backend.
    fetch_setup("PUT", "user", token, body)
    .then(result => {
        profile_modal.style.display = "none";
        // Remove files that were uploaded.
        profile_params[0].children[1].value = "";

        // Refresh the profile and display it...
        // Clears the "watched by" section so we can fill it out for the new user.
        if (watched_by_div.firstChild) watched_by_div.removeChild(watched_by_div.firstChild);

        // Clears the "jobs" section so we can fill it out for the new user.
        if (jobs_list_div.firstChild) jobs_list_div.removeChild(jobs_list_div.firstChild);
        display_profile(id);
    })
    .catch(result => {
        display_error(result);
    })
}

// Shows the watch user modal to watch a new user.
document.getElementById("top-bar-watch").onclick = () => {
    document.getElementById("watch-user-modal").style.display = "block";
}

// Used to watch a new user, given their email.
document.getElementById("watch-user-modal-save").onclick = () => {

    // Grab the user input, pass it to the backend.
    const user_input = document.getElementById("watch-user-modal-input");

    body = {
        "email": user_input.value,
        "turnon": true
    }

    fetch_setup("PUT", "user/watch", token, body)
    .then(result => {
        // Close the modal and update the job feed.
        document.getElementById("watch-user-modal").style.display = "none";
        clear_job_feed();
        display_feed();
    })
    .catch(result => {
        display_error(result);
    })
}

// Hides the watch user modal.
document.getElementById("watch-user-modal-close").onclick = () => {
    document.getElementById("watch-user-modal").style.display = "none";
}

/////////////////
// MILESTONE 5 //
/////////////////

// Shows the add job modal.
document.getElementById("top-bar-add-job").onclick = () => {
    document.getElementById("add-job-modal").style.display = "block";
}
// Hides the add job modal.
document.getElementById("add-job-modal-close").onclick = () => {
    document.getElementById("add-job-modal").style.display = "none";
}
// Creates job
document.getElementById("add-job-modal-save").onclick = () => {

    // Grab job data
    const jobTitle = document.getElementById("add-job-title").value;
    const jobDesc = document.getElementById("add-job-description").value;
    const jobSD = document.getElementById("add-job-start-date").value;
    const jobPic = document.getElementById("add-job-picture").files;

    if (jobTitle.length == 0) {
        display_error("Job title cannot be empty.");
        return;
    }
    if (jobDesc.length == 0) {
        display_error("Job description cannot be empty.");
        return;
    }
    if (jobSD.length == 0) {
        display_error("Please select a start date.");
        return;
    }
    if (jobPic.length != 1) {
        display_error("Please include one picture that represents the job.");
        return;
    }
    // Processing picture
    fileToDataUrl(jobPic[0])
    .then(result => {
        // Sends all inputs to backend
        addJob(jobTitle, jobDesc, jobSD, result);
        // Close the modal and update the job feed.
        document.getElementById("add-job-modal").style.display = "none";
        return;
    })
    .catch(result => {
        display_error(result);
    })

}
// Sending new job to backend
function addJob(title, description, start, image) {

    body = {
        "title": title,
        "description": description,
        "start": start,
        "image": image
    }
    // Pass details to backend.
    fetch_setup("POST", "job", token, body)
    .then(result => {
        console.log(result);
    })
    .catch(result => {
        display_error(result);
    })
}

// Creating an editing div and inserting it with display = "none" next to
// self-posted jobs in the your profile modal
function addJobEdit(jobId, jobsMediumContainer, job, jobCreated) {
    // Creating container.
    const editJobsForm = document.createElement("form");
    editJobsForm.id = "edit-jobs-form" + jobId;
    editJobsForm.style.display = "none";
    editJobsForm.style.border = "1px solid black";
    editJobsForm.id = "edit" + jobId;
    jobsMediumContainer.appendChild(editJobsForm);
    // Creating input for image
    const inputJobImage = document.createElement("input");
    inputJobImage.type = "file";
    inputJobImage.accept = "image/png, image/jpeg, image/jpg";
    editJobsForm.appendChild(inputJobImage);
    // Creating input for job title
    const editJobTitleLabel = document.createElement("label");
    editJobTitleLabel.innerText = "Job Title:";
    editJobTitleLabel.for = "edit-job-title";
    const editJobTitle = document.createElement("input");
    editJobTitle.id = "edit-job-title";
    editJobTitle.value = job.title;
    editJobsForm.appendChild(editJobTitleLabel);
    editJobsForm.appendChild(editJobTitle);
    // Listing date posted for continuity
    const jobCreatedClone = jobCreated.cloneNode(true);
    editJobsForm.appendChild(jobCreatedClone);
    // Creating input for job description
    const editJobDescLabel = document.createElement("label");
    editJobDescLabel.innerText = "Job Description:";
    editJobDescLabel.for = "edit-job-desc";
    const editJobDesc = document.createElement("input");
    editJobDesc.id = "edit-job-desc";
    editJobDesc.value = job.description;
    editJobsForm.appendChild(editJobDescLabel);
    editJobsForm.appendChild(editJobDesc);
    // Creating input for job start date
    const editJobSDLabel = document.createElement("label");
    editJobSDLabel.innerText = "Job Start Date:";
    editJobSDLabel.for = "edit-job-SD";
    const editJobSD = document.createElement("input");
    editJobSD.type = "date";
    editJobSD.id = "edit-job-SD";
    editJobsForm.appendChild(editJobSDLabel);
    editJobsForm.appendChild(editJobSD);
    // Creating cancel button to change from 'editing mode' to viewing mode
    const jobEditCancelButton = document.createElement("button");
    jobEditCancelButton.innerText = "cancel";
    jobEditCancelButton.id = "job-edit-cancel-button";
    jobEditCancelButton.data = "" + jobId;
    jobEditCancelButton.type = "button";
    editJobsForm.appendChild(jobEditCancelButton);
    jobEditCancelButton.addEventListener("click", (event)=> {
        const curJob = document.getElementById("" + event.target.data);
        const curJobEdit = document.getElementById("edit" + event.target.data);
        curJob.style.display = "block";
        curJobEdit.style.display = "none";
    });
    // Creating save button to post edited job to backend
    const jobEditSaveButton = document.createElement("button");
    jobEditSaveButton.innerText = "save";
    jobEditSaveButton.id = "job-edit-save-button";
    jobEditSaveButton.data = "" + jobId;
    editJobsForm.appendChild(jobEditSaveButton);
    jobEditSaveButton.addEventListener("click", (event)=> {
        event.preventDefault();
        const curJobEdit = document.getElementById("edit" + event.target.data);

        // Grab job data
        const jobTitle = curJobEdit.children[2].value;
        const jobDesc = curJobEdit.children[5].value;
        const jobSD = curJobEdit.children[7].value;
        const jobPic = curJobEdit.children[0].files;

        if (jobTitle.length == 0) {
            display_error("Job title cannot be empty.");
            return;
        }
        if (jobDesc.length == 0) {
            display_error("Job description cannot be empty.");
            return;
        }
        if (jobSD.length == 0) {
            display_error("Please select a start date.");
            return;
        }
        if (jobPic.length != 1) {
            display_error("Please include one picture that represents the job.");
            return;
        }
        // Processing picture
        fileToDataUrl(jobPic[0])
        .then(result => {
            // Sends all inputs to backend
            editJob(jobId, jobTitle, jobDesc, jobSD, result);
            // Close the modal and update the job feed.
            document.getElementById("profile-modal").style.display = "none";
            return;
        })
        .catch(result => {
            display_error(result);
        })

        
    });
    // Create delete button
    const jobDeleteButton = document.createElement("button");
    jobDeleteButton.innerText = "delete";
    jobDeleteButton.id = "job-delete-button";
    jobDeleteButton.data = "" + jobId;
    jobDeleteButton.type = "button";
    editJobsForm.appendChild(jobDeleteButton);
    jobDeleteButton.addEventListener("click", (event)=> {
        if (confirm("Are you sure you want to delete this job?")) {
            body = {
                "id": event.target.data
            };
            fetch_setup("DELETE", "job", token, body)
            .then(() => {
                document.getElementById("profile-modal").style.display = "none";
            })
            .catch(result => {
                console.log(result);
            })
        }
    });
}
// Sending edited job backend
function editJob(id, title, description, start, image) {
    body = {
        "id": id,
        "title": title,
        "description": description,
        "start": start,
        "image": image
    }
    fetch_setup("PUT", "job", token, body)
    .catch(result => {
        console.log(result);
    })
}
// Sending comment to backend
function postComment(jobId, comment) {
    body = {
        "id": jobId,
        "comment" : comment
    };
    fetch_setup("POST", "job/comment", token, body)
    .then(()=>{
        document.getElementById("job-modal").style.display = "none";
    })
    .catch(result => {
        console.log(result);
    })
}

/////////////////
// MILESTONE 6 //
/////////////////

// 2.6.1 infinite scroll
// When the window is 200px off the bottom
// Automatically adds 5 more jobs to feed
// https://webdesign.tutsplus.com/tutorials/how-to-implement-infinite-scrolling-with-javascript--cms-37055
let numJobs = 5;
function add5Jobs() {
    addJobsToFeed(numJobs);
    numJobs += 5;
}
window.addEventListener("scroll", ()=>{
    throttle(()=>{
        console.log("hello");
        if (window.innerHeight + window.pageYOffset + 200 >= document.getElementById("job-feed").offsetHeight) {
            console.log("hello2");
            add5Jobs();
        }
    }, 1000);
});
// Preventing large amounts of calls to add5Jobs
let throttleTimer;
const throttle = (callback, time) => {
    if (throttleTimer) return;
    throttleTimer = true;
    setTimeout(() => {
        callback();
        throttleTimer = false;
    }, time);
};

// 2.6.2 Live update.
// Currently refreshes every 3 seconds
// NOTE: this needs to be updated for cases where there are >5 jobs.
// Not sure if this is the best way to do dis 
function update_likes_and_comments () {
    if (!token) return;

    // Send the GET request with token and start = 0
    fetch_setup("GET", "job/feed?start=0", token, null)
    .then(result => {
        // Sorts job feed by date
        result.sort((job1, job2)=>{
            return new Date(job2.date) - new Date(job1.date);
        })

        // Creating each job
        for (const job of result) {

            if (job.likes.length === 1) {
                document.getElementById(`job-likes-${job.id}`).innerText = "1 like\n";
            } else {
                document.getElementById(`job-likes-${job.id}`).innerText = job.likes.length + " likes\n";
            }
            //console.log(job.id);

            if (document.getElementById(`job-likes-name-${job.id}`)) {
                const likesNameDiv = document.getElementById(`job-likes-name-${job.id}`);
                remove_children(likesNameDiv);

                // NOTE: A lil buggy here - gets into the if statement, but then becomes null and logs an error

    
                for (const like of job.likes) {
                    let likesName = document.createElement("span");
                    likesName.id = "user-" + like.userId;
                    likesName.innerText = like.userName;
                    likesName.style.border = "1px solid lightgrey";
                    likesName.style.borderRadius = "10px";
                    likesName.style.padding = "5px";
                    likesNameDiv.appendChild(likesName);
                }

            }

            //console.log(job.comments);
            if (job.comments.length === 1) {
                document.getElementById(`job-comments-${job.id}`).innerText = "1 comment\n";
            } else {
                document.getElementById(`job-comments-${job.id}`).innerText = job.comments.length + " comments\n";
            }

            if (document.getElementById(`job-comments-item-${job.id}`)) {
                const commentDisplayDiv = document.getElementById(`job-comments-item-${job.id}`);
                remove_children(commentDisplayDiv);
                
                // Makes list of users + comments
                for (const c of job.comments) {
                    let commentPoster = document.createElement("span");
                    commentPoster.style.border = "1px solid lightgrey";
                    commentPoster.style.borderRadius = "10px";
                    commentPoster.style.padding = "5px";
                    let commentText = document.createElement("p");
                    commentPoster.id = "user-" + c.userId;
                    commentPoster.innerText = c.userName;
                    commentText.style.margin = "5px 0px 10px 5px";
                    
                    commentText.innerText = c.comment;
                    
                    commentDisplayDiv.appendChild(commentPoster);
                    commentDisplayDiv.appendChild(commentText)
                }
            }
        }
    })
    .catch(result => {
        // console.logs any error

        console.log(result);
    })
}

function remove_children(element) {
    while (element.firstChild) {
        element.removeChild(element.firstChild);
    }
}

const notification = document.getElementById("notification-modal");

// 2.6.3 Push notifications
function check_new_jobs(index) {
    if (!token) return;

    fetch_setup("GET", `job/feed?start=${index}`, token, null)
    .then(result => {
        if (result.length === 5) {
            check_new_jobs(index + 5);
        }

        let new_number_of_jobs = index + result.length;
        if (new_number_of_jobs > number_of_jobs) {
            number_of_jobs = new_number_of_jobs;
        
            notification.style.display = "block";

            setTimeout(() => {
                notification.style.display = "none";
            }, 5000)
        }

        document.getElementById("notification-modal-close").addEventListener("click", () => {
            notification.style.display = "none";
        })
    })
    .catch(result => {
        console.log(result);
    })
}


/////////////////
// MILESTONE 7 //
/////////////////



/////////////////
//    BONUS    //
/////////////////

// Logout
document.getElementById("top-bar-logout").onclick = () => {
    clear_job_feed();
    display_message("Logged Out", "Successfully logged out. See you next time! 😊")
    login_page.style.display = "flex";
    document.getElementById("job-feed-container").style.display = "block";
    home_page.style.display = "none";
    user_profile_page.style.display = "none";
    footer.style.display = "flex";
    current_details = {};
    if (watched_by_div.firstChild) watched_by_div.removeChild(watched_by_div.firstChild);
    if (jobs_list_div.firstChild) jobs_list_div.removeChild(jobs_list_div.firstChild);
}

// Animations
// https://www.w3schools.com/css/css3_animations.asp
// Any job posted within 24 hours will have the post date animated!!
// Decided against putting a fire gif as the background because it made it look
// like it was made in the 2000s

// STUFF TO FIX
// Maybe add a remove profile photo button
// Check if there are anymore instances of "failed to removeChild etc."
// Fix edits to jobs
// Infinite scroll doesn't work when you watch a user using the watch user modal.
