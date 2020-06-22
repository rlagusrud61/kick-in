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

/**
 * @summary This method is used to create a table that displays the information on the users.
 *
 * @description Once all the information on the users is retrieved using the 'getAllUsers' function, a table is created
 * to display the information received. The table includes the columns 'Name', 'E-Mail' and 'Action' where 'Name' gives the nickname
 * of the user, 'E-Mail' gives the email address of the user and 'Action' allows the user to get more detailed information on
 * the required user.
 */
function loadTable() {
    let header, tr, th, i, table, users, row, name, email, action;
    getAllUsers(function() {
    	table = document.getElementById("usersTable");
        users = JSON.parse(this.responseText);
        console.log(users);

        header = [];
        header.push('Name');
        header.push('E-Mail');
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
            action = row.insertCell(2);
            name.innerHTML = users[i].nickname;
            email.innerHTML = users[i].email;
            action.innerHTML= "<a href='javascript: window.viewUser(" + users[i].userId + ")' class='text-success'>" +
                "<i class='glyphicon glyphicon-eye-open' style='font-size:20px;'></i></a>"
        }
    })
}

window.onload = loadTable;

/**
 * @param userId - The ID of the user whose information is required.
 *
 * @summary This method allows the admin to get the information on individual users.
 *
 * @description Once all the information on the required is retrieved using the 'getUser' function which takes the ID of
 * the required user as a parameter, a table is created to display the information received and to delete and edit the
 * user's credentials if required. The table includes the columns 'Name' , 'E-Mail', 'Clearance Level' and
 * 'Action' where 'Name' gives the nickname of the user, 'E-Mail' gives the email address of the user, 'Clearance Level'
 * gives the authorisation level of the user and 'Action' allows the credentials of the user to be edited and deleted.
 */
function viewUser(userId) {
	getUser(userId, function() {
	    let userInfo, table, header, i, th, tr, levelDescription, row, nickname, email, clearanceLevel, action;
		userInfo = JSON.parse(this.responseText);
		console.log(userInfo.email);
		table = document.getElementById("userTable");
		table.innerHTML = "";
		header = [];
        header.push('Name');
        header.push('E-mail')
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
        clearanceLevel = row.insertCell(2);
        action = row.insertCell(3);
        
        nickname.innerHTML = userInfo.nickname;
        email.innerHTML = userInfo.email;
        clearanceLevel.innerHTML = levelDescription;
        action.innerHTML = "<a href='javascript: window.removeUser(" + userInfo.userId + ")' class='text-success'>" +
            "<i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        userModal.style.display = "block";
        
	});
}

/**
 * @summary This method allows a new user to be added to the database.
 *
 * @description The name, email, password and clearance level description is retrieved from the popup. The clearance level
 * is then converted from a description to a number in order to be stored in the database. Then, a JSON object is created
 * from the information retrieved and the 'addUser' function called with this JSON object as a parameter. If the user was
 * successfully added to the database, the page is reloaded.
 */
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

/**
 * @param {number} id - The ID of the user to be deleted from the database.
 *
 * @summary This method is used to delete the required user from the database.
 *
 * @description The 'deleteUser' method is called which takes the ID of the user to be deleted as a parameter and reloads
 * the page if the user was successfully deleted from the database.
 */
function removeUser(id) {
	deleteUser(id, function() {
		console.log(this.responseText);
        location.reload();
	})
}