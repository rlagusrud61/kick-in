function login() {

    let emailAddress, password, xhr;
    emailAddress = document.getElementById("inputEmail").value;
    password = document.getElementById("inputPassword").value;

    console.log(emailAddress);
    console.log(password);

    xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        console.log("Something")
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                window.location.href = "http://localhost:8080/kickInTeam26/list.html";
            } else if (xhr.status === 401) {
                console.log("401 Unauthorized")
            }
        }
    }

    xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/authentication", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send('{"email": "' + emailAddress + '", "password": "' + password + '"}');
}

function checkKeyPress() {
	if (event.keyCode == 13) {
		login();
	}
}
