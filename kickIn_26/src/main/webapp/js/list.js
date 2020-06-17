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


function createEvent(form) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/events", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            window.location.href = 'http://localhost:8080/kickInTeam26/event.html/' + xhr.responseText
            console.log(xhr.responseText);
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(form));
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
    let xhr, header, tr, th, i, table, events, row, name, eventDate, creator, lastEditor, action;
    xhr = new XMLHttpRequest();
    xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/events", true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status == 200)) {
            table = document.getElementById("eventtable");
            events = JSON.parse(xhr.responseText);
            console.log(events);

            header = [];
            header.push('Name');
            header.push('Date Of Event')
            header.push('Creator');
            header.push('Last Editted By');
            header.push('Action');

            tr = table.insertRow(-1); // add a row to the table

            for (i = 0; i < header.length; i++) {
                th = document.createElement("th"); // add a header to the table
                th.innerHTML = header[i];
                tr.appendChild(th);
            }

            for (i = 0; i < events.length; i++) {
                row = table.insertRow(-1);
                name = row.insertCell(0);
                eventDate = row.insertCell(1);
                creator = row.insertCell(2);
                lastEditor = row.insertCell(3);
                action = row.insertCell(4);
                // deleteEvent = row.insertCell(5);
                name.innerHTML = '<a href = "http://localhost:8080/kickInTeam26/event.html?id=' + events[i].eventId + '">' + events[i].name + '</a>';
                eventDate = events[i].date;
                creator.innerHTML = events[i].createdBy;
                lastEditor.innerHTML = events[i].lastEditedBy;
                action.innerHTML = '<a href="http://localhost:8080/kickInTeam26/edit.html?id=' + events[i].eventId + '" class="text-success"><i class="glyphicon glyphicon-pencil" style="font-size:20px;"></i></a><a href="javascript: window.deleteEvent("' + events[i].eventId + '");" class="text-success"><i class="glyphicon glyphicon-trash" style="font-size:20px;"></span></a></i>';
                // action.innerHTML = "<button onclick = 'window.location.href = \"http://localhost:8080/kickInTeam26/event.html?id=" + events[i].eventId + "\";'>View</button>"
                // editEvent.innerHTML = "<button onclick = 'window.location.href = \"http://localhost:8080/kickInTeam26/edit.html?id=" + events[i].eventId + "\";'>Edit</button>"
                // deleteEvent.innerHTML = "<button onclick = 'deleteEvent(" + events[i].eventId + ")'>Delete</button>"

                    // '<a href="http://localhost:8080/kickInTeam26/edit.html?id=' + events[i].eventId + '" class="text-success"><i class="glyphicon glyphicon-pencil" style="font-size:20px;"></i></a>' +
                    // '' +
                    // '<a href='edit.html' class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>


//				htmltext += "<div class= 'col-md-12 col-sm-10'><div class='project'><div class='row bg-white has-shadow'>" +
//				"<div class='col-lg-12 col-md-12 d-flex align-items-center justify-content-between'><h3 class='h4'><a href='event.html'>" + events[i].name + "</a>" +
//				"</h3><h3 class='h4'>20/4/2020</h3><h3 class='h4'>" + events[i].createdBy + "</h3><h3 class='h4'> " + events[i].lastEditedBy + "</h3>" +
//				"<div class='col-md-1 col-sm-1 col-lg-1 d-flex justify-content-between'><a href='edit.html' class='text-success'>" +
//				"<i class='glyphicon glyphicon-pencil' style='font-size:20px;'></i></a><a href='edit.html' class='text-success'> " +
//				"<i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a></div></div></div></div></div>"
            }
            document.getElementById(id).style.property = new style

//			table.innerHTML = htmltext;
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

window.onload = loadTable;

function addEventPopup() {
    let description = document.getElementById("eventdescription").value;
    let namestuff = document.getElementById("eventname").value;
    let locationstuff = document.getElementById("eventlocation");

    let eventdate = document.getElementById("eventDate").value;
    console.log(eventdate);
    var dateControl = document.querySelector('input[type="date"]');
    dateControl.value = eventdate.value;
    console.log(dateControl.value); // prints "2017-06-01"
    console.log(dateControl.valueAsNumber); // prints 1496275200000, a UNIX timestamp

    let eventloc = locationstuff.options[locationstuff.selectedIndex].value
    let eventjson = {
        "createdBy": "CreaJoep",
        "description": description,
        "lastEditedBy": "EditJoep",
        "location": eventloc,
        "name": namestuff
    };
    console.log(JSON.stringify(eventjson));
    let xhr = new XMLHttpRequest();
    xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/events", true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status = 200)) {
            console.log(xhr.responseText);
            window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + xhr.responseText;
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(eventjson));
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
    //if (searchValue !== "") {
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
    //}
    // else {
    //     loadTable()
    // }
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