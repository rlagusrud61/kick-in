let userId;
userId = window.location.search.split("=")[1];
/**
 * @summary This method is used to update the information on a specific user.
 *
 * @description The values of the name, email, password and clearance level of the user are retrieved from the web page
 * and the ID of the user to be updated is retrieved from the URL. This information is then used to create a JSON
 * object which is taken as a parameter to the function 'updateUser' so that the information on the user can be
 * updated in the database. Once the information is updated, the user is redirected to the 'users.html' page.
 */
function updateUserData() {
    let userName, email, level, levelDescription, clearanceLevel, userJSON;
    userName = document.getElementById("userName").value;
    email = document.getElementById("email").value;
    level = document.getElementById("clearanceLevel");
    levelDescription = level.options[level.selectedIndex].value;
    switch(levelDescription) {
        case "Visitor":
            clearanceLevel = 0;
            break;
        case "Editor":
            clearanceLevel = 1;
            break;
        case "Admin":
            clearanceLevel = 2;
            break;
        default:
            clearanceLevel = -1;
    }
    userJSON = {
        "nickname": userName,
        "email": email,
        "clearanceLevel": clearanceLevel,
        "userId": userId,
    };
    updateUser(userJSON, function() {
        window.location.href = "users.html";
    })
}

function resetPassword() {
    sendPasswordReset(userId, function() {
        window.location.href = "users.html";
    })
}