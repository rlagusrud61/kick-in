function login() {

    let emailaddress = document.getElementById("inputEmail").value;
    let password = document.getElementById("inputPassword").value;

    console.log(emailaddress);
    console.log(password);

    let xhr = new XMLHttpRequest();
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
    xhr.send('{"email": "' + emailaddress + '", "password": "' + password + '"}');

}