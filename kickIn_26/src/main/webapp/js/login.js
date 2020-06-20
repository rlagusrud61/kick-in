/**
 * @summary This function is used to log into the website.
 *
 * @description First, the email address and password of the user are retrieved from the web page. A JSON object is
 * then created from the data that was retrieved. A POST request is sent to the RESTful service provider with the given
 * URL, where the content of the body is the JSON object that was created using the email address and the password.
 * The method then redirects the user to the 'list.html' page if the authentication was a success.
 */
function login() {

    let emailAddress, password, xhr, credentialsJSON;

    emailAddress = document.getElementById("inputEmail").value;
    password = document.getElementById("inputPassword").value;

    credentialsJSON = {
        "email": emailAddress,
        "password": password
    };

    console.log(emailAddress);
    console.log(password);

    xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        console.log("Something");
        console.log(xhr.status);
        if (xhr.readyState === 4) {
            if (xhr.status === 204) {
                console.log(xhr.responseText);
                window.location.href = "http://localhost:8080/kickInTeam26/list.html";
            } else if (xhr.status === 401) {
                console.log("401 Unauthorized")
            }
        }
    }

    xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/authentication", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(credentialsJSON));
}

/**
 * @summary This method allows the user to login by pressing the enter key when both the email address and password
 * have been entered.
 *
 * @desccription This method first checks the password input field for any signs of an XSS attack. After,
 */
function checkKeyPress() {
    let password, emailAddress;
    password = XSSInputSanitation('inputPassword');
    emailAddress = document.getElementById('inputEmail').value;
	if (event.keyCode === 13 && password !== "" && emailAddress !== "") {
		login();
	}
}
