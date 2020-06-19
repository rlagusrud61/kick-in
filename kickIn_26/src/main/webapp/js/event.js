let modal, modal1, btn, btn1, span;

// Get the modal
modal = document.getElementById("popupEventDelete");
modal1 = document.getElementById("popupMapDelete");

// Get the button that opens the modal
btn = document.getElementById("deleteEvent");
btn1 = document.getElementById("deleteMap");

// Get the <span> element that closes the modal
span = document.getElementsByClassName("close")[0];


// When the user clicks the button, open the modal
btn.onclick = function() {
    modal.style.display = "block";
}

btn1.onclick = function() {
    modal1.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === modal1) {
        modal1.style.display = "none";
    }
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === modal1) {
        modal1.style.display = "none";
    }
}

function displayEventInfo(){
    let id, event, maps, table, header, th, tr, row, mapName, creator, lastEditor, action;
    id = window.location.search.split("=")[1];
    document.getElementById("editEvent").href = "http://localhost:8080/kickInTeam26/edit.html?id=" + id
    getEvent(id, function() {
        event = JSON.parse(this.responseText);
        document.getElementById("introtext").innerHTML = event.description;
        document.getElementById("eventlocation").innerHTML = event.location;
        document.getElementById("eventname").innerHTML = event.name;
        document.getElementById("eventdate").innerHTML = event.date;
        document.getElementById("addNewMap").href = "http://localhost:8080/kickInTeam26/newMap.html?id=" + event.eventId;
    })

    getAllMapsForEvent(id, function() {
        maps = JSON.parse(this.responseText);
        table = document.getElementById("mapTable");
        console.log(maps);
        header = [];
        header.push('Name');
        header.push('Creator');
        header.push('Last Edited By');
        header.push('Action');

        tr = table.insertRow(-1); // add a row to the table
        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }

        for (i = 0; i < maps.length; i++) {
            row = table.insertRow(-1);
            mapName = row.insertCell(0);
            creator = row.insertCell(1);
            lastEditor = row.insertCell(2);
            action = row.insertCell(3);
            mapName.innerHTML = maps[i].name;
            creator.innerHTML = maps[i].createdBy;
            lastEditor.innerHTML = maps[i].lastEditedBy;
            action.innerHTML = "<a href='http://localhost:8080/kickInTeam26/mapView.html?mapId=" +
                maps[i].mapId + "' class='text-success'><i class='glyphicon glyphicon-eye-open' " +
                "style='font-size:20px;'></i></a><a href='http://localhost:8080/kickInTeam26/mapEdit.html?id=" +
                maps[i].mapId + "' class='text-success'><i class='glyphicon glyphicon-pencil' " +
                "style='font-size:20px;'></i></a><a href='javascript: window.confirmDelete(" + maps[i].mapId + ")'" +
                "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        }
    })
}

function confirmDelete(mapId) {
	deleteMap(mapId, function() {
		console.log(this.responseText);
		location.reload();
	})
}
window.onload = displayEventInfo;
