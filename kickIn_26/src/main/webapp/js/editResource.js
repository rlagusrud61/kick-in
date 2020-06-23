/**
 * @summary This method is used to update the information on a specific resource.
 *
 * @description The values of the description, name, and image of the resource are retrieved from the web page
 * and the ID of the resource to be updated is retrieved from the URL. This information is then used to create a JSON
 * object which is taken as a parameter to the function 'updateResource' so that the information on the resource can be
 * updated in the database. Once the information is updated, the user is redirected to the 'resources.html' page.
 */
function updateEventData() {
    let eventDescription, eventId, eventName, eventLoc, eventLocation, eventDate, eventJSON;
    eventDescription = document.getElementById("eventDescription").value;
    eventId = window.location.search.split("=")[1];
    eventName = document.getElementById("eventName").value;
    eventLoc = document.getElementById("eventLocation");
    eventLocation = eventLoc.options[eventLoc.selectedIndex].value;
    eventDate = document.getElementById("eventDate").value;
    eventJSON = {
        "description": eventDescription,
        "date": eventDate,
        "eventId": eventId,
        "location": eventLocation,
        "name": eventName
    };
    updateEvent(eventJSON, function() {
        window.location.href = "event.html?id=" + eventId;
    })
}