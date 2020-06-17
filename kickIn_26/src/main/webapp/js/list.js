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
span.onclick = function () {
    modal.style.display = "none";
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
	let xhr, header, tr, th, i, table, events, row, name, creator, editor, eventInfo, editEvent, deleteEvent;
	getAllEvents(function() {
		this.responseText;
	});
	getAllEvents(function() {
		table = document.getElementById("eventtable");
		events = JSON.parse(this.responseText);
        console.log(events);

        header = [];
        header.push('Name');
        header.push('Creator');
        header.push('Editor');
        header.push('Event Information');
        header.push('Edit Event');
        header.push('Delete Event');

        tr = table.insertRow(-1); // add a row to the table

        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }

        for (i = 0; i < events.length; i++) {
            row = table.insertRow(-1);
            name = row.insertCell(0);
            creator = row.insertCell(1);
            editor = row.insertCell(2);
            eventInfo = row.insertCell(3);
            editEvent = row.insertCell(4);
            deleteEvent = row.insertCell(5);
            name.innerHTML = events[i].name;
            creator.innerHTML = events[i].createdBy;
            editor.innerHTML = events[i].lastEditedBy;
            eventInfo.innerHTML = "<button onclick = 'window.location.href = \"http://localhost:8080/kickInTeam26/event.html?id=" + events[i].eventId + "\";'>View</button>"
            editEvent.innerHTML = "<button onclick = 'window.location.href = \"http://localhost:8080/kickInTeam26/edit.html?id=" + events[i].eventId + "\";'>Edit</button>"
            deleteEvent.innerHTML = "<button onclick = 'deleteEvent(" + events[i].eventId + ")'>Delete</button>"
        }
	});
}

window.onload = loadTable;

function creatEvent() {
	let eventDescription, eventName, eventLocation, eventDate, eventLoc, eventJson
    eventDescription = document.getElementById("eventDescription").value;
    eventName = document.getElementById("eventName").value;
    eventLoc = document.getElementById("eventLocation");
    eventDate = document.getElementById("eventDate").value;
    eventLocation = eventLoc.options[eventLoc.selectedIndex].value;
    eventJson = {
    	"date": eventDate,
        "description": eventDescription,
        "location": eventLocation,
        "name": eventName
    };
    console.log(eventJson);
    addEvent(eventJson, function() {
    	resp = this.responseText;
    	window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + resp;
    })
}

function deleteEvent(id) {
    let xhr = new XMLHttpRequest();
    xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/event/" + id, true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status == 200)) {
            console.log(xhr.responseText);
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
    if (searchValue !== "") {
        filter = searchValue.toUpperCase();
        table = document.getElementById("eventtable");
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
    } else {
        loadTable()
    }
}

function sortTableAZ() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("eventtable");
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
    table = document.getElementById("eventtable");
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