/**
 * @summary This function is used to log into the website.
 *
 * @description First, the email address and password of the user are retrieved from the web page. This information is
 * then used to create a JSON object which is taken as a parameter to the function 'loginUser' so that the credentials
 * can be authenticated and the user redirected to the 'list.html' page if the authentication was a success. If the authentication
 * is unsuccessful, the password and email fields are emptied and an error message is displayed.
 */
function login() {
    let emailAddress, password, credentialsJSON;

    emailAddress = document.getElementById("inputEmail").value;
    password = document.getElementById("inputPassword").value;

    credentialsJSON = {
        "email": emailAddress,
        "password": password
    };
    loginUser(credentialsJSON, function() {
        if (this.status === 204){
            window.location.href = "list.html";
        } else if (this.status === 403){
            document.getElementById("inputEmail").value = "";
            document.getElementById("inputPassword").value = "";
            document.getElementById("incorrectCredentials").innerHTML = "The credentials entered were incorrect. Try again."
        }
    });
}

/**
 * @summary This method allows the user to login by pressing the enter key when both the email address and password
 * have been entered.
 *
 * @description This method first checks the password input field for any signs of an XSS attack and sanitises the
 * input. After that, it checks if the user pressed the 'enter' key and that both the password and email input fields
 * are not empty. If this is true, the 'login' function is called.
 */
function checkKeyPress() {
    let password, emailAddress;
    password = document.getElementById('inputPassword').value;
    emailAddress = document.getElementById('inputEmail').value;
	if (event.keyCode === 13 && password !== "" && emailAddress !== "") {
		login();
	}
}
