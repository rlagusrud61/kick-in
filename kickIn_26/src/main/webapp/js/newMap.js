/**
 * @summary This function is used to add a new map to the database and link it to the required event.
 *
 * @description First, the name and description of the map are retrieved from the web page and the ID of the event for
 * which the map is to be added is retrieved from the URL. A JSON object is then created from the name and description of
 * the map and the 'addMap' function is called with the JSON object as a parameter. If the map is successfully added to
 * the database, the ID of the map created is retrieved from the response from the back-end and a new JSON object created with
 * the IDs of both the map and event to be linked. This JSON object is then taken as a parameter to the 'addEventMap'
 * method which is used to link together the map and the event. If the association was created successfully, the user is
 * redirected to the page of the event for which the map was added.
 */
function addNewMap() {
    let mapName, description, mapId, eventId, mapJSON, eventMapJSON;
    mapName = document.getElementById("mapName").value;
    description = document.getElementById("description").value;
    eventId = window.location.search.split("=")[1];
    mapJSON = {
    		"name" : mapName,
    		"description" : description
    }
    console.log(mapJSON)
    addMap(mapJSON, function() {
    	mapId = parseInt(this.responseText);
    	eventMapJSON = {
    			"eventId": eventId,
    			"mapId": mapId
    	}
    	addEventMap(eventMapJSON, function (){
    		window.location.href = "event.html?id=" + eventId;
    	})
    })
}