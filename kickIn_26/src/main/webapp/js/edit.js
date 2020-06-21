/**
 * @summary This method is used to update the information on a specific event.
 *
 * @description The values of the description, name, location and date of the event are retrieved from the web page
 * and the ID of the event to be updated is retrieved from the URL. This information is then used to create a JSON
 * object which is taken as a parameter to the function 'updateEvent' so that the information on the event can be
 * updated in the database. Once the information is updated, the user is redirected to the page of the specific event.
 */
function updateEventData() {
	let eventDescription, eventId, eventName, eventLoc, eventLocation, eventDate, eventJSON;
    eventDescription = document.getElementById("eventDescription").value;
    eventId = window.location.search.split("=")[1];
    eventName = document.getElementById("eventName").value;
    eventLoc = document.getElementById("eventLocation");
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