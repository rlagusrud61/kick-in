//function updateEvent() {
//    description = document.getElementById("eventdescription").value;
//    eventId = window.location.search.split("=")[1];
//    namestuff = document.getElementById("eventname").value;
//    locationstuff = document.getElementById("eventlocation");
//    eventloc = locationstuff.options[locationstuff.selectedIndex].value;
//    eventdate = document.getElementById("eventdate").value;
//    eventjson = {
//        "description": description,
//        "date": evendate,
//        "location": eventloc,
//        "name": namestuff
//    };
//    console.log(JSON.stringify(eventjson));
//    var xhr = new XMLHttpRequest();
//    xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/event/" + eventId, true);
//    xhr.onreadystatechange = function () {
//        if ((xhr.readyState === 4) && (xhr.status = 200)) {
//            console.log(xhr.responseText);
//            window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
//        }
//    }
//    xhr.setRequestHeader("Content-Type", "application/json");
//    xhr.send(JSON.stringify(eventjson));
//}

function upEvent() {
	let eventDescription, eventId, eventName, eventLoc, eventLocation, eventDate, eventJSON
    eventDescription = document.getElementById("eventdescription").value;
    eventId = window.location.search.split("=")[1];
    eventName = document.getElementById("eventname").value;
    eventLoc = document.getElementById("eventlocation");
    eventLocation = eventLoc.options[eventLoc.selectedIndex].value
    eventDate = document.getElementById("eventDate").value;
    eventJSON = {
        "description": eventDescription,
        "date": eventDate,
        "eventId": eventId,
        "location": eventLocation,
        "name": eventName
    };
    updateEvent(eventJSON, function() {
    	window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
    })
}