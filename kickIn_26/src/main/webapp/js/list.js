let modal, btn, span, yesBtn, deleteModal, close;

// Get the modal
modal = document.getElementById("addEvent");
deleteModal = document.getElementById("eventDeleteModal");
// Get the button that trigger the modal
btn = document.getElementById("addEventBtn");
noBtn = document.getElementById("noBtn");
yesBtn = document.getElementById("yesDeleteButton");
// Get the button to close (x) the modal
span = document.getElementsByClassName("close close_multi")[0];
span2 = document.getElementsByClassName("close close_multi")[1];

btn.onclick = function () {
    modal.style.display = "block";
};

noBtn.onclick = function () {
    deleteModal.style.display = "none";
};

span.onclick = function () {
    modal.style.display = "none";
};
span2.onclick = function () {
    deleteModal.style.display = "none";

};

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    }
}

/**
 * @summary This method is used to create a table that displays the information on the events.
 *
 * @description Once all the information on the events is retrieved using the 'getAllEvents' function, a table is created
 * to display the information received. The table includes the columns 'Name', 'Date Of Event', 'Creator', 'Last Edited By'
 * and 'Action' where 'Name' gives the name of the event, 'Date Of Event' gives the date of the event, 'Creator' gives the
 * name of the user that created the event, 'Last Edited By' gives name of the user that last edited the data on the the
 * event and 'Action' allows the user to view the data on the event (including maps), edit the data on the event as well as
 * delete the event.
 */
function loadTable() {
    let header, tr, th, i, table, events, row, name, eventDate, creator, lastEditor, action;
    getAllEvents(function () {
        table = document.getElementById("eventTable");
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
            action.innerHTML = "<a href='event.html?id=" +
                events[i].eventId + "' class='text-success'><i class='glyphicon glyphicon-eye-open' " +
                "style='font-size:20px;'></i></a><a href='editEvent.html?id=" +
                events[i].eventId + "' class='text-success'><i class='glyphicon glyphicon-pencil' " +
                "style='font-size:20px;'></i></a><a href='javascript: window.confirmDelete(" + events[i].eventId + ")'" +
                "' class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        }
    });
}

window.onload = loadTable;

/**
 * @summary This method is used to add a new event to the database.
 *
 * @description The name, description, location and date of the event is retrieved from the popup. Then, a JSON object
 * is created from the information retrieved and the 'addEvent' function is called with this JSON object as a parameter.
 * If the event was successfully added to the database, the page is reloaded.
 */
function addEventPopup() {
    let description, eventName, eventLoc, eventDate, eventLocation, eventJSON;
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

/**
 * @param {number} eventId - the ID of the event for which the trash button was clicked.
 *
 * @summary This method is used to open the modal and set the ID of the event to be deleted when the 'YES' button is
 * clicked in the event deletion confirmation popup.
 */
function confirmDelete(eventId) {
    yesBtn.setAttribute("onclick", "removeEvent(" + eventId + ")");
    deleteModal.style.display = "block";
}

/**
 * @param {number} eventId - the ID of the event that is to be deleted from the database.
 *
 * @summary This method is used to delete the required event from the database and reloads the page.
 *
 * @description When the 'YES' button is clicked in the event deletion confirmation popup, the 'removeEvent'
 * function is called with the ID of the event as a parameter so that it can be deleted from the database.
 * The ID of the event is taken as a parameter to the 'deleteEvent' function so that it can be deleted from
 * the database. If the deletion is successful, the page is reloaded.
 */
function removeEvent(eventId) {
    deleteEvent(eventId, function () {
        location.reload();
    })
}

