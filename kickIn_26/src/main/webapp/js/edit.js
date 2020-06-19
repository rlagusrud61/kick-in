function updateEventData() {
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