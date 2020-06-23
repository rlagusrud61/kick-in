/**
 * @summary This method is used to update the information on a specific user.
 *
 * @description The values of the name, email, password and clearance level of the user are retrieved from the web page
 * and the ID of the user to be updated is retrieved from the URL. This information is then used to create a JSON
 * object which is taken as a parameter to the function 'updateUser' so that the information on the user can be
 * updated in the database. Once the information is updated, the user is redirected to the 'users.html' page.
 */
function updateUserData() {
    let userId, userName, email, password, level, levelDescription, clearanceLevel, userJSON;
    userId = window.location.search.split("=")[1];
    userName = document.getElementById("userName").value;
    email = document.getElementById("email").value;
    password = document.getElementById("password").value;
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
        "password": password,
        "clearanceLevel": clearanceLevel,
        "userId": userId,
    };
    console.log(JSON.stringify(userJSON));
    updateUser(userJSON, function() {
        console.log(this.responseText);
        window.location.href = "users.html";
    })
}