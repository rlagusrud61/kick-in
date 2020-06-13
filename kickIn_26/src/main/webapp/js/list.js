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

window.onload = function () {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/events", true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState == 4) && (xhr.status == 200)) {
            var table = document.getElementById("eventtable");
            var events = JSON.parse(xhr.responseText);
            console.log(events);
            var htmltext = "<tr><th>Name</th><th>Creator</th><th>Editor</th><th>Event Info</th><th>Edit Event</th><th>Delete Event</th><tr>";
            table.innerHTML = htmltext;
            for (i = 0; i < events.length; i++) {
                var row = table.insertRow(i + 1);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);
                var cell6 = row.insertCell(5);
                cell1.innerHTML = events[i].name;
                cell2.innerHTML = events[i].createdBy;
                cell3.innerHTML = events[i].lastEditedBy;
                cell4.innerHTML = "<button onclick = 'window.location.href = \"http://localhost:8080/kickInTeam26/event.html?id=" + events[i].eventId + "\";'>View</button>"
                cell5.innerHTML = "<button onclick = 'window.location.href = \"http://localhost:8080/kickInTeam26/edit.html?id=" + events[i].eventId + "\";'>Edit</button>"
                cell6.innerHTML = "<button onclick = 'deleteEvent(" + events[i].eventId + ")'>Delete</button>"


//				htmltext += "<div class= 'col-md-12 col-sm-10'><div class='project'><div class='row bg-white has-shadow'>" +
//				"<div class='col-lg-12 col-md-12 d-flex align-items-center justify-content-between'><h3 class='h4'><a href='event.html'>" + events[i].name + "</a>" +
//				"</h3><h3 class='h4'>20/4/2020</h3><h3 class='h4'>" + events[i].createdBy + "</h3><h3 class='h4'> " + events[i].lastEditedBy + "</h3>" +
//				"<div class='col-md-1 col-sm-1 col-lg-1 d-flex justify-content-between'><a href='edit.html' class='text-success'>" +
//				"<i class='glyphicon glyphicon-pencil' style='font-size:20px;'></i></a><a href='edit.html' class='text-success'> " +
//				"<i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a></div></div></div></div></div>"
            }
//			table.innerHTML = htmltext;
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function addEvent() {
    description = document.getElementById("eventdescription").value;
    namestuff = document.getElementById("eventname").value;
    locationstuff = document.getElementById("eventlocation");
    eventloc = locationstuff.options[locationstuff.selectedIndex].value
    eventdate = document.getElementById("eventdate").value;
    eventjson = {
        "createdBy": "CreaJoep",
        "description": description,
        "lastEditedBy": "EditJoep",
        "location": eventloc,
        "name": namestuff
    };
    console.log(JSON.stringify(eventjson));
    var xhr = new XMLHttpRequest();
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
    var xhr = new XMLHttpRequest();
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
    const xhr = new XMLHttpRequest();
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
    searchValue = document.getElementById("searchTable");
    filter = searchValue.value.toUpperCase();
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
}
