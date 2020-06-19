let modal, btn, span, trashBtn, deleteModal, close;

// Get the modal
modal = document.getElementById("addEvent");
btn = document.getElementById("addEventBtn");
span = document.getElementsByClassName("close")[0];

btn.onclick = function(){
    modal.style.display = "block";
}

span.onclick = function(event){
    if (event.target === modal) {
        modal.style.display = "none";
    }
}


window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

trashBtn = document.getElementById("yesDeleteButton");
// Get the modal
deleteModal = document.getElementById("eventDeleteModal");
// Get the <span> element that closes the modal
close = document.getElementsByClassName("close")[1];

// When the user clicks the button, open the modal


//Delete function for loadTable()
function confirmDelete(eventId) {
    trashBtn.setAttribute("onclick", "removeEvent(" + eventId + ")");
    deleteModal.style.display = "block";
}


// When the user clicks on <span> (x), close the modal
//Doesn't work
close.onclick = function (event) {
    if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    }
}

// When the user clicks anywhere outside of the modal, close it
// Works
window.onclick = function (event) {
    if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    }
}

function loadTable() {
    let header, tr, th, i, table, events, row, name, eventDate, creator, lastEditor, action;
    getAllEvents(function () {
        table = document.getElementById("eventtable");
        events = JSON.parse(this.responseText);
        console.log(events);
        header = [];
        header.push('Name');
        header.push('Date Of Event')
        header.push('Creator');
        header.push('Last Edited By');
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
            name.innerHTML = events[i].name;
            eventDate.innerHTML = events[i].date;
            creator.innerHTML = events[i].createdBy;
            lastEditor.innerHTML = events[i].lastEditedBy;
            action.innerHTML = "<a href='http://localhost:8080/kickInTeam26/event.html?id=" +
                events[i].eventId + "' class='text-success'><i class='glyphicon glyphicon-eye-open' " +
                "style='font-size:20px;'></i></a><a href='http://localhost:8080/kickInTeam26/edit.html?id=" +
                events[i].eventId + "' class='text-success'><i class='glyphicon glyphicon-pencil' " +
                "style='font-size:20px;'></i></a><a href='javascript: window.confirmDelete(" + events[i].eventId + ")'" +
                "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        }
    });
}

window.onload = loadTable;

function addEventPopup() {
    let description, eventName, eventLoc, eventDate, dateControl, eventLocation, eventJSON;
    description = document.getElementById("eventDescription").value;
    eventName = document.getElementById("eventName").value;
    eventLoc = document.getElementById("eventLocation");
    eventDate = document.getElementById("eventDate").value;
    eventLocation = eventLoc.options[eventLoc.selectedIndex].value;
    eventJSON = {
        "name": eventName,
        "date": eventDate,
        "description": description,
        "location": eventLocation,
    };
    addEvent(eventJSON, function () {
        location.reload();
    });
}

function removeEvent(id) {
    deleteEvent(id, function () {
        console.log(this.responseText);
        location.reload();
    });
}