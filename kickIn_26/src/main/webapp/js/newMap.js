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
    		window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
    	})
    })
}