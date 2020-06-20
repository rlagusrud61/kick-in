let modal, btn, span;

// Get the modal
modal = document.getElementById("myModal");
userModal = document.getElementById("userViewModal");
// Get the button that opens the modal
btn = document.getElementById("myBtn");
// Get the <span> element that closes the modal
span = document.getElementsByClassName("close")[0];
span2 = document.getElementsByClassName("close")[1];


// When the user clicks the button, open the modal
btn.onclick = function () {
    modal.style.display = "block";
}
// When the user clicks on <span> (x), close the modal
span.onclick = function (event) {
    userModal.style.display = "none";
}

span2.onclick = function (event) {
    modal.style.display = "none";
}
// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    } else if (event.target === userViewModal) {
    	userViewModal.style.display = "none";
    }
}

function loadTable() {
    let header, tr, th, i, table, users, row, name, email, password, clearanceLevel, levelDescription, action;
    getAllUsers(function() {
    	table = document.getElementById("usersTable");
        users = JSON.parse(this.responseText);
        console.log(users);

        header = [];
        header.push('Name');
        header.push('E-Mail')

        tr = table.insertRow(-1); // add a row to the table

        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }
        for (i = 0; i < users.length; i++) {
            row = table.insertRow(-1);
            nickname = row.insertCell(0);
            email = row.insertCell(1);
            nickname.innerHTML = "<a href='javascript: window.viewUser(" + users[i].userId + ")'>" + users[i].nickname + "</a>"
            email.innerHTML = users[i].email;
        }
    })
}

window.onload = loadTable;
function viewUser(userId) {
	getUser(userId, function() {
		userInfo = JSON.parse(this.responseText);
		console.log(userInfo.email);
		table = document.getElementById("userTable");
		table.innerHTML = "";
		header = [];
        header.push('Name');
        header.push('E-mail')
        header.push('Password');
        header.push('Clearance Level');
        header.push('Delete');

        tr = table.insertRow(-1); // add a row to the table
        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }
        
        switch(userInfo.clearanceLevel) {
        case 0:
            levelDescription = "Visitor";
            break;
        case 1:
            levelDescription = "Editor";
            break;
        case 2:
            levelDescription = "Admin";
            break;
        default:
            levelDescription = "Unauthorised";
        }
        
        row = table.insertRow(-1);
        nickname = row.insertCell(0);
        email = row.insertCell(1);
        password = row.insertCell(2);
        clearanceLevel = row.insertCell(3);
        action = row.insertCell(4);
        
        nickname.innerHTML = userInfo.nickname;
        email.innerHTML = userInfo.email;
        password.innerHTML = userInfo.password;
        clearanceLevel.innerHTML = levelDescription;
        action.innerHTML = "<a href='javascript: window.removeUser(" + userInfo.userId + ")' " +
        "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        userModal.style.display = "block";
        
	})
}

function addUserPopup() {
    let userName, email, password, level, levelDescription, clearanceLevel, userJSON;
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
    addUser(userJSON, function() {
    	console.log(this.responseText);
        location.reload();
    })
}

function removeUser(id) {
	deleteUser(id, function() {
		console.log(this.responseText);
        location.reload();
	})
}

function sortTableHighLow() {
    let table, rows, switching, i, x, y, shouldSwitch, levelX, levelY;
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
            switch(x.innerHTML) {
                case "Visitor":
                    levelX = 0;
                    break;
                case "Editor":
                    levelX = 1;
                    break;
                case "Admin":
                    levelX = 2;
                    break;
                default:
                    levelX = -1;
            }
            switch(y.innerHTML) {
                case "Visitor":
                    levelY = 0;
                    break;
                case "Editor":
                    levelY = 1;
                    break;
                case "Admin":
                    levelY = 2;
                    break;
                default:
                    levelY = -1;
            }
            // Check if the two rows should switch place:
            if (levelX < levelY) {
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
    let table, rows, switching, i, x, y, shouldSwitch, levelX, levelY;
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
            switch(x.innerHTML) {
                case "Visitor":
                    levelX = 0;
                    break;
                case "Editor":
                    levelX = 1;
                    break;
                case "Admin":
                    levelX = 2;
                    break;
                default:
                    levelX = -1;
            }
            switch(y.innerHTML) {
                case "Visitor":
                    levelY = 0;
                    break;
                case "Editor":
                    levelY = 1;
                    break;
                case "Admin":
                    levelY = 2;
                    break;
                default:
                    levelY = -1;
            }
            // Check if the two rows should switch place:
            if (levelX > levelY) {
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