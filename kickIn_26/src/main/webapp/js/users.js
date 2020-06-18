// Get the modal
var modal = document.getElementById("myModal");
// Get the button that opens the modal
var btn = document.getElementById("myBtn");
// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal
btn.onclick = function () {
    modal.style.display = "block";
}
// When the user clicks on <span> (x), close the modal
span.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function XSSInputSanitation(id) {
    let element = document.getElementById(id).value;
    if (element.indexOf("onload") !== -1 || element.indexOf("<script>") !== -1 ||
        element.indexOf("onerror") !== -1 || element.indexOf("alert") !== -1) {
        document.getElementById(id).value = "";
        return "";
    } else {
        return element;
    }
}

function loadTable() {
    let xhr, header, tr, th, i, table, users, row, name, email, password, clearanceLevel, action;
    xhr = new XMLHttpRequest();
    xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/users", true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status == 200)) {
            table = document.getElementById("usersTable");
            users = JSON.parse(xhr.responseText);
            console.log(users);

            header = [];
            header.push('Name');
            header.push('E-Mail')
            header.push('Password');
            header.push('Clearance Level');
            header.push('Action');

            tr = table.insertRow(-1); // add a row to the table

            for (i = 0; i < header.length; i++) {
                th = document.createElement("th"); // add a header to the table
                th.innerHTML = header[i];
                tr.appendChild(th);
            }

            for (i = 0; i < users.length; i++) {
                row = table.insertRow(-1);
                name = row.insertCell(0);
                email = row.insertCell(1);
                password = row.insertCell(2);
                clearanceLevel = row.insertCell(3);
                action = row.insertCell(4);
                name.innerHTML = users[i].nickname;
                email.innerHTML = users[i].email;
                password.innerHTML = users[i].password;
                clearanceLevel.innerHTML = users[i].clearanceLevel;
                action.innerHTML = "<a href='javascript: window.deleteUser(" + users[i].userId + ")' " +
                    "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
            }
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

window.onload = loadTable;

function addUserPopup() {
    let userName, email, password, level, levelDescription, clearanceLevel, userJSON, xhr;
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
    };
    console.log(JSON.stringify(userJSON));
    xhr = new XMLHttpRequest();
    xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/users", true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status = 201)) {
            console.log(xhr.responseText);
            location.reload();
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(userJSON));
}

function deleteUser(id) {
    let xhr = new XMLHttpRequest();
    xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/user/" + id, true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status == 204)) {
            console.log(xhr.responseText);
            location.reload();
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function logout() {
    let xhr = new XMLHttpRequest();
    xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/authentication", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText);
            window.location.href = "http://localhost:8080/kickInTeam26/login.html";
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function searchTables() {
    // Declare variables
    let searchValue, filter, table, tr, td, i, txtValue;
    searchValue = XSSInputSanitation('searchTable');
    filter = searchValue.toUpperCase();
    table = document.getElementById("usersTable");
    tr = table.getElementsByTagName("tr");
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function sortTableAZ() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("usersTable");
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            x = rows[i].getElementsByTagName("TD")[0];
            y = rows[i + 1].getElementsByTagName("TD")[0];
            // Check if the two rows should switch place:
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

function sortTableZA() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("usersTable");
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            x = rows[i].getElementsByTagName("TD")[0];
            y = rows[i + 1].getElementsByTagName("TD")[0];
            // Check if the two rows should switch place:
            if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

function sortTableHighLow() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("usersTable");
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            x = rows[i].getElementsByTagName("TD")[3];
            y = rows[i + 1].getElementsByTagName("TD")[3];
            // Check if the two rows should switch place:
            if (x.innerHTML < y.innerHTML) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

function sortTableLowHigh() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("usersTable");
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            x = rows[i].getElementsByTagName("TD")[3];
            y = rows[i + 1].getElementsByTagName("TD")[3];
            // Check if the two rows should switch place:
            if (x.innerHTML > y.innerHTML) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

function goBack() {
    window.history.back();
}